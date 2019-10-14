package com.github.fit51.reactiveconfig.etcd

import scala.concurrent.duration._
import monix.eval.Task
import monix.execution.Scheduler
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * This is service test, start etcd before running.
  * Run docker command, it will start etcd on 127.0.0.1:2380 without authentication:
  * sudo docker run -e ALLOW_NONE_AUTHENTICATION=yes bitnami/etcd:latest
  */
class EtcdClientTest extends WordSpecLike with BeforeAndAfterAll with Matchers {
  import EtcdUtils._
  implicit val scheduler = Scheduler.global

  val chManager  = ChannelManager.noAuth("http://127.0.0.1:2379")
  val etcdClient = new EtcdClient[Task](chManager)

  override def beforeAll(): Unit = {}

  override def afterAll(): Unit = {
    etcdClient.close()
  }

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
  }
}
