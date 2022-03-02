package com.github.fit51.reactiveconfig.ce.typesafe

import java.nio.file.Paths

import cats.effect.{Blocker, ContextShift, IO, Timer}
import com.github.fit51.reactiveconfig.parser.{ConfigDecoder, ConfigParser}
import fs2.Stream
import io.circe.{parser, Decoder, Json}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class App(net: Net)
case class Net(host: String, port: Port)
case class Port(public: Int, `private`: Int)
case class Parameter(value: Int)

class TypesafeReactiveConfigTest extends WordSpecLike with Matchers {

  implicit val configParser: ConfigParser[Json] =
    parser.parse(_).toTry

  implicit def circeDecoderToConfigDecoder[T](implicit decoder: Decoder[T]): ConfigDecoder[T, Json] =
    json => decoder.apply(json.hcursor).toTry

  implicit val portDecoder: Decoder[Port] =
    cursor =>
      for {
        public <- cursor.get[Int]("public")
        prvt   <- cursor.get[Int]("private")
      } yield Port(public, prvt)
  implicit val netDecoder: Decoder[Net] =
    cursor =>
      for {
        host <- cursor.get[String]("host")
        port <- cursor.get[Port]("port")
      } yield Net(host, port)
  implicit val appDecoder: Decoder[App] =
    _.get[Net]("net").map(App)
  implicit val parameterDecoder: Decoder[Parameter] =
    _.get[Int]("value").map(Parameter)

  implicit val shift: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO]        = IO.timer(global)

  val path           = Paths.get("typesafe/src/test/resources/application.conf")
  val blocker        = Blocker.liftExecutionContext(global)
  val configResource = TypesafeReactiveConfig[IO, Json](blocker, path)

  def changeable(v: Int): String =
    s"""changeable {
       |  parameter {
       |    value: $v
       |  }
       |}
    """.stripMargin

  "TypesafeConfig" should {

    "properly fetch values with different paths" in {
      configResource.use { config =>
        for {
          simplePort <- config.get[Int]("app.net.port.public")
          host       <- config.get[String]("app.net.host")
          port       <- config.get[Port]("app.net.port")
          net        <- config.get[Net]("app.net")
          app        <- config.get[App]("app")
        } yield {
          simplePort shouldBe 8080
          host shouldBe "0.0.0.0"
          port shouldBe Port(8080, 9090)
          net shouldBe Net("0.0.0.0", Port(8080, 9090))
          app shouldBe App(Net("0.0.0.0", Port(8080, 9090)))
        }
      }.unsafeRunSync()
    }

    "fetch values from included config" in {
      configResource.use { config =>
        for {
          parallelism <- config.get[Int]("akka.kafka.producer.parallelism")
          hostname    <- config.get[String]("akka.remote.netty.tcp.hostname")
        } yield {
          parallelism shouldBe 10
          hostname shouldBe "0.0.0.0"
        }
      }.unsafeRunSync()
    }

    "config should be able to reload case class on change" in {
      configResource
        .flatMap(_.reloadable[Parameter]("changeable.parameter"))
        .use { reloadable =>
          for {
            first <- reloadable.get
            _ <- fs2.io.file
              .writeAll[IO](path.getParent().resolve("changeable.conf"), blocker)
              .apply(Stream.fromIterator[IO](changeable(2).getBytes().iterator))
              .compile
              .drain
            _      <- IO.sleep(10 seconds)
            second <- reloadable.get
          } yield {
            first shouldBe Parameter(1)
            second shouldBe Parameter(2)
          }
        }
        .guarantee {
          fs2.io.file
            .writeAll[IO](path.getParent().resolve("changeable.conf"), blocker)
            .apply(Stream.fromIterator[IO](changeable(1).getBytes().iterator))
            .compile
            .drain
        }
        .unsafeRunSync()
    }
  }
}
