package com.github.fit51.reactiveconfig.examples

import java.nio.file.Paths

import monix.eval.Task
import cats.effect.Blocker
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.generic.auto._
import com.github.fit51.reactiveconfig.config._
import com.github.fit51.reactiveconfig.parser._
import com.github.fit51.reactiveconfig.typesafe.TypesafeConfigStorage
import scala.concurrent.Await
import scala.concurrent.duration._
import monix.reactive.Observable

case class SimpleLib(foo: String, whatever: String)

object TypesafeConfigApplication extends App with LazyLogging {
  implicit val scheduler = monix.execution.Scheduler.global
  val blocker            = Blocker.liftExecutionContext(monix.execution.Scheduler.io())

  import CirceConfigParser._
  import CirceConfigDecoder._

  def reloadables(config: ReactiveConfig[Task, Json]) =
    for {
      someR      <- config.reloadable[String]("complex-app.something")
      libConfigR <- config.reloadable[SimpleLib]("complex-app.simple-lib-context.simple-lib")
      combinedR  <- libConfigR.combine(someR)((l, s) => l.copy(foo = s))
    } yield (someR, libConfigR, combinedR)

  def useReloadables(
      someR: Reloadable[Task, String],
      libConfigR: Reloadable[Task, SimpleLib],
      combinedR: Reloadable[Task, SimpleLib]
  ): Task[Unit] =
    Observable
      .interval(1 second)
      .mapEval(_ =>
        for {
          some      <- someR.get
          libConfig <- libConfigR.get
          combined  <- combinedR.get
        } yield {
          println(s"some: $some")
          println(s"lib: $libConfig")
          println(s"all together: $combined")
        }
      )
      .lastL

  val app = for {
    storage <- Task.eval(TypesafeConfigStorage[Task, Json](Paths.get("examples/config/application.conf"), blocker))
    _       <- Task.eval(println("Now change examples/config/application.conf file and see what happens!"))
    fiber <- ReactiveConfig[Task, Json](storage)
      .flatMap(reloadables).use({ case (someR, libConfigR, combinedR) =>
        useReloadables(someR, libConfigR, combinedR)
      }).start
    _ <- Task.sleep(1.minute)
    _ <- fiber.cancel
  } yield ()

  Await.result(
    app.doOnFinish {
      case Some(e) => Task.eval(println("Error", e))
      case None    => Task.eval(println("Finished"))
    }.runToFuture,
    Duration.Inf
  )
}
