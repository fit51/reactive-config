package com.github.fit51.reactiveconfig.zio.instances

import com.github.fit51.reactiveconfig.typeclasses._
import zio._

trait EffectInstances {

  implicit def effectForZio[R, E]: Effect[ZIO[R, E, *]] =
    new Effect[ZIO[R, E, *]] {
      override def pure[A](a: A): ZIO[R, E, A] =
        ZIO.succeed(a)

      override def sync[A](thunk: () => A): ZIO[R, E, A] =
        ZIO.succeed(thunk())

      override def async[A](cb: (A => Unit) => ZIO[R, E, Unit]): ZIO[R, E, A] =
        ZIO.asyncZIO { innerCb =>
          cb(a => innerCb(ZIO.succeed(a)))
        }

      override def map[A, B](fa: ZIO[R, E, A])(f: A => B): ZIO[R, E, B] =
        fa.map(f)

      override def flatMap[A, B](fa: ZIO[R, E, A])(f: A => ZIO[R, E, B]): ZIO[R, E, B] =
        fa.flatMap(f)

      override def fireAndForget[A](fa: ZIO[R, E, A]): ZIO[R, E, Unit] =
        fa.forkDaemon.unit

      override def parallelTraverse(fas: List[ZIO[R, E, Unit]]): ZIO[R, E, Unit] =
        ZIO.foreachParDiscard(fas)(identity)

      override def info(message: String): ZIO[R, E, Unit] =
        ZIO.logInfo(message)

      override def warn(message: String): ZIO[R, E, Unit] =
        ZIO.logWarning(message)

      override def warn(message: String, e: Throwable): ZIO[R, E, Unit] =
        ZIO.logWarningCause(message, Cause.fail(e))
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
        Allocate(ZIO.succeed((a, ZIO.unit)))

      override def liftF[A](fa: ZIO[R, E, A]): ResourceLike[ZIO[R, E, *], A] =
        Allocate(fa.map(a => (a, ZIO.unit)))

      override def make[A](acquire: ZIO[R, E, A])(release: A => ZIO[R, E, Unit]): ResourceLike[ZIO[R, E, *], A] =
        Allocate(acquire.map(a => (a, release(a))))

      override def map[A, B](ra: ResourceLike[ZIO[R, E, *], A])(f: A => B): ResourceLike[ZIO[R, E, *], B] =
        FlatMap(ra, (a: A) => Allocate(ZIO.succeed((f(a), ZIO.unit))))

      override def flatMap[A, B](
          ra: ResourceLike[ZIO[R, E, *], A]
      )(f: A => ResourceLike[ZIO[R, E, *], B]): ResourceLike[ZIO[R, E, *], B] =
        FlatMap(ra, f)
    }
}
