package com.github.fit51.reactiveconfig.reloadable

import java.util.concurrent.atomic.AtomicReference

import cats.kernel.Eq
import cats.syntax.eq._
import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.typeclasses.Effect._

import scala.collection.immutable.Queue

final case class SubscriberState[F[_], S, U](
    state: S,
    updates: Queue[(U, Unit => Unit)],
    draining: Boolean
)

abstract class AbstractSubscriber[F[_], S, U](implicit eff: Effect[F]) {

  protected val ref: AtomicReference[SubscriberState[F, S, U]]

  protected def updateState(state: S, update: U): S

  protected def handle(state: S): F[Unit]

  protected def modifyState(update: U): F[Unit] =
    eff.async[Unit] { cb =>
      eff.sync { () =>
        AtomicUtils.update(ref)(s => s.copy(updates = s.updates.enqueue(update, cb)))
      } *> drainQueue(true).fireAndForget
    }

  //         \ queue | empty       | non empty
  // drainging \     |             |
  // ----------------|-------------|------------
  // true            | avoid       | extract next task and re-drain at the end
  // false           | do nothing  | avoid
  protected def drainQueue(newTask: Boolean): F[Unit] =
    eff.sync { () =>
      AtomicUtils.getAndUpdate(ref) { state =>
        (newTask, state.draining) match {
          case (true, true) =>
            (state, None)
          case (false, false) =>
            // impossible
            ???
          case (false, true) if state.updates.isEmpty =>
            (state.copy(draining = false), None)
          case _ =>
            state.updates.dequeueOption match {
              case None =>
                // From upper pattern matching: newTask = true & draining = false
                (state, None)
              case Some(((update, cb), tail)) =>
                val newState = updateState(state.state, update)
                (state.copy(updates = tail, draining = true, state = newState), Some(newState, cb))
            }
        }
      }
    } flatMap {
      case None =>
        eff.unit
      case Some((state, cb)) =>
        handle(state) *> eff.sync(() => cb(())) *> drainQueue(false)
    }
}

private[reactiveconfig] class MappedSubscriber[F[_]: Effect, AA, A](f: AA => A, foreach: A => F[Unit])
    extends AbstractSubscriber[F, Option[AA], AA] with Subscriber[F, AA] {

  override val ref = new AtomicReference(SubscriberState(None, Queue.empty, false))

  override protected def handle(state: Option[AA]): F[Unit] =
    state.fold(Effect[F].unit)(foreach.compose(f))

  override protected def updateState(state: Option[AA], update: AA): Option[AA] =
    Some(update)

  override def onNext(next: AA): F[Unit] =
    modifyState(next)
}

private class MappedFSubscriber[F[_]: Effect, AA, A](f: AA => F[Option[A]], foreach: A => F[Unit])
    extends AbstractSubscriber[F, Option[AA], AA] with Subscriber[F, AA] {

  override val ref = new AtomicReference(SubscriberState(None, Queue.empty, false))

  override protected def handle(state: Option[AA]): F[Unit] =
    state.fold(Effect[F].unit)(f(_).flatMap(_.fold(Effect[F].unit)(foreach)))

  override protected def updateState(state: Option[AA], update: AA): Option[AA] =
    Some(update)

  override def onNext(next: AA): F[Unit] =
    modifyState(next)
}

private class CombinedFSubscriber[F[_]: Effect, A, B, C](
    initA: A,
    initB: B,
    combine: (A, B) => F[Option[C]],
    foreach: C => F[Unit]
) extends AbstractSubscriber[F, (A, B), Either[A, B]] {

  override val ref = new AtomicReference(SubscriberState((initA, initB), Queue.empty, false))

  protected def updateState(state: (A, B), update: Either[A, B]): (A, B) =
    update match {
      case Left(a)  => (a, state._2)
      case Right(b) => (state._1, b)
    }

  protected def handle(state: (A, B)): F[Unit] =
    (combine.tupled(state)).flatMap {
      case Some(c) => foreach(c)
      case None    => Effect[F].unit
    }

  val asSubscriberA: Subscriber[F, A] =
    (next: A) => modifyState(Left(next))

  val asSubscriberB: Subscriber[F, B] =
    (next: B) => modifyState(Right(next))
}

private class UniqueSubscriber[F[_]: Effect, A, K: Eq](initA: A, makeKey: A => K, foreach: A => F[Unit])
    extends AbstractSubscriber[F, (A, Boolean), A] with Subscriber[F, A] {

  override val ref = new AtomicReference(SubscriberState((initA, true), Queue.empty, false))

  override protected def handle(state: (A, Boolean)): F[Unit] =
    if (state._2) {
      foreach(state._1)
    } else {
      Effect[F].unit
    }

  override protected def updateState(state: (A, Boolean), update: A): (A, Boolean) =
    (update, makeKey(state._1) =!= makeKey(update))

  override def onNext(next: A): F[Unit] =
    modifyState(next)
}

private class RestartableSubscriber[F[_]: Effect, A, B](
    initB: B,
    restart: (A, B) => F[Option[B]],
    foreach: B => F[Unit]
) extends Subscriber[F, A] { self =>

  @volatile var b: B = initB

  override def onNext(next: A): F[Unit] =
    Effect[F].sync(() => self.b).flatMap { oldB =>
      restart(next, oldB).flatMap {
        case Some(newB) =>
          Effect[F].sync(() => self.b = newB) *> foreach(newB)
        case None =>
          Effect[F].unit
      }
    }
}

private class CombinedFSubscriberN[F[_]: Effect, Result](
    inits: Array[Any],
    combine: Array[Any] => F[Option[Result]],
    foreach: Result => F[Unit]
) extends AbstractSubscriber[F, Array[Any], (Int, Any)] {

  override val ref = new AtomicReference(SubscriberState(inits, Queue.empty, false))

  override protected def handle(state: Array[Any]): F[Unit] =
    combine(state).flatMap {
      case Some(result) => foreach(result)
      case None         => Effect[F].unit
    }

  override protected def updateState(state: Array[Any], update: (Int, Any)): Array[Any] = {
    val newArray = state.toArray
    newArray.update(update._1, update._2)
    newArray
  }

  def getSubscriber[A](i: Int): Subscriber[F, A] =
    (next: A) => modifyState((i, next))
}
