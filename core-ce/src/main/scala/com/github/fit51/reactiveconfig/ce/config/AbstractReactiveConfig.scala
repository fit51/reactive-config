package com.github.fit51.reactiveconfig.ce.config

import cats.~>
import cats.Parallel
import cats.effect.Concurrent
import cats.effect.Resource
import cats.effect.concurrent.Ref
import cats.effect.concurrent.Semaphore
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.config.Listener
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Volatile

class AbstractReactiveConfig[F[_], D](
    stateRef: Ref[F, ConfigState[F, D]],
    semaphore: Semaphore[F]
)(implicit
    F: Concurrent[F],
    P: Parallel[F]
) extends ReactiveConfig[F, D] {

  override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): F[T] =
    stateRef.get.flatMap(s => F.fromEither(s.extractValue(key)))

  override def hasKey(key: String): F[Boolean] =
    stateRef.get.map(_.values.contains(key))

  override def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, D]): Resource[F, Reloadable[F, T]] =
    Resource(semaphore.withPermit(F.uncancelable(for {
      state                              <- stateRef.get
      initial                            <- F.fromEither(state.extractValue(key))
      ((reloadable, updater), finalizer) <- Reloadable.root[F](initial).allocated
      listener = Listener[F, D, T](decoder, updater)
      _ <- stateRef.update(_.addListener(key, listener))
    } yield (reloadable, finalizer >> stateRef.update(_.removeListener(key, listener))))))

  override def volatile[G[_], T](
      key: String
  )(implicit decoder: ConfigDecoder[T, D], nat: F ~> G): Resource[F, Volatile[G, T]] =
    reloadable[T](key).flatMap(_.makeVolatile(nat))
}
