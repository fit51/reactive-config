package com.github.fit51.reactiveconfig.typeclasses

trait HandleTo[F[_], G[_], E] {

  def handleErrorWith[A](fa: F[A])(f: E => G[A]): G[A]

  def mapK[A](ga: G[A]): F[A]
}

object HandleTo {

  implicit def identityHandleTo[F[_], E]: HandleTo[F, F, E] =
    new HandleTo[F, F, E] {

      override def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A] = fa

      override def mapK[A](ga: F[A]): F[A] = ga
    }

  implicit class HandleToOps[F[_], A](private val fa: F[A]) extends AnyVal {

    def handleErrorWith[G[_], E](f: E => G[A])(implicit
        handleTo: HandleTo[F, G, E]
    ): G[A] =
      handleTo.handleErrorWith(fa)(f)

    def mapK[G[_], E](implicit handleTo: HandleTo[G, F, E]): G[A] =
      handleTo.mapK[A](fa)
  }
}
