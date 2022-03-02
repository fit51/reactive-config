package com.github.fit51.reactiveconfig.typeclasses

trait Effect[F[_]] {

  def pure[A](a: A): F[A]

  val unit: F[Unit] =
    pure(())

  def sync[A](thunk: () => A): F[A]

  def async[A](cb: (A => Unit) => F[Unit]): F[A]

  def map[A, B](fa: F[A])(f: A => B): F[B]

  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]

  def fireAndForget[A](fa: F[A]): F[Unit]

  def parallelTraverse(fas: List[F[Unit]]): F[Unit]

  def info(message: String): F[Unit]

  def warn(message: String): F[Unit]

  def warn(message: String, e: Throwable): F[Unit]
}

object Effect extends Zips {

  def apply[F[_]](implicit ev: Effect[F]): Effect[F] = ev

  implicit class EffectOps[F[_], A](private val fa: F[A]) extends AnyVal {

    def map[B](func: A => B)(implicit eff: Effect[F]): F[B] =
      eff.map(fa)(func)

    def as[B](b: B)(implicit eff: Effect[F]): F[B] =
      eff.map(fa)(_ => b)

    def unit(implicit eff: Effect[F]): F[Unit] =
      eff.map(fa)(_ => ())

    def flatMap[B](func: A => F[B])(implicit eff: Effect[F]): F[B] =
      eff.flatMap(fa)(func)

    def *>[B](next: F[B])(implicit eff: Effect[F]): F[B] =
      eff.flatMap(fa)(_ => next)

    def fireAndForget(implicit eff: Effect[F]): F[Unit] =
      eff.fireAndForget(fa)
  }

  def traverse[F[_], A](as: Iterable[A])(f: A => F[Unit])(implicit eff: Effect[F]): F[Unit] = {
    val it = as.iterator

    def traverseInternal: F[Unit] =
      if (it.hasNext) {
        eff.flatMap(f(it.next()))(_ => traverseInternal)
      } else {
        eff.unit
      }

    traverseInternal
  }
}

trait Zips {

  def zip[F[_], A1, A2](
      fa1: F[A1],
      fa2: F[A2]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2)] = {
    e.flatMap(fa1)(a1 => e.map(fa2)(a2 => (a1, a2)))
  }

  def zip[F[_], A1, A2, A3](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.map(fa3)(a3 => (a1, a2, a3))))
  }

  def zip[F[_], A1, A2, A3, A4](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.map(fa4)(a4 => (a1, a2, a3, a4)))))
  }

  def zip[F[_], A1, A2, A3, A4, A5](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.map(fa5)(a5 => (a1, a2, a3, a4, a5))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.map(fa6)(a6 => (a1, a2, a3, a4, a5, a6)))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.map(fa7)(a7 => (a1, a2, a3, a4, a5, a6, a7))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.map(fa8)(a8 => (a1, a2, a3, a4, a5, a6, a7, a8)))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.map(fa9)(a9 => (a1, a2, a3, a4, a5, a6, a7, a8, a9))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.map(fa10)(a10 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10)))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.map(fa11)(a11 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.map(fa12)(a12 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.map(fa13)(a13 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.map(fa14)(a14 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14)))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.map(fa15)(a15 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.map(fa16)(a16 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16)))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.map(fa17)(a17 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17))))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17],
      fa18: F[A18]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.flatMap(fa17)(a17 => e.map(fa18)(a18 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18)))))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17],
      fa18: F[A18],
      fa19: F[A19]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.flatMap(fa17)(a17 => e.flatMap(fa18)(a18 => e.map(fa19)(a19 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19))))))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17],
      fa18: F[A18],
      fa19: F[A19],
      fa20: F[A20]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.flatMap(fa17)(a17 => e.flatMap(fa18)(a18 => e.flatMap(fa19)(a19 => e.map(fa20)(a20 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20)))))))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17],
      fa18: F[A18],
      fa19: F[A19],
      fa20: F[A20],
      fa21: F[A21]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.flatMap(fa17)(a17 => e.flatMap(fa18)(a18 => e.flatMap(fa19)(a19 => e.flatMap(fa20)(a20 => e.map(fa21)(a21 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21))))))))))))))))))))))
  }

  def zip[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22](
      fa1: F[A1],
      fa2: F[A2],
      fa3: F[A3],
      fa4: F[A4],
      fa5: F[A5],
      fa6: F[A6],
      fa7: F[A7],
      fa8: F[A8],
      fa9: F[A9],
      fa10: F[A10],
      fa11: F[A11],
      fa12: F[A12],
      fa13: F[A13],
      fa14: F[A14],
      fa15: F[A15],
      fa16: F[A16],
      fa17: F[A17],
      fa18: F[A18],
      fa19: F[A19],
      fa20: F[A20],
      fa21: F[A21],
      fa22: F[A22]
  )(implicit
      e: Effect[F]
  ): F[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22)] = {
    e.flatMap(fa1)(a1 => e.flatMap(fa2)(a2 => e.flatMap(fa3)(a3 => e.flatMap(fa4)(a4 => e.flatMap(fa5)(a5 => e.flatMap(fa6)(a6 => e.flatMap(fa7)(a7 => e.flatMap(fa8)(a8 => e.flatMap(fa9)(a9 => e.flatMap(fa10)(a10 => e.flatMap(fa11)(a11 => e.flatMap(fa12)(a12 => e.flatMap(fa13)(a13 => e.flatMap(fa14)(a14 => e.flatMap(fa15)(a15 => e.flatMap(fa16)(a16 => e.flatMap(fa17)(a17 => e.flatMap(fa18)(a18 => e.flatMap(fa19)(a19 => e.flatMap(fa20)(a20 => e.flatMap(fa21)(a21 => e.map(fa22)(a22 => (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22)))))))))))))))))))))))
  }
}
