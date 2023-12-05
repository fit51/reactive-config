package com.github.fit51.reactiveconfig.examples

import java.nio.file.Paths

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.ce.typesafe.TypesafeReactiveConfig
import com.github.fit51.reactiveconfig.circe._
import com.typesafe.scalalogging.LazyLogging
import io.circe._
import io.circe.generic.auto._

import scala.concurrent.duration._

case class SimpleLib(foo: String, whatever: String)

object TypesafeConfigApplication extends IOApp with LazyLogging {

  def reloadables(config: ReactiveConfig[IO, Json]) =
    for {
      someR      <- config.reloadable[String]("complex-app.something")
      libConfigR <- config.reloadable[SimpleLib]("complex-app.simple-lib-context.simple-lib")
      combinedR  <- libConfigR.combine(someR)((l, s) => l.copy(foo = s))
    } yield (someR, libConfigR, combinedR)

  def useReloadables(
      someR: Reloadable[IO, String],
      libConfigR: Reloadable[IO, SimpleLib],
      combinedR: Reloadable[IO, SimpleLib]
  ): IO[Unit] =
    (for {
      some      <- someR.get
      libConfig <- libConfigR.get
      combined  <- combinedR.get
    } yield {
      println(s"some: $some")
      println(s"lib: $libConfig")
      println(s"all together: $combined")
    }) *> IO.sleep(1.second).flatMap(_ => useReloadables(someR, libConfigR, combinedR))

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      config <- TypesafeReactiveConfig[IO, Json](Paths.get("examples/config/application.conf"))
      _ <- Resource.eval(
        IO.delay(logger.info("Now change examples/config/application.conf file and see what happens!"))
      )
      reloadables <- reloadables(config)
    } yield reloadables).use { case (someR, libConfigR, combinedR) =>
      (useReloadables(someR, libConfigR, combinedR).start <* IO.sleep(1.minute)).flatMap(_.cancel).as(ExitCode.Success)
    }
}
