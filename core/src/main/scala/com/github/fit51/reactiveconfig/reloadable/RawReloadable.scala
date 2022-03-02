package com.github.fit51.reactiveconfig.reloadable

import java.util.concurrent.atomic.AtomicReference

import cats.~>
import cats.kernel.Eq
import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.typeclasses.Effect._
import com.github.fit51.reactiveconfig.typeclasses.HandleTo._
import com.github.fit51.reactiveconfig.typeclasses.Resource._

trait Subscriber[F[_], A] { self =>

  def onNext(next: A): F[Unit]
}

trait RawReloadable[F[_], R[_[_], _], A] {

  def unsafeGet: A

  val get: F[A]

  protected[reactiveconfig] def subscribe[G[_]](subscriber: Subscriber[F, A])(implicit
      effect: Effect[G],
      resource: Resource[R, G]
  ): R[G, Unit]
}

private final case class ReloadableState[F[_], A](
    value: A,
    subscribers: List[Subscriber[F, A]]
)

class RawReloadableImpl[F[_], R[_[_], _], A](
    init: A,
    mbRelease: Option[A => F[Unit]]
)(implicit
    effect: Effect[F],
    resource: Resource[R, F]
) extends RawReloadable[F, R, A] { self =>

  private val state = new AtomicReference[ReloadableState[F, A]](ReloadableState(init, Nil))

  private[reactiveconfig] def modifyCurrentValue(a: A): F[Unit] =
    effect.sync { () =>
      AtomicUtils.update(state)(_.copy(value = a))
    }.flatMap { state =>
      effect.parallelTraverse(state.subscribers.map(_.onNext(a))) *> {
        mbRelease match {
          case Some(release) => release(state.value).fireAndForget
          case None          => effect.unit
        }
      }
    }

  def subscribe[G[_]](subscriber: Subscriber[F, A])(implicit
      effect: Effect[G],
      resource: Resource[R, G]
  ): R[G, Unit] =
    resource.make(effect.sync { () =>
      AtomicUtils.update(state)(c => c.copy(subscribers = subscriber :: c.subscribers))
    }) { _ =>
      effect.sync { () =>
        AtomicUtils.update(state)(c => c.copy(subscribers = c.subscribers.filter(_ != subscriber)))
      }
    } as (())

  override def unsafeGet: A =
    state.get().value

  override val get: F[A] =
    effect.sync(() => state.get().value)

  def forEachF(f: A => F[Unit]): R[F, Unit] =
    resource.liftF(self.get.flatMap(f)) *> subscribe(new MappedSubscriber(identity, f))

  def mapF[G[_], E, B](
      f: A => G[B],
      reloadBehaviour: ReloadBehaviour[G, A, B]
  )(implicit
      effectG: Effect[G],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, B]] =
    RawReloadableImpl[F, G, R, B, E](handleTo.mapK(get).flatMap(f), reloadBehaviour) flatMap { mappedReloadable =>
      resourceG.liftF(mappedReloadable.get.mapK(handleTo)).flatMap { initB =>
        val subscriber =
          reloadBehaviour match {
            case Restart(restart, _) =>
              val optionalRestart = (a: A, b: B) =>
                effectG.map[B, Option[B]](restart(a, b))(Some(_)).handleErrorWith[F, E] { (e: E) =>
                  effect.warn(s"Unable to restart $a with $b because of $e").as(None)
                }
              new RestartableSubscriber[F, A, B](initB, optionalRestart, mappedReloadable.modifyCurrentValue)
            case _ =>
              val optionalFunc: A => F[Option[B]] = (a: A) =>
                effectG.map[B, Option[B]](f(a))(Some(_)).handleErrorWith { (e: E) =>
                  effect.warn(s"Unable to map $a because of $e").as(None)
                }
              new MappedFSubscriber[F, A, B](optionalFunc, mappedReloadable.modifyCurrentValue)
          }
        subscribe[G](subscriber).as(mappedReloadable)
      }
    }

  def combineF[G[_], E, B, C](rb: RawReloadable[F, R, B], reloadBehaviour: ReloadBehaviour[G, (A, B), C])(
      f: (A, B) => G[C]
  )(implicit
      effectG: Effect[G],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, C]] =
    reloadBehaviour match {
      case Restart(_, _) =>
        self
          .combineF(rb, ReloadBehaviour.simpleBehaviour[G, (A, B), (A, B)])((a, b) => effectG.pure((a, b)))
          .flatMap(_.mapF[G, E, C](f.tupled, reloadBehaviour))
      case _ =>
        resourceG.liftF(handleTo.mapK(Effect.zip(self.get, rb.get))).flatMap { case (initA, initB) =>
          RawReloadableImpl[F, G, R, C, E](f(initA, initB), reloadBehaviour) flatMap { combinedReloadable =>
            val optionalFunc: (A, B) => F[Option[C]] = (a: A, b: B) =>
              effectG.map[C, Option[C]](f(a, b))(Some(_)).handleErrorWith { (e: E) =>
                effect.warn(s"Unable to map $a and $b because of $e").as(None)
              }
            val sub = new CombinedFSubscriber(initA, initB, optionalFunc, combinedReloadable.modifyCurrentValue)
            (self.subscribe[G](sub.asSubscriberA) *> rb.subscribe[G](sub.asSubscriberB)).as(combinedReloadable)
          }
        }
    }

  def distinctByKey[K: Eq](makeKey: A => K): R[F, RawReloadableImpl[F, R, A]] =
    resource.liftF(self.get).flatMap { initA =>
      val uniqueReloadable = new RawReloadableImpl[F, R, A](initA, None)
      val subscriber       = new UniqueSubscriber(initA, makeKey, uniqueReloadable.modifyCurrentValue)
      subscribe(subscriber).as(uniqueReloadable)
    }

  def makeVolatile[G[_]](nat: F ~> G): R[F, Volatile[G, A]] = {
    val cur = self.get
    val ga  = nat.apply(cur)
    resource.liftF(cur).flatMap { initA =>
      val volatile = new VolatileImpl(initA, ga)
      val subscriber = new MappedSubscriber[F, A, A](
        identity,
        a => effect.sync(() => volatile.modifyCurrentValue(a, nat.apply(effect.pure(a))))
      )
      subscribe(subscriber).as(volatile)
    }
  }
}

