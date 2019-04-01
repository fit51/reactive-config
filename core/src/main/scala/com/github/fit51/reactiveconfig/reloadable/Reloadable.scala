package com.github.fit51.reactiveconfig.reloadable

import monix.reactive.Observable

/**
  * Reloadable is used for building dependant, reloadable variables.
  *
  * It is a piece of your Configuration, a unit of change-propagation chain.
  *
  * You can think of Reloadable as a wrapped [[B]] value, that reloads, when [[A]] changes.
  *
  * Reloadable has a bunch on methods for building change-propagation chains.
  *
  * @tparam F [_] reloading effect
  * @tparam A input, or dependant value
  * @tparam B output, or wrapped value
  **/
trait Reloadable[F[_], A, B] {
  def get: B

  def observable: Observable[B]

  def map[C](f: B => C, behaviour: ReloadBehaviour[F, B, C] = new Simple[F, B, C]): Reloadable[F, B, C]

  def mapF[C](f: B => F[C], behaviour: ReloadBehaviour[F, B, C] = new Simple[F, B, C]): F[Reloadable[F, B, C]]

  def combine[C, D](other: Reloadable[F, _, C])(
      f: (B, C) => D,
      behaviour: ReloadBehaviour[F, (B, C), D] = new Simple[F, (B, C), D]): Reloadable[F, (B, C), D]

  def combineF[C, D](other: Reloadable[F, _, C])(
      f: (B, C) => F[D],
      behaviour: ReloadBehaviour[F, (B, C), D] = new Simple[F, (B, C), D]): F[Reloadable[F, (B, C), D]]
}
