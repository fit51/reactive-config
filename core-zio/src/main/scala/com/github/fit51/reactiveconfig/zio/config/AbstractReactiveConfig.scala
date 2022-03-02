package com.github.fit51.reactiveconfig.zio.config

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.config.{ConfigState, Listener}
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

class AbstractReactiveConfig[D](stateRef: RefM[ConfigState[UIO, D]]) extends ReactiveConfig[D] {

  override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): IO[ReactiveConfigException, T] =
    stateRef.get.flatMap { state =>
      ZIO.fromEither(state.extractValue(key))
    }

  override def hasKey(key: String): UIO[Boolean] =
    stateRef.get.map(_.values.contains(key))

  override def reloadable[T](
      key: String
  )(implicit decoder: ConfigDecoder[T, D]): Managed[ReactiveConfigException, Reloadable[T]] =
    ZManaged((for {
      env <- ZIO.environment[(Any, ZManaged.ReleaseMap)]
      (reloadable, newFinalizer) <- stateRef.modify { state =>
        for {
          initial                            <- ZIO.fromEither(state.extractValue(key))
          (finalizer, (reloadable, updater)) <- Reloadable.root(initial).zio.provide(env)
          listener = Listener(decoder, updater)
          newFinalizer <- env._2.add { exit =>
            finalizer(exit) *> stateRef.update(state => UIO.succeed(state.removeListener(key, listener)))
          }
        } yield ((reloadable, newFinalizer), state.addListener(key, listener))
      }
    } yield (newFinalizer, reloadable)).uninterruptible)
}
