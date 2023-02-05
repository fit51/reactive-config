package com.github.fit51.reactiveconfig.zio.reloadable

import java.util.concurrent.atomic.AtomicReference

import cats.kernel.Eq
import com.github.fit51.reactiveconfig.reloadable.{AtomicUtils, ReloadBehaviour, Subscriber}
import com.github.fit51.reactiveconfig.typeclasses.{Effect, Resource, ResourceLike}
import com.github.fit51.reactiveconfig.typeclasses.Resource._
import zio.{Scope, UIO, URIO, ZIO}

private class ConstReloadable[T](t: T) extends Reloadable[T] {

  private val subscribers: AtomicReference[List[Subscriber[UIO, T]]] = new AtomicReference(Nil)

  override def unsafeGet: T =
    t

  override val get: UIO[T] =
    ZIO.succeed(t)

  override def map[B](
      f: T => B,
      reloadBehaviour: ReloadBehaviour[UIO, T, B]
  ): UIO[Reloadable[B]] =
    ZIO.succeed(new ConstReloadable[B](f(t)))

  override def mapF[R, E, B](
      f: T => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], T, B]
  ): ZIO[R, E, Reloadable[B]] =
    f(t).map(new ConstReloadable(_))

  override def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (T, B), C]
  )(f: (T, B) => C): UIO[Reloadable[C]] =
    other.get.map(f(t, _)).map(new ConstReloadable(_))

  override def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (T, B), C]
  )(f: (T, B) => ZIO[R, E, C]): ZIO[R, E, Reloadable[C]] =
    other.get.flatMap(f(t, _)).map(new ConstReloadable(_))

  override def forEachF[R](f: T => URIO[R, Unit]): URIO[R, Nothing] =
    f(t) *> ZIO.never

  override def distinctByKey[K: Eq](makeKey: T => K): UIO[Reloadable[T]] =
    ZIO.succeed(new ConstReloadable(t))

  override def mapScoped[R, E, B](f: T => ZIO[R with Scope, E, B]): ZIO[R with Scope, E, Reloadable[B]] =
    f(t).map(new ConstReloadable(_))

  override protected[reactiveconfig] def subscribe[G[_]](
      subscriber: Subscriber[UIO, T]
  )(implicit effect: Effect[G], resource: Resource[ResourceLike, G]): ResourceLike[G, Unit] =
    resource.make(effect.sync { () =>
      AtomicUtils.update(subscribers)(subscriber :: _)
    }) { _ =>
      effect.sync(() => AtomicUtils.update(subscribers)(_.filter(_ != subscriber)))
    } as (())
}
