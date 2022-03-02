package com.github.fit51.reactiveconfig.ce.etcd

import cats.data.NonEmptySet
import cats.effect.ContextShift
import cats.effect.IO
import cats.effect.Timer
import cats.effect.concurrent.Ref
import cats.syntax.foldable._
import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.ChannelManager
import com.github.fit51.reactiveconfig.etcd.ChannelOptions
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.parser.ConfigParser
import org.scalatest.{Matchers, WordSpecLike}
import org.testcontainers.containers.wait.strategy.Wait

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class EtcdClientTest extends WordSpecLike with Matchers with ForAllTestContainer {

  implicit val timer: Timer[IO]     = IO.timer(ExecutionContext.Implicits.global)
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.Implicits.global)

  override val container: GenericContainer = GenericContainer(
    "bitnami/etcd:latest",
    exposedPorts = List(2379),
    env = Map("ALLOW_NONE_AUTHENTICATION" -> "yes"),
    waitStrategy = Wait.forHttp("/health")
  )

  lazy val channel = ChannelManager
    .noAuth(
      endpoints = s"${container.containerIpAddress}:${container.mappedPort(2379)}",
      options = ChannelOptions(20 seconds),
      authority = None,
      trustManagerFactory = None
    ).channelBuilder

  "An EtcdClient" should {

    "put and get" in {
      val etcdClient = EtcdClient[IO](channel.build())
      val task = for {
        _  <- etcdClient.put("key", "value")
        kv <- etcdClient.get("key")
      } yield kv.get.value.utf8 shouldBe "value"

      task.unsafeRunSync()
    }

    val updates = List(
      ("some.key.prefix.key1", "v1"),
      ("some.key.prefix.key2", "v3"),
      ("some.key.prefix.key1", "v2"),
      ("some.key.prefix.key2", "v4")
    )

    implicit val parser: ConfigParser[String]           = ConfigParser.identity
    implicit val decoder: ConfigDecoder[String, String] = ConfigDecoder.identity

    "watch" in {
      val etcdClient = EtcdClient[IO](channel.build())

      val reloadables = for {
        etcdConfig  <- EtcdReactiveConfig[IO, String](channel.build(), NonEmptySet.one("some.key"))
        reloadable1 <- etcdConfig.reloadable[String]("some.key.prefix.key1")
        reloadable2 <- etcdConfig.reloadable[String]("some.key.prefix.key2")
      } yield (reloadable1, reloadable2)

      val task = etcdClient.put("some.key.prefix.key1", "v0") *>
        etcdClient.put("some.key.prefix.key2", "v0") *>
        reloadables.use { case (reloadable1, reloadable2) =>
          for {
            values1Ref <- Ref.of[IO, List[String]](Nil)
            values2Ref <- Ref.of[IO, List[String]](Nil)

            fiber1 <- reloadable1.forEachF(str => values1Ref.updateAndGet(str :: _).void).start
            fiber2 <- reloadable2.forEachF(str => values2Ref.updateAndGet(str :: _).void).start

            _ <- updates.traverse_((etcdClient.put _).tupled)
            _ <- timer.sleep(5 seconds)

            _ <- fiber1.cancel
            _ <- fiber2.cancel

            values1 <- values1Ref.get
            values2 <- values2Ref.get
          } yield {
            values1 should contain theSameElementsAs List("v0", "v1", "v2")
            values2 should contain theSameElementsAs List("v0", "v3", "v4")
          }
        }

      task.unsafeRunSync()
    }
  }
}
