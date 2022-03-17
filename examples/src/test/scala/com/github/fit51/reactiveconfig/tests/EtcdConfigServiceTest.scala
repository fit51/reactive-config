package com.github.fit51.reactiveconfig.tests

import cats.data.NonEmptySet
import cats.effect.Resource
import cats.implicits._
import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import io.circe.Json
import io.circe.parser._
import io.grpc.stub.StreamObserver
import monix.eval.{Task, TaskLift}
import monix.reactive.observers.Subscriber
import org.scalatest.concurrent.Eventually
import org.scalatest.{Matchers, WordSpecLike}
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.Await
import scala.concurrent.duration._

/** This is service test, which starts the test container
  * based on the image bitnami/etcd:latest
  */
class EtcdConfigServiceTest extends WordSpecLike with Matchers with Eventually with ForAllTestContainer {
  import monix.execution.Scheduler.Implicits.global

  override val container: GenericContainer = GenericContainer(
    "bitnami/etcd:latest",
    exposedPorts = List(2379),
    env = Map("ALLOW_NONE_AUTHENTICATION" -> "yes"),
    waitStrategy = Wait.forHttp("/health")
  )

  case class KV(k: String, v: String)
  implicit class AwaitTask[T](f: Task[T]) {
    def get: T = Await.result(f.runToFuture, 10.minutes)
  }
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 3.seconds)

  case class Init(
      client: EtcdClient[Task],
      r1: Reloadable[Task, String],
      r2: Reloadable[Task, String],
      release: Task[Unit]
  )

  def init: Task[Init] = {
    import com.github.fit51.reactiveconfig.parser.CirceConfigDecoder.decoder
    import com.github.fit51.reactiveconfig.parser.CirceConfigParser.parser

    val chManager = ChannelManager.noAuth(s"${container.containerIpAddress}:${container.mappedPort(2379)}")
    val etcdClient = new EtcdClient[Task](chManager) with Watch[Task] {
      val taskLift: TaskLift[Task]                                    = TaskLift[Task]
      override val errorRetryPolicy: RetryPolicy                      = SimpleDelayPolicy(10 seconds)
      override def monixToGrpc[T]: Subscriber[T] => StreamObserver[T] = GrpcMonix.monixToGrpcObserverBuffered
    }

    (for {
      _      <- Resource.liftF(data.traverse(kv => etcdClient.put(kv.k, kv.v)))
      config <- ReactiveConfigEtcd[Task, Json](etcdClient, NonEmptySet.of("common", "prefix1"))
      r1     <- config.reloadable[String]("common.key.prefix.key1")
      r2     <- config.reloadable[String]("prefix1.key.prefix.key2")
    } yield (r1, r2)).allocated.map { case (reloadables, release) =>
      Init(etcdClient, reloadables._1, reloadables._2, release)
    }
  }

  def close(init: Init): Unit =
    init.client.close()

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

  def check(rl: List[Reloadable[Task, String]], v: List[KV]): Task[Boolean] =
    rl.zip(v)
      .traverse { case (r, kv) =>
        r.get.map { v =>
          println(s"Current value: $v")
          v == parse(kv.v).getOrElse(Json.Null).as[String].getOrElse("")
        }
      }
      .map(_.forall(_ == true))

  "Reactive etcd config" should {
    "subscriber on key changes on multiple prefixes" in {
      val in = init.get
      eventually(check(List(in.r1, in.r2), data).get shouldEqual true)

      updates.traverse(kv => in.client.put(kv.k, kv.v)).get
      eventually(check(List(in.r1, in.r2), updates).get shouldEqual true)

      updates2.traverse(kv => in.client.put(kv.k, kv.v)).get
      eventually(check(List(in.r1, in.r2), updates2).get shouldEqual true)

      close(in)
    }
  }
}
