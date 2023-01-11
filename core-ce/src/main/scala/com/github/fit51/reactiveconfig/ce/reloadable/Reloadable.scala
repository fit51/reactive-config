package com.github.fit51.reactiveconfig.ce.reloadable

import cats.{~>, Eq, Monad, Parallel}
import cats.effect.{Concurrent, Resource => CatsResource}
import com.github.fit51.reactiveconfig.ce.instances.CatsEffectInstances
import com.github.fit51.reactiveconfig.ce.instances.all._
import com.github.fit51.reactiveconfig.reloadable._
import com.github.fit51.reactiveconfig.reloadable.MappedSubscriber
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.typeclasses.Resource

/** Reloadable is a wrapped [[A]] value, that can be accessed at any time.
  *
  * @tparam F[_]
  *   reloading effect
  * @tparam A
  *   wrapped value
  */
trait Reloadable[F[_], A] extends RawReloadable[F, CatsResource, A] { self =>

  /** Returns current value of this Reloadable.
    */
  override def unsafeGet: A

  /** Returns current value of this Reloadable.
    */
  override val get: F[A]

  /** Applies given function that may contains side-effect for each element of Reloadable.
    */
  def forEachF(f: A => F[Unit]): F[Unit]

  /** Returns a new Reloadable with suppressed consecutive duplicated elemented elements. Elements are compared by given
    * function.
    *
    * @param makeKey
    *   is a function that returns a K key for each element, a value that's then used to do the deduplication
    */
  def distinctByKey[K: Eq](makeKey: A => K): CatsResource[F, Reloadable[F, A]]

  /** Returns a new Reloadable by mapping the supplied function over the elements of the source Reloadable.
    *
    * @param f
    *   is the mapping function that transforms the source
    * @param reloadBehaviour
    *   is reload policy which may release or restart allocated resources
    *
    * @return
    *   a new Reloadable that's the result of mapping the given function over the source
    */
  def map[B](
      f: A => B,
      reloadBehaviour: ReloadBehaviour[F, A, B] = ReloadBehaviour.simpleBehaviour[F, A, B]
  ): CatsResource[F, Reloadable[F, B]]

  /** Returns a new Reloadable by mapping the supplied function that returns possibly lazy or asynchronous result.
    *
    * @param f
    *   is the mapping function that transforms the source
    * @param reloadBehaviour
    *   is reload policy which may release or restart allocated resources
    *
    * @return
    *   a new Reloadable that's the result of mapping the given function over the source
    */
  def mapF[B](
      f: A => F[B],
      reloadBehaviour: ReloadBehaviour[F, A, B] = ReloadBehaviour.simpleBehaviour[F, A, B]
  ): CatsResource[F, Reloadable[F, B]]

  /** Returns a new Reloadable by mapping the supplied function that returns closeable resource.
    *
    * @param f
    *   is the mapping function that transforms the source
    *
    * @return
    *   a new Reloadable that's the result of mapping the given function over the source
    */
  def mapResource[B](
      f: A => CatsResource[F, B]
  ): CatsResource[F, Reloadable[F, B]]

  /** Creates a new Reloadable from the source and another given Reloadable, by emitting elements created from pairs.
    *
    * @param other
    *   is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour
    *   is reload policy which may release or restart allocated resources
    */
  def combine[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => C): CatsResource[F, Reloadable[F, C]]

  /** Creates a new Reloadable from the source and another given Reloadable, by emitting elements created from pairs.
    * Creating new elements may contain lazy or asynchronous effect.
    *
    * @param other
    *   is Reloadable that gets paired with current Reloadable
    * @param reloadBehaviour
    *   is reload policy which may release or restart allocated resources
    */
  def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C] = ReloadBehaviour.simpleBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): CatsResource[F, Reloadable[F, C]]

  def makeVolatile[G[_]](nat: F ~> G): CatsResource[F, Volatile[G, A]]

  def widen[B >: A]: Reloadable[F, B] =
    this.asInstanceOf[Reloadable[F, B]]
}

class CatsReloadableImpl[F[_], A](
    underlying: RawReloadableImpl[F, CatsResource, A]
)(implicit F: Concurrent[F], P: Parallel[F])
    extends Reloadable[F, A] {
  override val get: F[A] =
    underlying.get

  override def unsafeGet: A =
    underlying.unsafeGet

  override def map[B](f: A => B, reloadBehaviour: ReloadBehaviour[F, A, B]): CatsResource[F, Reloadable[F, B]] =
    underlying.mapF(a => F.pure(f(a)), reloadBehaviour).map(new CatsReloadableImpl(_))

  override def mapF[B](f: A => F[B], reloadBehaviour: ReloadBehaviour[F, A, B]): CatsResource[F, Reloadable[F, B]] =
    underlying.mapF[F, Throwable, B](f, reloadBehaviour).map(new CatsReloadableImpl(_))

  override def mapResource[B](f: A => CatsResource[F, B]): CatsResource[F, Reloadable[F, B]] =
    mapF(
      a => f(a).allocated,
      Stop((pair: (B, F[Unit])) => pair._2)
    ).flatMap(_.map[B](_._1, ReloadBehaviour.simpleBehaviour[F, (B, F[Unit]), B]))

  override def combine[B, C](other: Reloadable[F, B], reloadBehaviour: ReloadBehaviour[F, (A, B), C])(
      f: (A, B) => C
  ): CatsResource[F, Reloadable[F, C]] =
    underlying.combineF(other, reloadBehaviour)((a, b) => F.pure(f(a, b))).map(new CatsReloadableImpl(_))

  override def combineF[B, C](
      other: Reloadable[F, B],
      reloadBehaviour: ReloadBehaviour[F, (A, B), C]
  )(f: (A, B) => F[C]): CatsResource[F, Reloadable[F, C]] =
    underlying.combineF(other, reloadBehaviour)(f).map(new CatsReloadableImpl(_))

  override def distinctByKey[K](makeKey: A => K)(implicit eq: Eq[K]): CatsResource[F, Reloadable[F, A]] =
    underlying.distinctByKey[K](makeKey).map(new CatsReloadableImpl(_))

  override def forEachF(f: A => F[Unit]): F[Unit] =
    underlying.forEachF(f).use(_ => Concurrent[F].never)

  override def makeVolatile[G[_]](nat: F ~> G): CatsResource[F, Volatile[G, A]] =
    underlying.makeVolatile(nat)

  override protected[reactiveconfig] def subscribe[G[_]](subscriber: Subscriber[F, A])(implicit
      effect: Effect[G],
      resource: Resource[CatsResource, G]
  ): CatsResource[G, Unit] =
    underlying.subscribe[G](subscriber)
}

object Reloadable extends HugeCombines with CatsEffectInstances {

  def root[F[_]]: RootBuilder[F] = new RootBuilder[F]

  final class RootBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {
    def apply[A](
        initial: A
    )(implicit concurrent: Concurrent[F], P: Parallel[F]): CatsResource[F, (Reloadable[F, A], A => F[Unit])] =
      RawReloadableImpl[F, F, CatsResource, A, Throwable](Concurrent[F].pure(initial), Simple()).map { r =>
        val sub = new MappedSubscriber[F, A, A](identity, r.modifyCurrentValue)
        (new CatsReloadableImpl(r), sub.onNext)
      }
  }

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {

    def apply[A](a: A)(implicit monad: Monad[F]): Reloadable[F, A] =
      new ConstReloadable[F, A](a)
  }
}
