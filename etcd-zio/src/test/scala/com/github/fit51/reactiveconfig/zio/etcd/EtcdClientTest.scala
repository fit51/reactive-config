package com.github.fit51.reactiveconfig.zio.etcd

import cats.data.NonEmptySet
import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.ChannelManager
import com.github.fit51.reactiveconfig.etcd.ChannelOptions
import com.github.fit51.reactiveconfig.etcd.gen.rpc.ZioRpc._
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.parser.ConfigParser
import org.scalatest.{Matchers, WordSpecLike}
import org.testcontainers.containers.wait.strategy.Wait
import zio._
import zio.clock._
import zio.duration.Duration

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scalapb.zio_grpc.ZManagedChannel

class EtcdClientTest extends WordSpecLike with Matchers with ForAllTestContainer {

  override val container: GenericContainer = GenericContainer(
    "bitnami/etcd:latest",
    exposedPorts = List(2379),
    env = Map("ALLOW_NONE_AUTHENTICATION" -> "yes"),
    waitStrategy = Wait.forHttp("/health")
  )

  lazy val channel = ZManagedChannel(
    ChannelManager
      .noAuth(
        endpoints = s"${container.containerIpAddress}:${container.mappedPort(2379)}",
        options = ChannelOptions(20 seconds),
        authority = None,
        trustManagerFactory = None
      ).channelBuilder
  )

  implicit val parser: ConfigParser[String]           = ConfigParser.identity
  implicit val decoder: ConfigDecoder[String, String] = ConfigDecoder.identity

  lazy val layer =
    (Clock.live ++ KVClient.live(channel) ++ WatchClient.live(channel)) >+> EtcdClient.live >+> EtcdReactiveConfig.live(
      NonEmptySet.one("some.key.prefix"),
      Duration.fromScala(5 seconds)
    )

  val runtime = Runtime.default

  "An EtcdClient" should {

    "put and get" in {
      val task = (for {
        etcdClient <- ZIO.service[EtcdClient]
        _          <- etcdClient.put("key", "value")
        kv         <- etcdClient.get("key")
      } yield kv.get.value.utf8 shouldBe "value").provideLayer(layer)

      runtime.unsafeRunSync(task)
    }

    val keyRange = "some.key.prefix".asKeyRange

    val updates = List(
      ("some.key.prefix.key1", "v1"),
      ("some.key.prefix.key2", "v3"),
      ("some.key.prefix.key1", "v2"),
      ("some.key.prefix.key2", "v4")
    )

    "watch" in {
      val task = (for {
        etcdClient <- ZIO.service[EtcdClient]
        _          <- etcdClient.put("some.key.prefix.key1", "v0")
        _          <- etcdClient.put("some.key.prefix.key2", "v0")

        etcdConfig <- ZIO.service[EtcdReactiveConfig[String]]

        values1Ref <- Ref.make[List[String]](Nil)
        values2Ref <- Ref.make[List[String]](Nil)

        (values1, values2) <- etcdConfig
          .reloadable[String]("some.key.prefix.key1")
          .zip(etcdConfig.reloadable[String]("some.key.prefix.key2")).use { case (reloadable1, reloadable2) =>
            reloadable1.forEachF(str => values1Ref.updateAndGet(str :: _).unit).fork *>
              reloadable2.forEachF(str => values2Ref.updateAndGet(str :: _).unit).fork *>
              ZIO.foreach(updates)((etcdClient.put _).tupled) *>
              ZIO.sleep(Duration.fromScala(5 seconds)) *>
              values1Ref.get.zip(values2Ref.get)
          }
      } yield {
        values1 should contain theSameElementsAs List("v0", "v1", "v2")
        values2 should contain theSameElementsAs List("v0", "v3", "v4")
      }).provideLayer(layer)

      runtime.unsafeRunSync(task)
    }
  }
}
