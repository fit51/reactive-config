package com.github.fit51.reactiveconfig.zio.reloadable

import com.github.fit51.reactiveconfig.reloadable.{HugeCombines => HC, _}
import com.github.fit51.reactiveconfig.typeclasses._
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable._
import zio._

trait HugeCombines {

  def combine[A1, A2, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2]
  )(f: (A1, A2) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3]
  )(f: (A1, A2, A3) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4]
  )(f: (A1, A2, A3, A4) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5]
  )(f: (A1, A2, A3, A4, A5) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6]
  )(f: (A1, A2, A3, A4, A5, A6) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7]
  )(f: (A1, A2, A3, A4, A5, A6, A7) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17],
      ra18: Reloadable[A18]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17],
      ra18: Reloadable[A18],
      ra19: Reloadable[A19]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17],
      ra18: Reloadable[A18],
      ra19: Reloadable[A19],
      ra20: Reloadable[A20]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17],
      ra18: Reloadable[A18],
      ra19: Reloadable[A19],
      ra20: Reloadable[A20],
      ra21: Reloadable[A21]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20, ra21)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))

  def combine[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, Result](
      ra1: Reloadable[A1],
      ra2: Reloadable[A2],
      ra3: Reloadable[A3],
      ra4: Reloadable[A4],
      ra5: Reloadable[A5],
      ra6: Reloadable[A6],
      ra7: Reloadable[A7],
      ra8: Reloadable[A8],
      ra9: Reloadable[A9],
      ra10: Reloadable[A10],
      ra11: Reloadable[A11],
      ra12: Reloadable[A12],
      ra13: Reloadable[A13],
      ra14: Reloadable[A14],
      ra15: Reloadable[A15],
      ra16: Reloadable[A16],
      ra17: Reloadable[A17],
      ra18: Reloadable[A18],
      ra19: Reloadable[A19],
      ra20: Reloadable[A20],
      ra21: Reloadable[A21],
      ra22: Reloadable[A22]
  )(f: (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22) => Result): UManaged[Reloadable[Result]] =
    new ResourceLikeOps[Any, Nothing, RawReloadableImpl[UIO, ResourceLike, Result]](
      HC.combine(ra1, ra2, ra3, ra4, ra5, ra6, ra7, ra8, ra9, ra10, ra11, ra12, ra13, ra14, ra15, ra16, ra17, ra18, ra19, ra20, ra21, ra22)(f.tupled.andThen(UIO.succeed(_)))
    ).toManaged.map(new ReloadableImpl(_))
}
