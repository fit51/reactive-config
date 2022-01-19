package com.github.fit51.reactiveconfig.reloadable

import cats.Id
import cats.MonadError
import cats.effect.Bracket
import cats.effect.Resource
import cats.kernel.Eq
import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.either._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import monix.eval.Task
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler
import monix.reactive.Observable
import monix.reactive.Observable.Operator
import monix.reactive.observers.Subscriber

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.Failure

object ReloadableImpl extends StrictLogging {
  val log = logger
}

private class ReloadableImpl[F[_]: TaskLike: TaskLift, A](initial: A, ob: Observable[A])(implicit
    scheduler: Scheduler,
    F: MonadError[F, Throwable]
) extends Reloadable[F, A] {
  import Reloadable._

  @volatile
  private[reloadable] var value = initial

  private[reloadable] val valueF = Task.delay(value).to[F]

  private[reloadable] def modifyCurrentValue(mbKey: Option[String], value: A): F[Unit] =
    Task.delay {
      mbKey.foreach(key => ReloadableImpl.log.info(s"Updated key $key"))
      this.value = value
    }.to[F]

  override protected[reloadable] val observable: Observable[A] = ob

  override def unsafeGet: A =
    value

  override def get: F[A] =
    valueF

  override def forEachF(f: A => F[Unit]): F[Unit] =
    (this.get >>= f) >> observable.mapEvalF(f).lastL.to[F]

  override def distinctByKey[K: Eq](makeKey: (A) => K): Resource[F, Reloadable[F, A]] =
    for {
      init <- Resource.liftF(get)
      obs = (init +: this.observable).distinctUntilChangedByKey(makeKey).drop(1)
      result <- Reloadable.apply(init, obs)
    } yield result

  override def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[F, A, B]
  ): Resource[F, Reloadable[F, B]] =
    for {
      init <- Resource.liftF(get)
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(f(init), observable.map(f))
        case Stop(stop) =>
          mapAndStopF[Id, A, B](f(init), observable, f, stop)
        case restart: Restart[F, A, B] =>
          mapAndRestartF(f(init), observable, restart)
      }
    } yield result

  override def mapF[B](
      f: A => F[B],
      reloadBehaviour: ReloadBehaviour[F, A, B]
  ): Resource[F, Reloadable[F, B]] =
    for {
      init  <- Resource.liftF(get)
      initB <- Resource.liftF(f(init))
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(initB, mapEvalAndSkipErrors(observable, f))
        case Stop(stop) =>
          mapAndStopF[F, A, B](initB, observable, f, stop)
        case restart: Restart[F, A, B] =>
          mapAndRestartF(initB, observable, restart)
      }
    } yield result

  override def mapResource[B](
      f: A => Resource[F, B]
  )(implicit bracket: Bracket[F, Throwable]): Resource[F, Reloadable[F, B]] =
    mapF(
      a => f(a).allocated,
      Stop((pair: (B, F[Unit])) => pair._2)
    ).flatMap(_.map(_._1))

  override def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => C): Resource[F, Reloadable[F, C]] =
    for {
      val1 <- Resource.liftF(get)
      val2 <- Resource.liftF(other.get)
      combinedInit = f(val1, val2)
      result <- reloadBehaviour match {
        case Simple() =>
          val combinedObs = (val1 +: this.observable).combineLatestMap(val2 +: other.observable)(f).drop(1)
          Reloadable.apply(combinedInit, combinedObs)
        case Stop(stop) =>
          val combinedObs = (val1 +: this.observable).combineLatest(val2 +: other.observable).drop(1)
          mapAndStopF[Id, (A, B), C](combinedInit, combinedObs, f.tupled, stop)
        case restart: Restart[F, (A, B), C] =>
          val combinedObs = (val1 +: this.observable).combineLatest(val2 +: other.observable).drop(1)
          mapAndRestartF(combinedInit, combinedObs, restart)
      }
    } yield result

  override def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): Resource[F, Reloadable[F, C]] =
    for {
      val1         <- Resource.liftF(get)
      val2         <- Resource.liftF(other.get)
      combinedInit <- Resource.liftF(f(val1, val2))
      combinedObs = (val1 +: this.observable).combineLatest(val2 +: other.observable).drop(1)
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(combinedInit, mapEvalAndSkipErrors(combinedObs, f.tupled))
        case Stop(stop) =>
          mapAndStopF[F, (A, B), C](combinedInit, combinedObs, f.tupled, stop)
        case restart: Restart[F, (A, B), C] =>
          mapAndRestartF(combinedInit, combinedObs, restart)
      }
    } yield result

  private def mapEvalAndSkipErrors[G[_]: TaskLike, A, B](ob: Observable[A], f: A => G[B]): Observable[B] =
    ob.mapEvalF(f.andThen(Task.from(_).attempt)).collect { case Right(b) => b }

  private def mapAndStopF[G[_]: TaskLike, I, O](
      init: O,
      obs: Observable[I],
      f: I => G[O],
      stop: O => F[_]
  ): Resource[F, Reloadable[F, O]] = {
    val nextObservable = mapEvalAndSkipErrors[G, I, O](obs, f)
      .liftByOperator(new ReleasePrevOperator(init, stop.andThen(Task.from(_).void)))
    Reloadable.apply(init, nextObservable, Some(stop))
  }

  private def mapAndRestartF[O, I](
      init: O,
      obs: Observable[I],
      restart: Restart[F, I, O]
  ): Resource[F, Reloadable[F, O]] = {
    val nextObservable = obs
      .scanEvalF(init.asRight[O].pure[F]) { case (state, newEl) =>
        val o = state.fold(identity, identity)
        restart.restart(newEl, o).map(_.asRight[O]).handleErrorWith { case e =>
          log("Failed to restart", e).as(o.asLeft[O])
        }
      }
      .collect { case Right(o) => o }
    Reloadable.apply(init, nextObservable, Some(restart.stop))
  }

  private def log(message: String, e: Throwable): F[Unit] =
    Task.delay(ReloadableImpl.log.error(message, e)).to[F]
}

private class ReleasePrevOperator[A](init: A, release: A => Task[Unit]) extends Operator[A, A] {

  import monix.execution.Ack
  import monix.execution.Ack.Stop

  override def apply(out: Subscriber[A]): Subscriber[A] =
    new Subscriber[A] {
      implicit val scheduler              = out.scheduler
      @volatile private[this] var prev: A = init

      override def onNext(elem: A): Future[Ack] =
        try {
          val nonUsed = prev
          prev = elem
          val result = out.onNext(elem)
          result.onComplete { _ =>
            releaseWithLogging(nonUsed)
          }
          result
        } catch {
          case NonFatal(ex) =>
            onError(ex)
            Stop
        }

      override def onError(ex: Throwable): Unit = {
        releaseWithLogging(prev)
        out.onError(ex)
      }

      override def onComplete(): Unit = {
        releaseWithLogging(prev)
        out.onComplete()
      }

      private def releaseWithLogging(elem: A): Unit =
        release(elem).runToFuture.onComplete {
          case Failure(e) => ReloadableImpl.log.error(s"Failed to release $elem", e)
          case _          => ()
        }
    }
}
