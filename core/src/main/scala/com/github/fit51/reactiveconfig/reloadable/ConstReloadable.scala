package com.github.fit51.reactiveconfig.reloadable

import cats.effect.{Bracket, Resource}
import cats.kernel.Eq
import cats.syntax.applicative._
import cats.Applicative
import monix.reactive.Observable

private class ConstReloadable[F[_]: Applicative, T](t: T) extends Reloadable[F, T] {

  override def unsafeGet: T =
    t

  override def get: F[T] =
    t.pure[F]

  override def forEachF(f: T => F[Unit]): F[Unit] =
    f(t)

  override def distinctByKey[K: Eq](makeKey: T => K): Resource[F, Reloadable[F, T]] =
    Resource.pure[F, Reloadable[F, T]](this)

  override def map[B](f: T => B, reloadBehaviour: ReloadBehaviour[F, T, B]): Resource[F, Reloadable[F, B]] =
    Resource.pure[F, Reloadable[F, B]](new ConstReloadable(f(t)))

  override def mapF[B](f: T => F[B], reloadBehaviour: ReloadBehaviour[F, T, B]): Resource[F, Reloadable[F, B]] =
    for {
      value <- Resource.liftF(f(t))
    } yield new ConstReloadable(value)

  override def mapResource[B](
      f: T => Resource[F, B]
  )(implicit bracket: Bracket[F, Throwable]): Resource[F, Reloadable[F, B]] =
    mapF(
      a => f(a).allocated,
      Stop((pair: (B, F[Unit])) => pair._2)
    ).flatMap(_.map(_._1))

  override def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (T, B), C]
  )(f: (T, B) => C): Resource[F, Reloadable[F, C]] =
    Resource.pure[F, Reloadable[F, C]](new ConstReloadable(f(t, other.unsafeGet)))

  override def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (T, B), C]
  )(f: (T, B) => F[C]): Resource[F, Reloadable[F, C]] =
    for {
      value <- Resource.liftF(f(t, other.unsafeGet))
    } yield new ConstReloadable(value)

  override def observable: Observable[T] =
    Observable.never
}
