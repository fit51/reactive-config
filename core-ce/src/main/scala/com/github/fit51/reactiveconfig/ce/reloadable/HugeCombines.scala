package com.github.fit51.reactiveconfig.ce.reloadable

import cats.Parallel
import cats.effect.{Concurrent, Resource => CatsResource}
import com.github.fit51.reactiveconfig.reloadable.{HugeCombines => HC}
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable._

trait HugeCombines {

  def combine[F[_], A1, A2, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2]
  )(f: (A1, A2) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3]
  )(f: (A1, A2, A3) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4]
  )(f: (A1, A2, A3, A4) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5]
  )(f: (A1, A2, A3, A4, A5) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6]
  )(f: (A1, A2, A3, A4, A5, A6) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7]
  )(f: (A1, A2, A3, A4, A5, A6, A7) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17],
      ra18: Reloadable[F, A18]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17],
      ra18: Reloadable[F, A18],
      ra19: Reloadable[F, A19]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17],
      ra18: Reloadable[F, A18],
      ra19: Reloadable[F, A19],
      ra20: Reloadable[F, A20]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17],
      ra18: Reloadable[F, A18],
      ra19: Reloadable[F, A19],
      ra20: Reloadable[F, A20],
      ra21: Reloadable[F, A21]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20, ra21)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))

  def combine[F[_], A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, Result](
      ra1: Reloadable[F, A1],
      ra2: Reloadable[F, A2],
      ra3: Reloadable[F, A3],
      ra4: Reloadable[F, A4],
      ra5: Reloadable[F, A5],
      ra6: Reloadable[F, A6],
      ra7: Reloadable[F, A7],
      ra8: Reloadable[F, A8],
      ra9: Reloadable[F, A9],
      ra10: Reloadable[F, A10],
      ra11: Reloadable[F, A11],
      ra12: Reloadable[F, A12],
      ra13: Reloadable[F, A13],
      ra14: Reloadable[F, A14],
      ra15: Reloadable[F, A15],
      ra16: Reloadable[F, A16],
      ra17: Reloadable[F, A17],
      ra18: Reloadable[F, A18],
      ra19: Reloadable[F, A19],
      ra20: Reloadable[F, A20],
      ra21: Reloadable[F, A21],
      ra22: Reloadable[F, A22]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22) => Result)(implicit
      F: Concurrent[F],
      P: Parallel[F]
  ): CatsResource[F, Reloadable[F, Result]] =
    HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20, ra21, ra22)(f.tupled.andThen(F.pure(_))).map(new CatsReloadableImpl(_))
}
