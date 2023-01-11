package com.github.fit51.reactiveconfig.ce.typesafe

import java.nio.file.Path

import cats.Parallel
import cats.effect.{Blocker, Concurrent, ContextShift, Resource}
import cats.effect.concurrent.{Ref, Semaphore}
import cats.effect.syntax.concurrent._
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.ce.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable._
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.typesafe.TypesafeUtils
import fs2.io.Watcher

trait TypesafeReactiveConfig[F[_], D] extends ReactiveConfig[F, D]

object TypesafeReactiveConfig {

  def apply[F[_]: Concurrent: ContextShift: Parallel, D](blocker: Blocker, path: Path)(implicit
      encoder: ConfigParser[D]
  ) =
    for {
      watcher   <- Watcher.default(blocker)
      _         <- Resource.make(watcher.watch(path.getParent(), List(Watcher.EventType.Modified)))(identity)
      semaphore <- Resource.eval(Semaphore(1))
      stateRef  <- Resource.eval(parseConfig(path, -1).map(ConfigState[F, D](_, Map.empty)) >>= Ref[F].of)
      _ <- Resource.eval(
        watcher
          .events()
          .chunks
          .zipWithIndex
          .evalMap { case (_, idx) => parseConfig(path, idx).attempt }
          .evalMap {
            case Right(newMap) =>
              semaphore.withPermit(for {
                state <- stateRef.get
                _     <- state.fireUpdates(newMap)
                _     <- stateRef.set(state.copy(values = newMap))
              } yield ())
            case Left(e) =>
              Effect[F].warn("Unable to parse config", e)
          }
          .compile
          .drain
          .start
      )
    } yield new AbstractReactiveConfig[F, D](stateRef, semaphore)

  private def parseConfig[F[_], D: ConfigParser](path: Path, index: Long)(implicit
      eff: Effect[F]
  ): F[Map[String, Value[D]]] = {
    import com.github.fit51.reactiveconfig.typeclasses.Effect._
    eff.sync(() => TypesafeUtils.parseConfig(path)).flatMap(TypesafeUtils.parseValuesInMap[F, D](_, index))
  }
}
