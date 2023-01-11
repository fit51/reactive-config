package com.github.fit51.reactiveconfig.reloadable

import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.typeclasses.Effect._
import com.github.fit51.reactiveconfig.typeclasses.HandleTo._
import com.github.fit51.reactiveconfig.typeclasses.Resource._

object HugeCombines {

  private def handleErrorWith[G[_], F[_], Result, E](
      args: Array[Any],
      result: G[Result]
  )(implicit effectG: Effect[G], effectF: Effect[F], handleTo: HandleTo[G, F, E]): F[Option[Result]] =
    effectG
      .map[Result, Option[Result]](result)(Some(_))
      .handleErrorWith { (e: E) =>
        effectF.warn(s"Unable to ${args.toList.mkString(", ")} because of $e").as(None)
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2]
  )(f: ((A1, A2)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get)))
      .flatMap { case tuple @ (a1, a2) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2])))
          }
          val inits = Array(a1, a2)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3]
  )(f: ((A1, A2, A3)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get)))
      .flatMap { case tuple @ (a1, a2, a3) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3])))
          }
          val inits = Array(a1, a2, a3)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4]
  )(f: ((A1, A2, A3, A4)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4])))
          }
          val inits = Array(a1, a2, a3, a4)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5]
  )(f: ((A1, A2, A3, A4, A5)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5])))
          }
          val inits = Array(a1, a2, a3, a4, a5)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6]
  )(f: ((A1, A2, A3, A4, A5, A6)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7]
  )(f: ((A1, A2, A3, A4, A5, A6, A7)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17],
      ra18: RawReloadable[F, R, A18],
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get, ra18.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17], array(17).asInstanceOf[A18])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> ra18.subscribe[G](sub.getSubscriber[A18](17)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17],
      ra18: RawReloadable[F, R, A18],
      ra19: RawReloadable[F, R, A19]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get, ra18.get, ra19.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17], array(17).asInstanceOf[A18], array(18).asInstanceOf[A19])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> ra18.subscribe[G](sub.getSubscriber[A18](17)) *> ra19.subscribe[G](sub.getSubscriber[A19](18)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17],
      ra18: RawReloadable[F, R, A18],
      ra19: RawReloadable[F, R, A19],
      ra20: RawReloadable[F, R, A20]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get, ra18.get, ra19.get, ra20.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17], array(17).asInstanceOf[A18], array(18).asInstanceOf[A19], array(19).asInstanceOf[A20])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> ra18.subscribe[G](sub.getSubscriber[A18](17)) *> ra19.subscribe[G](sub.getSubscriber[A19](18)) *> ra20.subscribe[G](sub.getSubscriber[A20](19)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17],
      ra18: RawReloadable[F, R, A18],
      ra19: RawReloadable[F, R, A19],
      ra20: RawReloadable[F, R, A20],
      ra21: RawReloadable[F, R, A21]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get, ra18.get, ra19.get, ra20.get, ra21.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17], array(17).asInstanceOf[A18], array(18).asInstanceOf[A19], array(19).asInstanceOf[A20], array(20).asInstanceOf[A21])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> ra18.subscribe[G](sub.getSubscriber[A18](17)) *> ra19.subscribe[G](sub.getSubscriber[A19](18)) *> ra20.subscribe[G](sub.getSubscriber[A20](19)) *> ra21.subscribe[G](sub.getSubscriber[A21](20)) *> resourceG.pure(combinedReloadable)
        }
      }

  def combine[F[_], G[_], R[_[_], _], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, Result, E](
      ra1: RawReloadable[F, R, A1],
      ra2: RawReloadable[F, R, A2],
      ra3: RawReloadable[F, R, A3],
      ra4: RawReloadable[F, R, A4],
      ra5: RawReloadable[F, R, A5],
      ra6: RawReloadable[F, R, A6],
      ra7: RawReloadable[F, R, A7],
      ra8: RawReloadable[F, R, A8],
      ra9: RawReloadable[F, R, A9],
      ra10: RawReloadable[F, R, A10],
      ra11: RawReloadable[F, R, A11],
      ra12: RawReloadable[F, R, A12],
      ra13: RawReloadable[F, R, A13],
      ra14: RawReloadable[F, R, A14],
      ra15: RawReloadable[F, R, A15],
      ra16: RawReloadable[F, R, A16],
      ra17: RawReloadable[F, R, A17],
      ra18: RawReloadable[F, R, A18],
      ra19: RawReloadable[F, R, A19],
      ra20: RawReloadable[F, R, A20],
      ra21: RawReloadable[F, R, A21],
      ra22: RawReloadable[F, R, A22]
  )(f: ((A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22)) => G[Result])(implicit
      effectF: Effect[F],
      effectG: Effect[G],
      resourceF: Resource[R, F],
      resourceG: Resource[R, G],
      handleTo: HandleTo[G, F, E]
  ): R[G, RawReloadableImpl[F, R, Result]] =
    resourceG
      .liftF(handleTo.mapK(Effect.zip(ra1.get, ra2.get, ra3.get, ra4.get, ra5.get, ra6.get, ra7.get, ra8.get, ra9.get, ra10.get, ra11.get, ra12.get, ra13.get, ra14.get, ra15.get, ra16.get, ra17.get, ra18.get, ra19.get, ra20.get, ra21.get, ra22.get)))
      .flatMap { case tuple @ (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22) =>
        RawReloadableImpl[F, G, R, Result, E](
          f(tuple),
          ReloadBehaviour.simpleBehaviour
        ) flatMap { combinedReloadable =>
          val optionalFunc: Array[Any] => F[Option[Result]] = (array: Array[Any]) => {
            handleErrorWith[G, F, Result, E](array, f((array(0).asInstanceOf[A1], array(1).asInstanceOf[A2], array(2).asInstanceOf[A3], array(3).asInstanceOf[A4], array(4).asInstanceOf[A5], array(5).asInstanceOf[A6], array(6).asInstanceOf[A7], array(7).asInstanceOf[A8], array(8).asInstanceOf[A9], array(9).asInstanceOf[A10], array(10).asInstanceOf[A11], array(11).asInstanceOf[A12], array(12).asInstanceOf[A13], array(13).asInstanceOf[A14], array(14).asInstanceOf[A15], array(15).asInstanceOf[A16], array(16).asInstanceOf[A17], array(17).asInstanceOf[A18], array(18).asInstanceOf[A19], array(19).asInstanceOf[A20], array(20).asInstanceOf[A21], array(21).asInstanceOf[A22])))
          }
          val inits = Array(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22)
          val sub = new CombinedFSubscriberN[F, Result](inits, optionalFunc, combinedReloadable.modifyCurrentValue)
          ra1.subscribe[G](sub.getSubscriber[A1](0)) *> ra2.subscribe[G](sub.getSubscriber[A2](1)) *> ra3.subscribe[G](sub.getSubscriber[A3](2)) *> ra4.subscribe[G](sub.getSubscriber[A4](3)) *> ra5.subscribe[G](sub.getSubscriber[A5](4)) *> ra6.subscribe[G](sub.getSubscriber[A6](5)) *> ra7.subscribe[G](sub.getSubscriber[A7](6)) *> ra8.subscribe[G](sub.getSubscriber[A8](7)) *> ra9.subscribe[G](sub.getSubscriber[A9](8)) *> ra10.subscribe[G](sub.getSubscriber[A10](9)) *> ra11.subscribe[G](sub.getSubscriber[A11](10)) *> ra12.subscribe[G](sub.getSubscriber[A12](11)) *> ra13.subscribe[G](sub.getSubscriber[A13](12)) *> ra14.subscribe[G](sub.getSubscriber[A14](13)) *> ra15.subscribe[G](sub.getSubscriber[A15](14)) *> ra16.subscribe[G](sub.getSubscriber[A16](15)) *> ra17.subscribe[G](sub.getSubscriber[A17](16)) *> ra18.subscribe[G](sub.getSubscriber[A18](17)) *> ra19.subscribe[G](sub.getSubscriber[A19](18)) *> ra20.subscribe[G](sub.getSubscriber[A20](19)) *> ra21.subscribe[G](sub.getSubscriber[A21](20)) *> ra22.subscribe[G](sub.getSubscriber[A22](21)) *> resourceG.pure(combinedReloadable)
        }
      }
}
