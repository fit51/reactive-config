package com.github.fit51.reactiveconfig.zio.config

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.config.{ConfigState, Listener}
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

class AbstractReactiveConfig[D](stateRef: Ref.Synchronized[ConfigState[UIO, D]]) extends ReactiveConfig[D] {

  override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): IO[ReactiveConfigException, T] =
    stateRef.get.flatMap { state =>
      ZIO.fromEither(state.extractValue(key))
    }

  override def hasKey(key: String): UIO[Boolean] =
    stateRef.get.map(_.values.contains(key))

  override def reloadable[T](
      key: String
  )(implicit decoder: ConfigDecoder[T, D]): ZIO[Scope, ReactiveConfigException, Reloadable[T]] =
    ZIO.scopeWith { scope =>
      stateRef.modifyZIO { state =>
        for {
          initial               <- ZIO.fromEither(state.extractValue(key))
          (reloadable, updater) <- Reloadable.root(initial)
          listener = Listener(decoder, updater)
          _ <- scope.addFinalizer(ZIO.succeed(state.removeListener(key, listener)))
        } yield (reloadable, state.addListener(key, listener))
      }.uninterruptible
    }
}
