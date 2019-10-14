package com.github.fit51.reactiveconfig.etcd

import monix.eval.Task
import monix.execution.Ack.{Continue, Stop}
import monix.execution.{Ack, Scheduler}
import monix.reactive.Observable
import monix.reactive.observers.Subscriber
import monix.reactive.subjects.PublishSubject
import com.github.fit51.reactiveconfig.etcd.gen.kv.Event.EventType
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest.RequestUnion.CreateRequest
import com.github.fit51.reactiveconfig.etcd.gen.rpc.{WatchCreateRequest, WatchGrpc, WatchRequest, WatchResponse}
import com.github.fit51.reactiveconfig.etcd.GrpcMonix.monixSubscriberToGrpcObserver
import monix.catnap.CircuitBreaker

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

trait Watch[F[_]] {
  this: EtcdClient[F] =>

  import EtcdUtils._

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

  /**
    * @param subscriber will be subscribed to keyRange Events
    * @param p          is completed when "Created" WatchResponce is received.
    *                   If error occurs protectedSubscribe is called and p is completed with it's result.
    */
  private def subscribe(subscriber: Subscriber[KeyValue], keyRange: KeyRange, p: Promise[Unit]): Unit = {
    val s = new Subscriber[WatchResponse] {
      override implicit def scheduler: Scheduler = subscriber.scheduler

      override def onNext(elem: WatchResponse): Future[Ack] = elem match {
        case v: WatchResponse if !v.created && !v.canceled =>
          val keyValues = v.events.flatMap { ev =>
            if (ev.`type` == EventType.PUT) ev.kv else None
          }
          subscriber.feed(keyValues)
        case v if v.created =>
          logger.info("Subscribed on Watch!")
          p.completeWith(Future.unit)
          Future.successful(Continue)
        case v if v.canceled =>
          logger.warn("Etcd Watch cancelled")
          p.completeWith(protectedSubscribe(subscriber, keyRange, p).runToFuture)
          Stop
      }

      override def onError(ex: Throwable): Unit = {
        logger.error("ETCD: Watch requestObserver crashed ", ex)
        p.completeWith(protectedSubscribe(subscriber, keyRange, p).runToFuture)
      }

      override def onComplete(): Unit =
        logger.warn(s"ETCD: Watch finished")
    }
    val observer = watchService.watch(monixSubscriberToGrpcObserver(s))
    observer.onNext(
      WatchRequest(
        CreateRequest(WatchCreateRequest(keyRange.start.bytes, keyRange.end.bytes))
      )
    )
  }

  private def protectedSubscribe(subscriber: Subscriber[KeyValue], keyRange: KeyRange, p: Promise[Unit]): Task[Unit] =
    circuitBreaker.protect(Task.deferFuture {
      subscribe(subscriber, keyRange, p)
      p.future
    })

  /** Subscribes on Watch Events for defined keyRange
    */
  def watch(keyRange: KeyRange)(implicit scheduler: Scheduler): Observable[KeyValue] = {
    // Promise for subscription is created only once here and is passed to protectedSubscribe loop.
    val subscribed   = Promise[Unit]
    val watchSubject = PublishSubject[KeyValue]

    Observable
      .fromTask(protectedSubscribe(Subscriber(watchSubject, scheduler), keyRange, subscribed))
      .flatMap(_ => watchSubject)
  }
}
