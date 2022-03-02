package com.github.fit51.reactiveconfig.zio.reloadable

import cats.kernel.Eq
import com.github.fit51.reactiveconfig.reloadable._
import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.zio.instances._
import zio._

trait Reloadable[A] extends RawReloadable[UIO, ResourceLike[*[_], *], A] { self =>

  def unsafeGet: A

  val get: UIO[A]

  def forEachF[R](f: A => URIO[R, Unit]): URIO[R, Nothing]

  def distinctByKey[K: Eq](makeKey: A => K): UManaged[Reloadable[A]]

  def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[UIO, A, B] = ReloadBehaviour.simpleBehaviour[UIO, A, B]
  ): UManaged[Reloadable[B]]

  def mapF[R, E, B](
      f: A => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], A, B] = ReloadBehaviour.simpleBehaviour[ZIO[R, E, *], A, B]
  ): ZManaged[R, E, Reloadable[B]]

  def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (A, B), C] = ReloadBehaviour.simpleBehaviour[UIO, (A, B), C]
  )(f: (A, B) => C): UManaged[Reloadable[C]]

  def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (A, B), C] =
        ReloadBehaviour.simpleBehaviour[ZIO[R, E, *], (A, B), C]
  )(f: (A, B) => ZIO[R, E, C]): ZManaged[R, E, Reloadable[C]]

  def mapManaged[R, E, B](f: A => ZManaged[R, E, B]): ZManaged[R, E, Reloadable[B]]

  def makeVolatile: Volatile[UIO, A] =
    new Volatile[UIO, A] {
      override def unsafeGet: A =
        self.unsafeGet

      override def get: UIO[A] =
        self.get
    }
}

class ReloadableImpl[A](
    underlying: RawReloadableImpl[UIO, ResourceLike, A]
) extends Reloadable[A] {

  import Reloadable._

  override protected[reactiveconfig] def subscribe[G[_]](subscriber: Subscriber[UIO, A])(implicit
      effect: Effect[G],
      resource: Resource[ResourceLike, G]
  ): ResourceLike[G, Unit] =
    underlying.subscribe[G](subscriber)

  override def unsafeGet: A =
    underlying.unsafeGet

  override val get: UIO[A] =
    underlying.get

  override def forEachF[R](f: A => URIO[R, Unit]): URIO[R, Nothing] =
    RIO.environment[R] >>= { env =>
      new ResourceLikeOps[Any, Nothing, Unit](underlying.forEachF(a => f(a).provide(env))).toManaged.useForever
    }

  override def distinctByKey[K: Eq](makeKey: A => K): UManaged[Reloadable[A]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, A]](
      underlying.distinctByKey[K](makeKey)
    ).toManaged.map(new ReloadableImpl(_))

  override def map[B](f: A => B, reloadBehaviour: ReloadBehaviour[UIO, A, B]): UManaged[Reloadable[B]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, B]](
      underlying.mapF(a => UIO.succeed(f(a)), reloadBehaviour)
    ).toManaged.map(new ReloadableImpl(_))

  override def mapF[R, E, B](
      f: A => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], A, B]
  ): ZManaged[R, E, Reloadable[B]] =
    for {
      env <- ZManaged.environment[R]
      result <- new ResourceLikeOps[Any, E, RawReloadableImpl[UIO, ResourceLike, B]](
        underlying.mapF(a => f(a).provide(env), provideEnv(env, reloadBehaviour))
      ).toManaged
    } yield new ReloadableImpl(result)

  override def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (A, B), C]
  )(f: (A, B) => C): UManaged[Reloadable[C]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, C]](
      underlying.combineF(other, reloadBehaviour)((a, b) => UIO.succeed(f(a, b)))
    ).toManaged.map(new ReloadableImpl(_))

  override def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (A, B), C]
  )(f: (A, B) => ZIO[R, E, C]): ZManaged[R, E, Reloadable[C]] =
    for {
      env <- ZManaged.environment[R]
      result <- new ResourceLikeOps[Any, E, RawReloadableImpl[UIO, ResourceLike, C]](
        underlying.combineF(other, provideEnv(env, reloadBehaviour))((a, b) => f(a, b).provide(env))
      ).toManaged
    } yield new ReloadableImpl(result)

  override def mapManaged[R, E, B](f: A => ZManaged[R, E, B]): ZManaged[R, E, Reloadable[B]] =
    ZManaged(for {
      env <- ZIO.environment[(R, ZManaged.ReleaseMap)]
      result <- mapF[Any, E, (ZManaged.Finalizer, B)](
        a => f(a).zio.provide(env),
        Stop((pair: (ZManaged.Finalizer, B)) => pair._1(Exit.Success(pair._2)).unit)
      ).zio
    } yield result).flatMap(_.map(_._2))
}

object Reloadable extends EffectInstances with HugeCombines {

  def root[A](initial: A): UManaged[(Reloadable[A], A => UIO[Unit])] = {
    val x = RawReloadableImpl[UIO, UIO, ResourceLike, A, Nothing](UIO.succeed(initial), Simple())
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, A]](x).toManaged.map { r =>
      val sub = new MappedSubscriber[UIO, A, A](identity, r.modifyCurrentValue)
      (new ReloadableImpl(r), sub.onNext)
    }
  }

  def const[A](a: A): Reloadable[A] =
    new ConstReloadable(a)

  implicit class ResourceLikeOps[R, E, A](private val resource: ResourceLike[ZIO[R, E, *], A]) extends AnyVal {
    def toManaged: ZManaged[R, E, A] = {
      def go[B](resource: ResourceLike[ZIO[R, E, *], B]): ZManaged[R, E, B] =
        resource match {
          case Allocate(resource) =>
            // FIXME: uncacellable
            Managed.makeReserve(resource.map { case (b, release) =>
              Reservation(
                ZIO.succeed(b),
                _ =>
                  release.catchAll { e =>
                    UIO.effectTotal(logger.error(s"Unable to release: $e"))
                  }
              )
            })
          case FlatMap(resource, func) =>
            go(resource).flatMap(a => go(func(a)))
        }

      go(resource)
    }
  }

  def provideEnv[R, E, A, B](env: R, behaviour: ReloadBehaviour[ZIO[R, E, *], A, B]): ReloadBehaviour[IO[E, *], A, B] =
    behaviour match {
      case simple: Simple[ZIO[R, E, *], A, B] =>
        simple.asInstanceOf[ReloadBehaviour[IO[E, *], A, B]]
      case Stop(stop) =>
        Stop(b => stop(b).provide(env))
      case Restart(restart, stop) =>
        Restart((a, b) => restart(a, b).provide(env), b => stop(b).provide(env))
    }
}
