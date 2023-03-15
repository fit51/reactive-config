package com.github.fit51.reactiveconfig.zio.reloadable

import cats.kernel.Eq
import com.github.fit51.reactiveconfig.reloadable._
import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.zio.instances._
import zio._

trait Reloadable[A] extends RawReloadable[UIO, ResourceLike[*[_], *], A] { self =>

  def unsafeGet: A

  val get: UIO[A]

  def forEachF[R](f: A => URIO[R, Unit]): URIO[R with Scope, Nothing]

  def distinctByKey[K: Eq](makeKey: A => K): URIO[Scope, Reloadable[A]]

  def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[UIO, A, B] = ReloadBehaviour.simpleBehaviour[UIO, A, B]
  ): URIO[Scope, Reloadable[B]]

  def mapF[R, E, B](
      f: A => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], A, B] = ReloadBehaviour.simpleBehaviour[ZIO[R, E, *], A, B]
  ): ZIO[R with Scope, E, Reloadable[B]]

  def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (A, B), C] = ReloadBehaviour.simpleBehaviour[UIO, (A, B), C]
  )(f: (A, B) => C): URIO[Scope, Reloadable[C]]

  def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (A, B), C] =
        ReloadBehaviour.simpleBehaviour[ZIO[R, E, *], (A, B), C]
  )(f: (A, B) => ZIO[R, E, C]): ZIO[R with Scope, E, Reloadable[C]]

  def mapScoped[R, E, B](f: A => ZIO[R with Scope, E, B]): ZIO[R with Scope, E, Reloadable[B]]

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

  override def forEachF[R](f: A => URIO[R, Unit]): URIO[R with Scope, Nothing] =
    ZIO.environmentWithZIO[R] { env =>
      new ResourceLikeOps[Any, Nothing, Unit](underlying.forEachF(a => f(a).provideEnvironment(env))).toScoped
        .zipRight(ZIO.never)
    }

  override def distinctByKey[K: Eq](makeKey: A => K): URIO[Scope, Reloadable[A]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, A]](
      underlying.distinctByKey[K](makeKey)
    ).toScoped.map(new ReloadableImpl(_))

  override def map[B](f: A => B, reloadBehaviour: ReloadBehaviour[UIO, A, B]): URIO[Scope, Reloadable[B]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, B]](
      underlying.mapF(a => ZIO.succeed(f(a)), reloadBehaviour)
    ).toScoped.map(new ReloadableImpl(_))

  override def mapF[R, E, B](
      f: A => ZIO[R, E, B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], A, B]
  ): ZIO[R with Scope, E, Reloadable[B]] =
    for {
      env <- ZIO.environment[R]
      result <- new ResourceLikeOps[Any, E, RawReloadableImpl[UIO, ResourceLike, B]](
        underlying.mapF(a => f(a).provideEnvironment(env), provideEnv(env, reloadBehaviour))
      ).toScoped
    } yield new ReloadableImpl(result)

  override def combine[B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[UIO, (A, B), C]
  )(f: (A, B) => C): URIO[Scope, Reloadable[C]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, C]](
      underlying.combineF(other, reloadBehaviour)((a, b) => ZIO.succeed(f(a, b)))
    ).toScoped.map(new ReloadableImpl(_))

  override def combineF[R, E, B, C](
      other: Reloadable[B],
      reloadBehaviour: ReloadBehaviour[ZIO[R, E, *], (A, B), C]
  )(f: (A, B) => ZIO[R, E, C]): ZIO[R with Scope, E, Reloadable[C]] =
    for {
      env <- ZIO.environment[R]
      result <- new ResourceLikeOps[Any, E, RawReloadableImpl[UIO, ResourceLike, C]](
        underlying.combineF(other, provideEnv(env, reloadBehaviour))((a, b) => f(a, b).provideEnvironment(env))
      ).toScoped
    } yield new ReloadableImpl(result)

  override def mapScoped[R, E, B](f: A => ZIO[R with Scope, E, B]): ZIO[R with Scope, E, Reloadable[B]] =
    mapF[R, E, (B, Scope.Closeable)](
      a => Scope.make.flatMap(scope => scope.extend[R](f(a)) <*> ZIO.succeed(scope)),
      Stop((pair: (B, Scope.Closeable)) => pair._2.close(Exit.Success(pair._1)))
    ).flatMap(_.map(_._1))
}

object Reloadable extends EffectInstances with HugeCombines {

  def root[A](initial: A): URIO[Scope, (Reloadable[A], A => UIO[Unit])] = {
    val x = RawReloadableImpl[UIO, UIO, ResourceLike, A, Nothing](ZIO.succeed(initial), Simple())
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, A]](x).toScoped.map { r =>
      val sub = new MappedSubscriber[UIO, A, A](identity, r.modifyCurrentValue)
      (new ReloadableImpl(r), sub.onNext)
    }
  }

  def const[A](a: A): Reloadable[A] =
    new ConstReloadable(a)

  implicit class ResourceLikeOps[R, E, A](private val resource: ResourceLike[ZIO[R, E, *], A]) extends AnyVal {
    def toScoped: ZIO[R with Scope, E, A] = {
      def go[B](resource: ResourceLike[ZIO[R, E, *], B]): ZIO[R with Scope, E, B] =
        resource match {
          case Allocate(resource) =>
            ZIO.scopeWith { scope =>
              ZIO.environmentWithZIO[R] { env =>
                resource.flatMap { case (b, release) =>
                  scope
                    .addFinalizer(release.provideEnvironment(env).catchAllCause { cause =>
                      ZIO.logErrorCause("Unable to release", cause)
                    }).as(b)
                }
              }
            }
          case FlatMap(resource, func) =>
            go(resource).flatMap(a => go(func(a)))
        }

      go(resource)
    }
  }

  def provideEnv[R, E, A, B](
      env: ZEnvironment[R],
      behaviour: ReloadBehaviour[ZIO[R, E, *], A, B]
  ): ReloadBehaviour[IO[E, *], A, B] =
    behaviour match {
      case simple: Simple[ZIO[R, E, *], A, B] =>
        simple.asInstanceOf[ReloadBehaviour[IO[E, *], A, B]]
      case Stop(stop) =>
        Stop(b => stop(b).provideEnvironment(env))
      case Restart(restart, stop) =>
        Restart((a, b) => restart(a, b).provideEnvironment(env), b => stop(b).provideEnvironment(env))
    }
}
