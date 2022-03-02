package com.github.fit51.reactiveconfig.ce.instances

import cats.{Applicative, Parallel}
import cats.MonadThrow
import cats.effect.{BracketThrow, Concurrent, Resource => CatsResource}
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.typeclasses.HandleTo
import com.github.fit51.reactiveconfig.typeclasses.Resource
import com.typesafe.scalalogging.StrictLogging

trait CatsEffectInstances extends StrictLogging {

  implicit def effectForConcurrent[F[_]](implicit F: Concurrent[F], P: Parallel[F]): Effect[F] =
    new Effect[F] {
      import cats.instances.list._
      import cats.syntax.parallel._

      override def pure[A](a: A): F[A] =
        F.pure(a)

      override def sync[A](thunk: () => A): F[A] =
        F.delay(thunk())

      override def async[A](cb: (A => Unit) => F[Unit]): F[A] =
        F.asyncF[A] { innerCb =>
          cb(a => innerCb(Right(a)))
        }

      override def map[A, B](fa: F[A])(f: A => B): F[B] =
        F.map(fa)(f)

      override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] =
        F.flatMap(fa)(f)

      override def fireAndForget[A](fa: F[A]): F[Unit] =
        F.start(fa).void

      override def parallelTraverse(fas: List[F[Unit]]): F[Unit] =
        fas.parSequence_

      override def info(message: String): F[Unit] =
        F.delay(logger.info(message))

      override def warn(message: String): F[Unit] =
        F.delay(logger.warn(message))

      override def warn(message: String, e: Throwable): F[Unit] =
        F.delay(logger.warn(message, e))
    }

  implicit def handleTo[F[_]: MonadThrow]: HandleTo[F, F, Throwable] =
    new HandleTo[F, F, Throwable] {
      override def handleErrorWith[A](fa: F[A])(f: Throwable => F[A]): F[A] =
        MonadThrow[F].handleErrorWith(fa)(f)
      override def mapK[A](ga: F[A]): F[A] =
        ga
    }

  implicit def catsResource[F[_]: BracketThrow]: Resource[CatsResource, F] =
    new Resource[CatsResource, F] {

      override def pure[A](a: A): CatsResource[F, A] =
        CatsResource.pure(a)

      override def liftF[A](fa: F[A]): CatsResource[F, A] =
        CatsResource.eval(fa)

      override def make[A](acquire: F[A])(release: A => F[Unit]): CatsResource[F, A] =
        CatsResource.make(acquire)(release)

      override def map[A, B](ra: CatsResource[F, A])(f: A => B): CatsResource[F, B] =
        ra.map(f)

      override def flatMap[A, B](ra: CatsResource[F, A])(f: A => CatsResource[F, B]): CatsResource[F, B] =
        ra.flatMap(f)
    }

  implicit def applicativeForEffect[F[_]](implicit effect: Effect[F]): Applicative[F] =
    new Applicative[F] {

      override def ap[A, B](ff: F[A => B])(fa: F[A]): F[B] =
        effect.flatMap(fa)(a => effect.map(ff)(func => func(a)))

      override def pure[A](x: A): F[A] =
        effect.pure(x)
    }
}
