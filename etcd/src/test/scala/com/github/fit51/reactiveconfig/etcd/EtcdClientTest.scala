package com.github.fit51.reactiveconfig.etcd

import io.grpc.stub.StreamObserver
import monix.eval.{Task, TaskLift}
import monix.execution.Scheduler
import monix.reactive.observers.Subscriber
import org.scalatest.{Assertion, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.util.Try

/**
  * This is service test, start etcd before running.
  * Run docker command, it will start etcd on 127.0.0.1:2379 without authentication:
  * sudo docker run -e ALLOW_NONE_AUTHENTICATION=yes -p 2379:2379 bitnami/etcd:latest
  */
class EtcdClientTest extends WordSpecLike with Matchers {
  import EtcdUtils._
  implicit val scheduler = Scheduler.global

  def init: EtcdClient[Task] with Watch[Task] = {
    val chManager = ChannelManager.noAuth("http://127.0.0.1:2379")
    new EtcdClient[Task](chManager) with Watch[Task] {
      val taskLift                                                    = TaskLift[Task]
      override def monixToGrpc[T]: Subscriber[T] => StreamObserver[T] = GrpcMonix.monixToGrpcObserverBuffered
    }
  }

  def close(etcdClient: EtcdClient[Task]): Unit = {
    etcdClient.close()
  }

  case class KV(k: String, v: String)

  def contractBreakingImplMonixToGrpcObserver[T](subscriber: Subscriber[T]): StreamObserver[T] =
    new StreamObserver[T] {
      override def onError(t: Throwable): Unit = subscriber.onError(t)
      override def onCompleted(): Unit         = subscriber.onComplete()
      // No backpressure here, breaking the observer contract
      override def onNext(value: T): Unit = subscriber.onNext(value)
    }

  def testWatch(etcdClient: EtcdClient[Task] with Watch[Task]): Task[Assertion] = {
    val chStream = etcdClient.watch(EtcdUtils.getRange("some.key.prefix"))

    val updates = List(
      KV("some.key.prefix.key1", "v1"),
      KV("some.key.prefix.key2", "v1"),
      KV("some.key.prefix.key1", "v2"),
      KV("some.key.prefix.key2", "v2")
    )

    for {
      changeStream <- chStream.map(
        _.map(kv => KV(kv.key.utf8, kv.value.utf8))
          .delayOnNext(1 second)
          .dump("kv")
      )
      changesTask <- changeStream.take(updates.length).toListL.start
      _           <- Task.gather(updates.map(kv => etcdClient.put(kv.k, kv.v)))
      changes     <- changesTask.join
    } yield {
      changes should contain theSameElementsAs updates
    }
  }

  "EtcdClient" should {
    "put and get" in {
      val etcdClient = init

      val t = for {
        _  <- etcdClient.put("key", "value")
        kv <- etcdClient.get("key")
      } yield {
        kv.get.value.utf8 shouldEqual "value"
      }
      t.runSyncUnsafe(3 seconds)

      close(etcdClient)
    }

    "watch" in {
      val etcdClient = init

      val task = testWatch(etcdClient)
      task.runSyncUnsafe()

      close(etcdClient)
    }

    "break during watch with Invalid StreamObserver converter" in {
      val chManager = ChannelManager.noAuth("http://127.0.0.1:2379")
      val etcdClient = new EtcdClient[Task](chManager) with Watch[Task] {
        val taskLift = TaskLift[Task]

        override def monixToGrpc[T]: Subscriber[T] => StreamObserver[T] = contractBreakingImplMonixToGrpcObserver
      }

      val tried = Try { testWatch(etcdClient).runSyncUnsafe() }
      tried.isFailure shouldBe true

      close(etcdClient)
    }
  }
}
