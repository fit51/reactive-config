package com.github.fit51.reactiveconfig.zio.reloadable

import cats.kernel.Eq
import com.github.fit51.reactiveconfig.reloadable.{ReloadBehaviour, Subscriber}
import com.github.fit51.reactiveconfig.typeclasses.{Allocate, Effect, Resource, ResourceLike}
import zio._

private class ConstReloadable[T](t: T) extends Reloadable[T] {

  override def unsafeGet: T =
    t

  override val get: UIO[T] =
    UIO.succeed(t)

  override def map[B](
      f: T => B,
      reloadBehaviour: ReloadBehaviour[UIO, T, B]
  ): UManaged[Reloadable[B]] =
    Managed.succeed(new ConstReloadable[B](f(t)))

  override def mapF[R, E, B](
      f: T => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], T, B]
  ): ZManaged[R, E, Reloadable[B]] =
    f(t).map(new ConstReloadable(_)).toManaged_

  override def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (T, B), C]
  )(f: (T, B) => C): UManaged[Reloadable[C]] =
    other.get.map(f(t, _)).map(new ConstReloadable(_)).toManaged_

  override def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (T, B), C]
  )(f: (T, B) => ZIO[R, E, C]): ZManaged[R, E, Reloadable[C]] =
    other.get.flatMap(f(t, _)).map(new ConstReloadable(_)).toManaged_

  override def forEachF[R](f: T => URIO[R, Unit]): URIO[R, Nothing] =
    f(t) *> UIO.never

  override def distinctByKey[K: Eq](makeKey: T => K): UManaged[Reloadable[T]] =
    Managed.succeed(new ConstReloadable(t))

  override def mapManaged[R, E, B](f: T => ZManaged[R, E, B]): ZManaged[R, E, Reloadable[B]] =
    f(t).map(new ConstReloadable(_))

  override protected[reactiveconfig] def subscribe[G[_]](
      subscriber: Subscriber[UIO, T]
  )(implicit effect: Effect[G], resource: Resource[ResourceLike, G]): ResourceLike[G, Unit] =
    Allocate[G, Unit](effect.pure(((), effect.unit)))
}
