package com.github.fit51.reactiveconfig.zio.instances

import com.github.fit51.reactiveconfig.typeclasses._
import com.typesafe.scalalogging.StrictLogging
import zio._

trait EffectInstances extends StrictLogging {

  implicit def effectForZio[R, E]: Effect[ZIO[R, E, *]] =
    new Effect[ZIO[R, E, *]] {
      override def pure[A](a: A): ZIO[R, E, A] =
        UIO.succeed(a)

      override def sync[A](thunk: () => A): ZIO[R, E, A] =
        ZIO.effectTotal(thunk())

      override def async[A](cb: (A => Unit) => ZIO[R, E, Unit]): ZIO[R, E, A] =
        ZIO.effectAsyncM { innerCb =>
          cb(a => innerCb(UIO.succeed(a)))
        }

      override def map[A, B](fa: ZIO[R, E, A])(f: A => B): ZIO[R, E, B] =
        fa.map(f)

      override def flatMap[A, B](fa: ZIO[R, E, A])(f: A => ZIO[R, E, B]): ZIO[R, E, B] =
        fa.flatMap(f)

      override def fireAndForget[A](fa: ZIO[R, E, A]): ZIO[R, E, Unit] =
        fa.forkDaemon.unit

      override def parallelTraverse(fas: List[ZIO[R, E, Unit]]): ZIO[R, E, Unit] =
        ZIO.foreachPar_(fas)(identity)

      override def info(message: String): ZIO[R, E, Unit] =
        UIO.effectTotal(logger.info(message))

      override def warn(message: String): ZIO[R, E, Unit] =
        UIO.effectTotal(logger.warn(message))

      override def warn(message: String, e: Throwable): ZIO[R, E, Unit] =
        UIO.effectTotal(logger.warn(message, e))
    }

  implicit def handleTo[E]: HandleTo[IO[E, *], UIO, E] =
    new HandleTo[IO[E, *], UIO, E] {

      override def handleErrorWith[A](fa: IO[E, A])(f: E => UIO[A]): UIO[A] =
        fa.catchAll(f)

      override def mapK[A](ga: UIO[A]): ZIO[Any, E, A] =
        ga
    }

  implicit def zioResource[R, E]: Resource[ResourceLike, ZIO[R, E, *]] =
    new Resource[ResourceLike, ZIO[R, E, *]] {
      override def pure[A](a: A): ResourceLike[ZIO[R, E, *], A] =
        Allocate(UIO.succeed((a, UIO.unit)))

      override def liftF[A](fa: ZIO[R, E, A]): ResourceLike[ZIO[R, E, *], A] =
        Allocate(fa.map(a => (a, UIO.unit)))

      override def make[A](acquire: ZIO[R, E, A])(release: A => ZIO[R, E, Unit]): ResourceLike[ZIO[R, E, *], A] =
        Allocate(acquire.map(a => (a, release(a))))

      override def map[A, B](ra: ResourceLike[ZIO[R, E, *], A])(f: A => B): ResourceLike[ZIO[R, E, *], B] =
        FlatMap(ra, (a: A) => Allocate(UIO.succeed((f(a), UIO.unit))))

      override def flatMap[A, B](
          ra: ResourceLike[ZIO[R, E, *], A]
      )(f: A => ResourceLike[ZIO[R, E, *], B]): ResourceLike[ZIO[R, E, *], B] =
        FlatMap(ra, f)
    }
}
