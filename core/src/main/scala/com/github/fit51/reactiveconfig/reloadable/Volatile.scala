package com.github.fit51.reactiveconfig.reloadable

import cats.~>
import cats.Applicative

/** A Volatile is simplified Reloadable.
  *
  * Reloadable provides a lot of possibilities for mapping and combining Reloadables but it has complex interface where
  * F[_] is used in covariant as well as in contravariant positions. It makes impossible to change effect F[_] for
  * Reloadable once it is created. This may be necessary if several different effects are used inside one application.
  *
  * Volatile has only one method `get` where F[_] is used in covariant position. Such interface allows to implement
  * method `mapK` and change effect using natural transformation.
  *
  * Note: Method `mapK` is also added in trait Reloadable but it requires instances of `TaskLike` and `TaskLift`. Method
  * `Volatile.mapK` takes only natural transformation from initial effect to new one.
  */
trait Volatile[F[_], A] { self =>

  def unsafeGet: A

  def get: F[A]

  def widen[B >: A]: Volatile[F, B] =
    this.asInstanceOf[Volatile[F, B]]

  def mapK[G[_]](natTranform: F ~> G): Volatile[G, A] =
    new Volatile[G, A] {
      override def unsafeGet: A =
        self.unsafeGet
      override def get: G[A] =
        natTranform.apply(self.get)
    }
}

class VolatileImpl[F[_], A](initA: A, initFa: F[A]) extends Volatile[F, A] {

  @volatile var value: A     = initA
  @volatile var valueF: F[A] = initFa

  override def unsafeGet: A =
    value

  override def get: F[A] =
    valueF

  private[reloadable] def modifyCurrentValue(a: A, fa: F[A]): Unit = {
    value = a
    valueF = fa
  }
}

object Volatile {

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[A](a: A)(implicit applicative: Applicative[F]): Volatile[F, A] =
      new Volatile[F, A] {
        override def unsafeGet: A = a
        override def get: F[A]    = applicative.pure(a)
      }
  }
}
