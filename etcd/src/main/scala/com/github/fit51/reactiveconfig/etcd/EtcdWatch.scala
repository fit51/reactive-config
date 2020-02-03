package com.github.fit51.reactiveconfig.etcd

import cats.effect.concurrent.{Deferred, MVar}
import cats.syntax.applicative._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.etcd.gen.kv.Event.EventType
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest.RequestUnion.CreateRequest
import com.github.fit51.reactiveconfig.etcd.gen.rpc.{WatchCreateRequest, WatchGrpc, WatchRequest, WatchResponse}
import com.github.fit51.reactiveconfig.etcd.GrpcMonix.monixSubscriberToGrpcObserver
import monix.catnap.CircuitBreaker
import monix.eval.Task
import monix.execution.Ack.{Continue, Stop}
import monix.execution.exceptions.ExecutionRejectedException
import monix.execution.{Ack, Scheduler}
import monix.reactive.Observable
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.PublishSubject
import monix.eval.TaskLift

import scala.concurrent.duration._
import scala.concurrent.Future

trait Watch[F[_]] {
  self: EtcdClient[F] =>
  import monix.execution.schedulers.CanBlock.permit
  import EtcdUtils._

  implicit def taskLift: TaskLift[F]

  /**
    * This CB protects subscribe method.
    */
  private val circuitBreaker = CircuitBreaker[Task].unsafe(
    maxFailures = 2,
    resetTimeout = 4.seconds,
    onOpen = Task {
      logger.error("ETCD: Watch is unavailiable!")
    },
    onHalfOpen = Task {
      logger.warn("ETCD: Watch is trying to connect!")
    },
    onClosed = Task {
      logger.warn("ETCD: Watch connected to Etcd!")
    }
  )

  private lazy val watchService = WatchGrpc.stub(manager.channel)

  type WatchId = Long
  private val watchIds = MVar[Task].of[Map[String, WatchId]](Map.empty).runSyncUnsafe()
  private def putWatchId(key: String, id: WatchId): Task[Unit] =
    watchIds.take.flatMap(m => watchIds.put(m + (key -> id)))
  private def removeWatchId(key: String): Task[Unit] =
    watchIds.take.flatMap(m => watchIds.put(m - key))

  /**
    * @param subscriber will be subscribed to keyRange Events
    * @param p          is completed when "Created" WatchResponse is received.
    *                   If error occurs protectedSubscribe is called and p is completed with it's result.
    */
  private def subscribe(subscriber: Subscriber[KeyValue], keyRange: KeyRange, p: Deferred[Task, Unit]): Unit = {
    val s = new Subscriber[WatchResponse] {
      override implicit def scheduler: Scheduler = self.scheduler

      override def onNext(elem: WatchResponse): Future[Ack] = elem match {
        case v: WatchResponse if !v.created && !v.canceled =>
          val keyValues = v.events.flatMap { ev =>
            if (ev.`type` == EventType.PUT) ev.kv else None
          }
          subscriber.feed(keyValues)
        case v if v.created =>
          logger.info("Subscribed on Watch!")
          putWatchId(keyRange.start, v.watchId) >>
            p.complete(()).attempt.as(Continue) runToFuture
        case v if v.canceled =>
          logger.warn("Etcd Watch cancelled")
          removeWatchId(keyRange.start).map { _ =>
            // Run in background
            protectedSubscribe(subscriber, keyRange, p).runToFuture
          }.as(Stop).runToFuture
      }

      override def onError(ex: Throwable): Unit = {
        logger.error("ETCD: Watch requestObserver crashed ", ex)
        removeWatchId(keyRange.start).map { _ =>
          // Run in background
          protectedSubscribe(subscriber, keyRange, p).runToFuture
        }.runToFuture
      }

      override def onComplete(): Unit = {
        logger.warn(s"ETCD: Watch finished")
        removeWatchId(keyRange.start).runToFuture
      }
    }

    val observer = watchService.watch(monixSubscriberToGrpcObserver(s))
    observer.onNext(
      WatchRequest(
        CreateRequest(WatchCreateRequest(keyRange.start.bytes, keyRange.end.bytes))
      )
    )
  }

  private def protectedSubscribe(
      subscriber: Subscriber[KeyValue],
      keyRange: KeyRange,
      p: Deferred[Task, Unit]
  ): Task[Unit] =
    circuitBreaker.protect {
      for {
        map <- watchIds.read
        id = map.get(keyRange.start)
        _ <- Task.raiseError(new Exception("Watch already exists")).whenA(id.nonEmpty)
        _ = subscribe(subscriber, keyRange, p)
        _ <- p.get
      } yield ()
    }.onErrorRecoverWith {
      case _: ExecutionRejectedException =>
        circuitBreaker.awaitClose >> protectedSubscribe(subscriber, keyRange, p)
    }

  /** Subscribes on Watch Events for defined keyRange
    */
  def watch(keyRange: KeyRange): F[Observable[KeyValue]] = {
    val watchSubject = PublishSubject[KeyValue]
    Deferred[Task, Unit].flatMap { subscribed =>
      protectedSubscribe(Subscriber(watchSubject, scheduler), keyRange, subscribed)
    }.as(watchSubject: Observable[KeyValue]).to[F]
  }
}