private object AtomicUtils {

  def update[X](ref: AtomicReference[X])(mod: X => X): X = {
    var current = ref.get()
    while (!ref.compareAndSet(current, mod(current)))
      current = ref.get()
    current
  }

  @annotation.tailrec
  def getAndUpdate[X, A](ref: AtomicReference[X])(mod: X => (X, A)): A = {
    val current        = ref.get()
    val (newX, result) = mod(current)
    if (ref.compareAndSet(current, newX)) {
      result
    } else {
      getAndUpdate(ref)(mod)
    }
  }
}

object RawReloadableImpl {

  def apply[F[_], G[_], R[_[_], _], A, E](
      ga: G[A],
      reloadBehaviour: ReloadBehaviour[G, _, A]
  )(implicit
      resourceG: Resource[R, G],
      resourceF: Resource[R, F],
      effectG: Effect[G],
      effectF: Effect[F],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, A]] = {
    val releaseF = reloadBehaviour.release.map { release => (a: A) =>
      release(a).handleErrorWith { (e: E) =>
        effectF.warn(s"Unable to release $a because of $e")
      }
    }
    resourceG.make(
      effectG.map(ga)(a => new RawReloadableImpl[F, R, A](a, releaseF))
    ) { reloadable =>
      val shutdownG = reloadBehaviour.shutdown match {
        case Some(shutdown) =>
          val funit = reloadable.get.flatMap { b =>
            shutdown(b).handleErrorWith[F, E] { (e: E) =>
              effectF.warn(s"Unable to stop $b because of $e")
            }
          }
          handleTo.mapK(funit)
        case None =>
          effectG.unit
      }

      val warnG = effectG.sync(() => reloadable.state.get().subscribers.nonEmpty).flatMap { nonEmpty =>
        if (nonEmpty) effectG.warn("Non empty list of subscribers") else effectG.unit
      }

      shutdownG *> warnG
    }
  }
}
