package com.github.fit51.reactiveconfig.typesafe

import better.files.File
import cats.effect.Clock
import cats.effect.IO
import com.github.fit51.reactiveconfig.config.ReactiveConfigImpl
import io.circe.generic.auto._
import io.circe.Json
import java.nio.file.Paths
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

class TypesafeConfigStorageTest extends WordSpecLike with Matchers with MockitoSugar {
  import TypesafeConfigStorageTest._
  import com.github.fit51.reactiveconfig.parser.CirceConfigDecoder.decoder
  import com.github.fit51.reactiveconfig.parser.CirceConfigParser.parser

  trait mocks {
    val path    = Paths.get("typesafe/src/test/resources/application.conf")
    val storage = TypesafeConfigStorage[IO, Json](path)
    val config  = Await.result(ReactiveConfigImpl[IO, Json](storage).unsafeToFuture, 1 second)

    implicit val timer = new cats.effect.Timer[IO] {
      override def clock: Clock[IO] =
        Clock.create
      override def sleep(duration: FiniteDuration): IO[Unit] =
        Task.sleep(duration).to[IO]
    }
  }

  "TypesafeConfig" should {

    "properly fetch values with different paths" in new mocks {
      config.unsafeGet[Int]("app.net.port.public").shouldEqual(Success(8080))
      config.unsafeGet[String]("app.net.host").shouldEqual(Success("0.0.0.0"))
      config.unsafeGet[Port]("app.net.port").shouldEqual(Success(Port(8080, 9090)))
      config.unsafeGet[Net]("app.net").shouldEqual(Success(Net("0.0.0.0", Port(8080, 9090))))
      config.unsafeGet[App]("app").shouldEqual(Success(App(Net("0.0.0.0", Port(8080, 9090)))))
    }

    "fetch values from included config" in new mocks {
      config.unsafeGet[Int]("akka.kafka.producer.parallelism").shouldEqual(Success(10))
      config.unsafeGet[String]("akka.remote.netty.tcp.hostname") shouldEqual (Success("0.0.0.0"))
    }

    "config should be able to reload primitive value on change" in new mocks {
      (for {
        reloadable <- config.reloadable[Int]("changeable.parameter.value")
        first <- reloadable.get
        _ <- IO.delay(
          File(path.getParent.resolve("changeable.conf")).overwrite(changeable(2))
        )
        _ <- IO.sleep(300 millis)
        second <- reloadable.get
      } yield {
        first shouldBe 1
        second shouldBe 2
      }).guarantee(IO.delay(
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(1))
      )).unsafeRunSync()
    }

    "config should be able to reload case class on change" in new mocks {
      (for {
        reloadable <- config.reloadable[Parameter]("changeable.parameter")
        first <- reloadable.get
        _ <- IO.delay(
          File(path.getParent.resolve("changeable.conf")).overwrite(changeable(2))
        )
        _ <- IO.sleep(300 millis)
        second <- reloadable.get
      } yield {
        first shouldBe Parameter(1)
        second shouldBe Parameter(2)
      }).guarantee(IO.delay(
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(1))
      )).unsafeRunSync()
    }
  }
}

object TypesafeConfigStorageTest {
  case class App(net: Net)
  case class Net(host: String, port: Port)
  case class Port(public: Int, `private`: Int)

  def changeable(v: Int): String =
    s"""changeable {
       |  parameter {
       |    value: $v
       |  }
       |}
    """.stripMargin

  case class Parameter(value: Int)
}
