package com.github.fit51.reactiveconfig.reloadable

import cats.FlatMap
import cats.implicits._

/**
  * Reload policy
  *
  * @see [[ReloadableInternal]]
  */
trait ReloadBehaviour[F[_], A, B] {

  /**
    * @param start is a transformation function from input [[A]] to output [[B]]
    * @return reload function: given updated input [[A]] and current output [[B]] returns new output instance [[B]]
    */
  def reload(start: A => F[B]): (A, B) => F[B]
}

/**
  * Most common reload policy
  *
  * When new [[A]] arrives - creates new [[B]]
  */
case class Simple[F[_], A, B]() extends ReloadBehaviour[F, A, B] {
  def reload(start: A => F[B]): (A, B) => F[B] =
    (a, _) => start(a)
}

/**
  * Stop reload policy
  *
  * When new [[A]] arrives - creates a new [[B]] and then stops current [[B]]
  * @param stop Stop function
  */
case class Stop[F[_]: FlatMap, A, B](stop: B => F[_]) extends ReloadBehaviour[F, A, B] {
  def reload(start: A => F[B]): (A, B) => F[B] =
    (a, b) =>
      for {
        newB <- start(a)
        _    <- stop(b)
      } yield newB
}

/**
  * Restart reload policy
  *
  * When new [[A]] arrives - restarts current B instance with [[restart]]
  * @param restart shows how to restart current instance[[B]] with new input [[A]]
  * Note: Use this in cases, when [[B]] supports restart.
  */
case class Restart[F[_], A, B](restart: (A, B) => F[B]) extends ReloadBehaviour[F, A, B] {
  def reload(start: A => F[B]): (A, B) => F[B] = (a, b) => restart(a, b)
}
