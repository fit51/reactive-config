package com.github.fit51.reactiveconfig.reloadable

import cats.{Applicative, MonadError}
import cats.syntax.all._
import com.typesafe.scalalogging.LazyLogging
import monix.eval.TaskLike
import monix.execution.Scheduler
import monix.reactive.Observable

object ReloadableImpl {
  def apply[F[_], A, B](
      initial: B,
      ob: Observable[A],
      start: A => F[B],
      reloadBehaviour: ReloadBehaviour[F, A, B] = Simple[F, A, B]()
  )(implicit scheduler: Scheduler, F: MonadError[F, Throwable], T: TaskLike[F]): ReloadableImpl[F, A, B] =
    new ReloadableImpl[F, A, B](initial, ob, start, reloadBehaviour)
}

/**
  * Reloadable is a wrapped [[B]] value, that can be accessed at any time.
  * Inside it encapsulates logic for updating [[B]] on [[A]] changes.
  *
  * @see [[ReloadableInternal]]
  * @param initial   value of type [[B]]
  * @param ob        observable of [[A]] changes
  * @param start     function to get [[B]] from [[A]]. Is used in [[behaviour]]
  * @param behaviour [[ReloadBehaviour]]
  * @tparam F [_] reloading effect
  * @tparam A input
  * @tparam B output
  **/
class ReloadableImpl[F[_], A, B](initial: B, ob: Observable[A], start: A => F[B], behaviour: ReloadBehaviour[F, A, B])(
    implicit scheduler: Scheduler,
    F: MonadError[F, Throwable],
    T: TaskLike[F]
) extends ReloadableInternal[F, A, B] with LazyLogging {

  @volatile
  private var value      = initial
  private val underlying = ob.mapEvalF(safeReload).collect { case Some(v) => v }
  underlying.subscribe()

  private def safeReload(a: A): F[Option[B]] =
    behaviour
      .reload(start)(a, get)
      .map { v =>
        value = v
        Option(v)
      }
      .recover {
        case e =>
          logger.error("Failed to reload", e)
          None
      }

  def get: B = value

  def observable: Observable[B] = underlying

  def map[C](f: B => C, behaviour: ReloadBehaviour[F, B, C]): ReloadableInternal[F, B, C] = {
    val init = f(get)
    ReloadableImpl[F, B, C](init, this.observable, in => Applicative[F].pure(f(in)), behaviour)
  }

  def mapF[C](f: B => F[C], behaviour: ReloadBehaviour[F, B, C]): F[ReloadableInternal[F, B, C]] = {
    f(get).map { c =>
      val init = c
      ReloadableImpl[F, B, C](init, this.observable, f, behaviour)
    }
  }

  def combine[C, D](other: ReloadableInternal[F, _, C])(
      f: (B, C) => D,
      behaviour: ReloadBehaviour[F, (B, C), D]
  ): ReloadableInternal[F, (B, C), D] = {
    val b1       = Observable(Observable.eval(this.get), this.observable).concat
    val b2       = Observable(Observable.eval(other.get), other.observable).concat
    val combined = b1.combineLatest(b2).drop(1)
    val init     = f(this.get, other.get)
    ReloadableImpl[F, (B, C), D](init, combined, in => Applicative[F].pure(f(in._1, in._2)), behaviour)
  }

  def combineF[C, D](other: ReloadableInternal[F, _, C])(
      f: (B, C) => F[D],
      behaviour: ReloadBehaviour[F, (B, C), D]
  ): F[ReloadableInternal[F, (B, C), D]] = {
    f(this.get, other.get).map { c =>
      val init     = c
      val b1       = Observable(Observable.eval(this.get), this.observable).concat
      val b2       = Observable(Observable.eval(other.get), other.observable).concat
      val combined = b1.combineLatest(b2).drop(1)
      ReloadableImpl[F, (B, C), D](init, combined, in => f(in._1, in._2), behaviour)
    }
  }
}
