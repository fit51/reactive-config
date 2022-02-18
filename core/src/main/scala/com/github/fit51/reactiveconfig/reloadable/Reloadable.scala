package com.github.fit51.reactiveconfig.reloadable

import cats.~>
import cats.Applicative
import cats.Id
import cats.MonadError
import cats.effect.Bracket
import cats.effect.Resource
import cats.instances.option._
import cats.kernel.Eq
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.foldable._
import monix.eval.Task
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler
import monix.reactive.Consumer
import monix.reactive.Observable

object Reloadable {

  private[reactiveconfig] def apply[F[_]: TaskLike: TaskLift, A](
      initial: A,
      ob: Observable[A],
      elementReleaser: Option[A => F[_]] = None,
      mbKey: Option[String] = None
  )(implicit scheduler: Scheduler, F: MonadError[F, Throwable]): Resource[F, Reloadable[F, A]] =
    Resource((for {
      connectableObservable <- Task.pure(ob.publish)
      canceler   = connectableObservable.connect()
      reloadable = new ReloadableImpl[F, A](initial, connectableObservable)
      fiber <- connectableObservable.consumeWith(Consumer.foreachEval(reloadable.modifyCurrentValue(mbKey, _))).start
      release = (fiber.cancel >> Task.delay(canceler.cancel())).to[F] >>
        elementReleaser.traverse_(finalizer => reloadable.get.flatMap(finalizer(_).void))
    } yield (reloadable: Reloadable[F, A], release)).to[F])

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {

    def apply[A](a: A)(implicit applicative: Applicative[F]): Reloadable[F, A] =
      new ConstReloadable[F, A](a)(applicative)
  }

  implicit private[reloadable] val taskLikeId = new TaskLike[Id] {
    override def apply[A](fa: Id[A]): Task[A] = Task.pure(fa)
  }
}

/** Reloadable is a wrapped [[A]] value, that can be accessed at any time.
  *
  * @tparam F[_] reloading effect
  * @tparam A wrapped value
  */
trait Reloadable[F[_], A] { self =>

  /** Returns current value of this Reloadable.
    */
  def unsafeGet: A

  /** Returns current value of this Reloadable.
    */
  def get: F[A]

  /** Applies given function that may contains side-effect for each element of Reloadable.
    */
  def forEachF(f: A => F[Unit]): F[Unit]

  /** Returns a new Reloadable with suppressed consecutive duplicated elemented elements.
    * Elements are compared by given function.
    *
    * @param makeKey is a function that returns a K key for each element, a value that's
    * then used to do the deduplication
    */
  def distinctByKey[K: Eq](makeKey: (A) => K): Resource[F, Reloadable[F, A]]

  /** Returns a new Reloadable by mapping the supplied function over the elements of
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
  ): Resource[F, Reloadable[F, B]]

  /** Returns a new Reloadable by mapping the supplied function that returns possibly lazy
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
  ): Resource[F, Reloadable[F, B]]

  /** Returns a new Reloadable by mapping the supplied function that returns closeable resource.
    *
    * @param f is the mapping function that transforms the source
    *
    * @return a new Reloadable that's the result of mapping the given
    *         function over the source
    */
  def mapResource[B](
      f: A => Resource[F, B]
  )(implicit bracket: Bracket[F, Throwable]): Resource[F, Reloadable[F, B]]

  /** Creates a new Reloadable from the source and another given Reloadable, by emitting elements
    * created from pairs.
    *
    * @param other is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    */
  def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => C): Resource[F, Reloadable[F, C]]

  /** Creates a new Reloadable from the source and another given Reloadable, by emitting elements
    * created from pairs. Creating new elements may contain lazy or asynchronous effect.
    *
    * @param other is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour is reload policy which may release or restart allocated resources
    */
  def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): Resource[F, Reloadable[F, C]]

  def widen[B >: A]: Reloadable[F, B] =
    this.asInstanceOf[Reloadable[F, B]]

  protected[reloadable] def observable: Observable[A]

  def makeVolatile: Volatile[F, A] =
    new Volatile[F, A] {
      override def unsafeGet: A = self.unsafeGet
      override def get: F[A]    = self.get
    }
}

/** A Volatile is simplified Reloadable.
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

  def widen[B >: A]: Volatile[F, B] =
    this.asInstanceOf[Volatile[F, B]]

  def mapK[G[_]](natTranform: F ~> G): Volatile[G, A] =
    new Volatile[G, A] {
      override def unsafeGet: A =
        self.unsafeGet
      override def get: G[A] =
        natTranform.apply(self.get)
    }
}

object Volatile {

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[A](a: A)(implicit applicative: Applicative[F]): Volatile[F, A] =
      new Volatile[F, A] {
        override def unsafeGet: A = a
        override def get: F[A]    = applicative.pure(a)
      }
  }
}
