package com.github.fit51.reactiveconfig.tests

import cats.data.NonEmptySet
import cats.implicits._
import com.github.fit51.reactiveconfig.config.ReactiveConfig
import com.github.fit51.reactiveconfig.etcd.{ChannelManager, EtcdClient, ReactiveConfigEtcd, Watch}
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import io.circe.Json
import monix.eval.{Task, TaskLift}
import org.scalatest.{Matchers, WordSpecLike}
import io.circe.parser._
import org.scalatest.concurrent.Eventually

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * This is service test, start etcd before running.
  * Run docker command, it will start etcd on 127.0.0.1:2379 without authentication:
  * sudo docker run -e ALLOW_NONE_AUTHENTICATION=yes -p 2379:2379 bitnami/etcd:latest
  */
class EtcdConfigServiceTest extends WordSpecLike with Matchers with Eventually {
  import monix.execution.Scheduler.Implicits.global

  case class KV(k: String, v: String)
  implicit class AwaitTask[T](f: Task[T]) {
    def get: T = Await.result(f.runToFuture, 10.minutes)
  }
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 3.seconds)

  case class Init(
      client: EtcdClient[Task],
      config: ReactiveConfig[Task, Json],
      r1: Reloadable[Task, String],
      r2: Reloadable[Task, String]
  )

  def init: Task[Init] = {
    import com.github.fit51.reactiveconfig.parser.CirceConfigParser.parser
    import com.github.fit51.reactiveconfig.parser.CirceConfigDecoder.decoder

    val chManager = ChannelManager.noAuth("http://127.0.0.1:2379")
    val etcdClient = new EtcdClient[Task](chManager) with Watch[Task] {
      val taskLift = TaskLift[Task]
    }

    for {
      _      <- data.traverse(kv => etcdClient.put(kv.k, kv.v))
      config <- ReactiveConfigEtcd[Task, Json](etcdClient, NonEmptySet.of("common", "prefix1"))
      r1     <- config.reloadable[String]("common.key.prefix.key1")
      r2     <- config.reloadable[String]("prefix1.key.prefix.key2")
    } yield Init(etcdClient, config, r1, r2)
  }

  def close(init: Init): Unit = {
    init.client.close()
  }

  val data = List(
    KV("common.key.prefix.key1", "\"v1\""),
    KV("prefix1.key.prefix.key2", "\"v1\"")
  )

  val updates = List(
    KV("common.key.prefix.key1", "\"v2\""),
    KV("prefix1.key.prefix.key2", "\"v2\"")
  )

  val updates2 = List(
    KV("prefix1.key.prefix.key2", "\"v3\""),
    KV("common.key.prefix.key1", "\"v3\"")
  )

  def check(rl: List[Reloadable[Task, String]], v: List[KV]): Task[Boolean] = {
    rl.zip(v)
      .traverse {
        case (r, kv) =>
          r.get.map { v =>
            println(s"Current value: $v")
            v == parse(kv.v).getOrElse(Json.Null).as[String].getOrElse("")
          }
      }
      .map(_.forall(_ == true))
  }

  "Reactive etcd config" should {
    "subscriber on key changes on multiple prefixes" in {
      val in = init.get
      eventually { check(List(in.r1, in.r2), data).get shouldEqual true }

      updates.traverse(kv => in.client.put(kv.k, kv.v)).get
      eventually { check(List(in.r1, in.r2), updates).get shouldEqual true }

      updates2.traverse(kv => in.client.put(kv.k, kv.v)).get
      eventually { check(List(in.r1, in.r2), updates2).get shouldEqual true }

      close(in)
    }
  }
}
