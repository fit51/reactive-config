package com.github.fit51.reactiveconfig.etcd

import scala.concurrent.duration._
import monix.eval.Task
import monix.execution.{Ack, Scheduler}
import monix.reactive.{Consumer, Observer}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Future

/**
  * This is service test, start etcd before running.
  * Run docker command, it will start etcd on 127.0.0.1:2379 without authentication:
  * sudo docker run -e ALLOW_NONE_AUTHENTICATION=yes -p 2379:2379 bitnami/etcd:latest
  */
class EtcdClientTest extends WordSpecLike with BeforeAndAfterAll with Matchers {
  import EtcdUtils._
  implicit val scheduler = Scheduler.global

  val chManager  = ChannelManager.noAuth("http://127.0.0.1:2379")
  val etcdClient = new EtcdClient[Task](chManager) with Watch[Task]

  override def beforeAll(): Unit = {}

  override def afterAll(): Unit = {
    etcdClient.close()
  }

  case class KV(k: String, v: String)

  "EtcdClient" should {
    "put and get" in {
      val t = for {
        _  <- etcdClient.put("key", "value")
        kv <- etcdClient.get("key")
      } yield {
        kv.get.value.utf8 shouldEqual "value"
      }
      t.runSyncUnsafe(3 seconds)
    }

    "watch" in {
      val chStream = etcdClient.watch(EtcdUtils.getRange("some.key.prefix"))

      val updates = List(
        KV("some.key.prefix.key1", "v1"),
        KV("some.key.prefix.key2", "v1"),
        KV("some.key.prefix.key1", "v2"),
        KV("some.key.prefix.key2", "v2")
      )

      val task = for {
        changeStream <- chStream.map(_.map(kv => KV(kv.key.utf8, kv.value.utf8)).dump("kv"))
        changesTask  <- changeStream.take(4).toListL.start
        _            <- Task.traverse(updates)(kv => etcdClient.put(kv.k, kv.v))
        changes      <- changesTask.join
      } yield {
        changes should contain theSameElementsAs updates
      }

      task.runSyncUnsafe()
    }
  }
}
