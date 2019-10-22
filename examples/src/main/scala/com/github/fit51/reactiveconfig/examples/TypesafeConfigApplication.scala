package com.github.fit51.reactiveconfig.examples

import java.nio.file.Paths

import monix.eval.Task
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.generic.auto._
import monix.execution.Cancelable
import com.github.fit51.reactiveconfig.config._
import com.github.fit51.reactiveconfig.parser._
import com.github.fit51.reactiveconfig.typesafe.TypesafeConfigStorage
import scala.concurrent.Await
import scala.concurrent.duration._

case class SimpleLib(foo: String, whatever: String)

object TypesafeConfigApplication extends App with LazyLogging {
  implicit val scheduler = monix.execution.Scheduler.global

  import CirceConfigParser._
  import CirceConfigDecoder._

  def useConfig(config: ReactiveConfig[Task, Json]): Task[Cancelable] = {
    val some      = config.reloadable[String]("complex-app.something")
    val libConfig = config.reloadable[SimpleLib]("complex-app.simple-lib-context.simple-lib")
    val combined  = libConfig.combine(some)((l, s) => l.copy(foo = s))

    Task.eval {
      println("Entered")
      scheduler.scheduleWithFixedDelay(0 seconds, 1 second) {
        println(s"some: ${some.get}")
        println(s"lib: ${libConfig.get}")
        println(s"all together: ${combined.get}")
      }
    }
  }

  val app = for {
    storage     <- Task.eval(TypesafeConfigStorage[Task, Json](Paths.get("examples/config/application.conf")))
    config      <- ReactiveConfigImpl[Task, Json](storage)
    _           <- Task.eval(println("Now change examples/config/application.conf file and see what happens!"))
    cancellable <- useConfig(config)
    _           <- Task.sleep(1.minute)
    _           <- Task.eval(cancellable.cancel())
  } yield ()

  Await.result(
    app.doOnFinish {
      case Some(e) => Task.eval(println("Error", e))
      case None    => Task.eval(println("Finished"))
    }.runToFuture,
    Duration.Inf
  )
}
