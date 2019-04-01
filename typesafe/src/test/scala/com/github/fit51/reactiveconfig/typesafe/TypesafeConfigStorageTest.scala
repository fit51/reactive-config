package com.github.fit51.reactiveconfig.typesafe

import java.nio.file.Paths
import better.files.File
import io.circe.generic.auto._
import cats.effect.IO
import com.github.fit51.reactiveconfig.config.ConfigImpl
import io.circe.Json
import org.scalatest.{Matchers, WordSpecLike}
import org.scalatest.mockito.MockitoSugar
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

class TypesafeConfigStorageTest extends WordSpecLike with Matchers with MockitoSugar {
  import TypesafeConfigStorageTest._
  import com.github.fit51.reactiveconfig.parser.CirceConfigDecoder.decoder
  import com.github.fit51.reactiveconfig.parser.CirceConfigParser.parser

  trait mocks {
    val path    = Paths.get("typesafe/src/test/resources/application.conf")
    val storage = TypesafeConfigStorage[IO, Json](path)
    val config  = Await.result(ConfigImpl[IO, Json](storage).unsafeToFuture, 1 second)
  }

  "TypesafeConfig" should {

    "properly fetch values with different paths" in new mocks {
      config.get[Int]("app.net.port.public").shouldEqual(Some(8080))
      config.get[String]("app.net.host").shouldEqual(Some("0.0.0.0"))
      config.get[Port]("app.net.port").shouldEqual(Some(Port(8080, 9090)))
      config.get[Net]("app.net").shouldEqual(Some(Net("0.0.0.0", Port(8080, 9090))))
      config.get[App]("app").shouldEqual(Some(App(Net("0.0.0.0", Port(8080, 9090)))))
    }

    "fetch values from included config" in new mocks {
      config.get[Int]("akka.kafka.producer.parallelism").shouldEqual(Some(10))
      config.get[String]("akka.remote.netty.tcp.hostname") shouldEqual (Some("0.0.0.0"))
    }

    "config should be able to reload primitive value on change" in new mocks {
      try {
        val reloadable = config.reloadable[Int]("changeable.parameter.value")

        reloadable.get.shouldEqual(1)
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(2))
        Thread.sleep(300)
        reloadable.get.shouldEqual(2)
      } finally {
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(1))
      }
    }

    "config should be able to reload case class on change" in new mocks {
      try {
        val reloadable = config.reloadable[Parameter]("changeable.parameter")

        reloadable.get.value.shouldEqual(1)
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(2))
        Thread.sleep(300)
        reloadable.get.value.shouldEqual(2)
      } finally {
        File(path.getParent.resolve("changeable.conf")).overwrite(changeable(1))
      }
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
