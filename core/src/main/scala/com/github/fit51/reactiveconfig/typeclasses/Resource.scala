package com.github.fit51.reactiveconfig.typeclasses

trait Resource[R[_[_], _], F[_]] {

  def pure[A](a: A): R[F, A]

  def liftF[A](fa: F[A]): R[F, A]

  def make[A](acquire: F[A])(release: A => F[Unit]): R[F, A]

  def map[A, B](ra: R[F, A])(f: A => B): R[F, B]

  def flatMap[A, B](ra: R[F, A])(f: A => R[F, B]): R[F, B]
}

sealed trait ResourceLike[F[_], A]

final case class Allocate[F[_], A](
    resource: F[(A, F[Unit])]
) extends ResourceLike[F, A]

final case class FlatMap[F[_], A, B](
    resource: ResourceLike[F, A],
    func: A => ResourceLike[F, B]
) extends ResourceLike[F, B]

object Resource {

  implicit class ResourceOps[R[_[_], _], F[_], A](private val ra: R[F, A]) extends AnyVal {

    def as[B](b: B)(implicit res: Resource[R, F]): R[F, B] =
      res.map(ra)(_ => b)

    def map[B](func: A => B)(implicit res: Resource[R, F]): R[F, B] =
      res.map(ra)(func)

    def flatMap[B](func: A => R[F, B])(implicit res: Resource[R, F]): R[F, B] =
      res.flatMap(ra)(func)

    def *>[B](next: R[F, B])(implicit res: Resource[R, F]): R[F, B] =
      res.flatMap(ra)(_ => next)
  }
}
