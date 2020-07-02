package com.github.fit51.reactiveconfig.reloadable

import cats.~>
import cats.Id
import cats.MonadError
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
import monix.reactive.Consumer
import monix.reactive.Observable
import monix.reactive.Observable.Operator
import monix.reactive.observers.Subscriber

import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.Failure

object Reloadable {
  def apply[F[_]: TaskLike: TaskLift, A](
      initial: A,
      ob: Observable[A]
  )(implicit scheduler: Scheduler, F: MonadError[F, Throwable]): F[Reloadable[F, A]] =
    (for {
      connectableObservable <- Task.pure(ob.publish)
      canceler   = connectableObservable.connect()
      reloadable = new ReloadableImpl[F, A](initial, connectableObservable)
      fiber <- connectableObservable
        .consumeWith(Consumer.foreach { newValue =>
          reloadable.value = newValue
        })
        .start
    } yield {
      reloadable.canceler = (fiber.cancel >> Task.delay(canceler.cancel())).to[F]
      reloadable: Reloadable[F, A]
    }).to[F]

  implicit private[reloadable] val taskLikeId = new TaskLike[Id] {
    override def apply[A](fa: Id[A]): Task[A] = Task.pure(fa)
  }
}

/**
  * Reloadable is a wrapped [[A]] value, that can be accessed at any time.
  *
  * @tparam F[_] reloading effect
  * @tparam A wrapped value
  **/
trait Reloadable[F[_], A] { self =>

  /**
    * Returns current value of this Reloadable.
    */
  def unsafeGet: A

  /**
    * Returns current value of this Reloadable.
    */
  def get: F[A]

  /**
    * Applies given function that may contains side-effect for each element of Reloadable.
    */
  def forEachF(f: A => F[Unit]): F[Unit]

  /**
    * Returns a new Reloadable with suppressed consecutive duplicated elemented elements.
    * Elements are compared by given function.
    *
    * @param makeKey is a function that returns a K key for each element, a value that's
    * then used to do the deduplication
    */
  def distinctByKey[K: Eq](makeKey: (A) => K): F[Reloadable[F, A]]

  /**
    * Returns a new Reloadable by mapping the supplied function over the elements of
    * the source Reloadable.
    *
    * @param f is the mapping function that transforms the source
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    *
    * @return a new Reloadable that's the result of mapping the given
    *         function over the source
    */
  def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[F, A, B] = ReloadBehaviour.simpleBehaviour[F, A, B]
  ): F[Reloadable[F, B]]

  /**
    * Returns a new Reloadable by mapping the supplied function that returns possibly lazy
    * or asynchronous result.
    *
    * @param f is the mapping function that transforms the source
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    *
    * @return a new Reloadable that's the result of mapping the given
    *         function over the source
    */
  def mapF[B](
      f: A => F[B],
      reloadBehaviour: ReloadBehaviour[F, A, B] = ReloadBehaviour.simpleBehaviour[F, A, B]
  ): F[Reloadable[F, B]]

  /**
    * Creates a new Reloadable from the source and another given Reloadable, by emitting elements
    * created from pairs.
    *
    * @param other is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    */
  def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => C): F[Reloadable[F, C]]

  /**
    * Creates a new Reloadable from the source and another given Reloadable, by emitting elements
    * created from pairs. Creating new elements may contain lazy or asynchronous effect.
    *
    * @param other is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    */
  def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): F[Reloadable[F, C]]

  /**
    * Stops current Reloadable and cancels created subscriptions.
    */
  def stop: F[Unit]

  protected[reloadable] def observable: Observable[A]

  /**
    * Returns a new Reloadable wrapped in another effect G.
    */
  def mapK[G[_]: TaskLike: TaskLift: MonadError[?[_], Throwable]]: G[Reloadable[G, A]]

  def makeVolatile: Volatile[F, A] =
    new Volatile[F, A] {
      override def unsafeGet: A = self.unsafeGet
      override def get: F[A]    = self.get
    }
}

/**
  * A Volatile is simplified Reloadable.
  *
  * Reloadable provides a lot of possibilities for mapping and combining Reloadables but
  * it has complex interface where F[_] is used in covariant as well as in contravariant
  * positions. It makes impossible to change effect F[_] for Reloadable once it is created.
  * This may be necessary if several different effects are used inside one application.
  *
  * Volatile has only one method `get` where F[_] is used in covariant position. Such interface
  * allows to implement method `mapK` and change effect using natural transformation.
  *
  * Note: Method `mapK` is also added in trait Reloadable but it requires instances of
  * `TaskLike` and `TaskLift`. Method `Volatile.mapK` takes only natural transformation
  * from initial effect to new one.
  */
trait Volatile[F[_], A] { self =>

  def unsafeGet: A

  def get: F[A]

  def mapK[G[_]](natTranform: F ~> G): Volatile[G, A] =
    new Volatile[G, A] {
      override def unsafeGet: A =
        self.unsafeGet
      override def get: G[A] =
        natTranform.apply(self.get)
    }
}

