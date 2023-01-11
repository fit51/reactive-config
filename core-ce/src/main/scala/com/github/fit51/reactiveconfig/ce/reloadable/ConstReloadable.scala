package com.github.fit51.reactiveconfig.ce.reloadable

import cats.{~>, Monad}
import cats.effect.{Resource => CatsResource}
import cats.kernel.Eq
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable._
import com.github.fit51.reactiveconfig.reloadable._
import com.github.fit51.reactiveconfig.typeclasses._

private class ConstReloadable[F[_]: Monad, A](a: A) extends Reloadable[F, A] { self =>

  override def unsafeGet: A =
    a

  override val get: F[A] =
    Monad[F].pure(a)

  override def forEachF(f: A => F[Unit]): F[Unit] =
    f(a)

  override def distinctByKey[K: Eq](makeKey: A => K): CatsResource[F, Reloadable[F, A]] =
    CatsResource.pure(new ConstReloadable(a))

  override def map[B](f: A => B, reloadBehaviour: ReloadBehaviour[F, A, B]): CatsResource[F, Reloadable[F, B]] =
    CatsResource.pure(new ConstReloadable(f(a)))

  override def mapF[B](f: A => F[B], reloadBehaviour: ReloadBehaviour[F, A, B]): CatsResource[F, Reloadable[F, B]] =
    CatsResource.eval(f(a).map(new ConstReloadable(_)))

  override def mapResource[B](f: A => CatsResource[F, B]): CatsResource[F, Reloadable[F, B]] =
    f(a).map(new ConstReloadable(_))

  override def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => C): CatsResource[F, Reloadable[F, C]] =
    CatsResource.eval(other.get.map(f(a, _)).map(new ConstReloadable(_)))

  override def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): CatsResource[F, Reloadable[F, C]] =
    CatsResource.eval(other.get.flatMap(f(a, _)).map(new ConstReloadable(_)))

  override def makeVolatile[G[_]](nat: F ~> G): CatsResource[F, Volatile[G, A]] = {
    val ga = nat(get)
    CatsResource.pure(new Volatile[G, A] {
      override val unsafeGet: A = self.unsafeGet

      override val get: G[A] = ga
    })
  }

  override protected[reactiveconfig] def subscribe[G[_]](
      subscriber: Subscriber[F, A]
  )(implicit effect: Effect[G], resource: Resource[CatsResource, G]): CatsResource[G, Unit] =
    CatsResource.pure[G, Unit](())
}