private class ReloadableImpl[F[_]: TaskLike: TaskLift, A](initial: A, ob: Observable[A])(
    implicit scheduler: Scheduler,
    F: MonadError[F, Throwable]
) extends Reloadable[F, A] with StrictLogging {
  import Reloadable._

  @volatile
  private[reloadable] var value = initial

  private[reloadable] val valueF = Task.delay(value).to[F]

  override protected[reloadable] val observable: Observable[A] = ob

  override def unsafeGet: A =
    value

  override def get: F[A] =
    valueF

  override def forEachF(f: A => F[Unit]): F[Unit] =
    (this.get >>= f) >> observable.mapEvalF(f).lastL.to[F]

  override def distinctByKey[K: Eq](makeKey: (A) => K): F[Reloadable[F, A]] =
    for {
      init <- get
      obs = (init +: this.observable).distinctUntilChangedByKey(makeKey).drop(1)
      result <- Reloadable.apply(init, obs)
    } yield result

  override def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[F, A, B]
  ): F[Reloadable[F, B]] =
    for {
      init <- get
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(f(value), observable.map(f))
        case Stop(stop) =>
          mapAndStopF[Id, A, B](f(init), observable, f, stop)
        case restart: Restart[F, A, B] =>
          mapAndRestartF(f(init), observable, restart)
      }
    } yield result

  override def mapF[B](
      f: A => F[B],
      reloadBehaviour: ReloadBehaviour[F, A, B]
  ): F[Reloadable[F, B]] =
    (for {
      initB <- f(value)
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(initB, mapEvalAndSkipErrors(observable, f))
        case Stop(stop) =>
          mapAndStopF[F, A, B](initB, observable, f, stop)
        case restart: Restart[F, A, B] =>
          mapAndRestartF(initB, observable, restart)
      }
    } yield result) handleErrorWith {
      case excp =>
        log("Failed to construct init value for Reloadable.mapF", excp) >> F.raiseError(excp)
    }

  override def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => C): F[Reloadable[F, C]] =
    for {
      val1 <- this.get
      val2 <- other.get
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
  )(f: (A, B) => F[C]): F[Reloadable[F, C]] =
    (for {
      val1         <- this.get
      val2         <- other.get
      combinedInit <- f(val1, val2)
      combinedObs = (val1 +: this.observable).combineLatest(val2 +: other.observable).drop(1)
      result <- reloadBehaviour match {
        case Simple() =>
          Reloadable.apply(combinedInit, mapEvalAndSkipErrors(combinedObs, f.tupled))
        case Stop(stop) =>
          mapAndStopF[F, (A, B), C](combinedInit, combinedObs, f.tupled, stop)
        case restart: Restart[F, (A, B), C] =>
          mapAndRestartF(combinedInit, combinedObs, restart)
      }
    } yield result) handleErrorWith {
      case excp =>
        log("Failed to construct init value for Reloadable.combineF", excp) >> F.raiseError(excp)
    }

  private[reloadable] var canceler: F[Unit] = F.unit

  override def stop: F[Unit] =
    canceler

  override def mapK[G[_]: TaskLike: TaskLift: MonadError[?[_], Throwable]]: G[Reloadable[G, A]] =
    Reloadable[G, A](initial, ob)

  private def mapEvalAndSkipErrors[G[_]: TaskLike, A, B](ob: Observable[A], f: A => G[B]): Observable[B] =
    ob.mapEvalF(f.andThen(Task.from(_).attempt)).collect { case Right(b) => b }

  private def mapAndStopF[G[_]: TaskLike, I, O](
      init: O,
      obs: Observable[I],
      f: I => G[O],
      stop: O => F[_]
  ): F[Reloadable[F, O]] = {
    val nextObservable = mapEvalAndSkipErrors[G, I, O](obs, f)
      .liftByOperator(new ReleasePrevOperator(init, stop.andThen(Task.from(_).void)))
    Reloadable.apply(init, nextObservable)
  }

  private def mapAndRestartF[O, I](
      init: O,
      obs: Observable[I],
      restart: Restart[F, I, O]
  ): F[Reloadable[F, O]] = {
    val nextObservable = obs
      .scanEvalF(init.asRight[O].pure[F]) {
        case (state, newEl) =>
          val o = state.fold(identity, identity)
          restart
            .restart(newEl, o)
            .map(_.asRight[O])
            .handleErrorWith {
              case e =>
                log("Failed to restart", e).as(o.asLeft[O])
            }
      }
      .collect { case Right(o) => o }
    Reloadable.apply(init, nextObservable)
  }

  private def log(message: String, e: Throwable): F[Unit] =
    Task.delay(logger.error(message, e)).to[F]
}

private class ReleasePrevOperator[A](init: A, release: A => Task[Unit]) extends Operator[A, A] with StrictLogging {

  import monix.execution.Ack
  import monix.execution.Ack.Stop

  override def apply(out: Subscriber[A]): Subscriber[A] =
    new Subscriber[A] {
      implicit val scheduler              = out.scheduler
      @volatile private[this] var prev: A = init

      override def onNext(elem: A): Future[Ack] = {
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
          case Failure(e) => logger.error(s"Failed to release $elem", e)
          case _          => ()
        }
    }
}
