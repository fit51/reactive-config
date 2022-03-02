package com.github.fit51.reactiveconfig.reloadable

/** Reload policy
  *
  * @see
  *   [[ReloadableInternal]]
  */
sealed trait ReloadBehaviour[F[_], A, B] {
  val release: Option[B => F[Unit]]

  val shutdown: Option[B => F[Unit]]
}

/** Most common reload policy
  *
  * Doesn't postprocess neither [[B]] nor [[A]] when new one comes
  */
final case class Simple[F[_], A, B]() extends ReloadBehaviour[F, A, B] {

  override val release: Option[B => F[Unit]] = None

  override val shutdown: Option[B => F[Unit]] = None
}

/** Stop reload policy
  *
  * When new [[A]] arrives then a new [[B]] is created and then current [[B]] is stopped
  * @param stop
  *   Stop function
  */
final case class Stop[F[_], A, B](stop: B => F[Unit]) extends ReloadBehaviour[F, A, B] {

  override val release: Option[B => F[Unit]] = Some(stop)

  override val shutdown: Option[B => F[Unit]] = Some(stop)
}

/** Restart reload policy
  *
  * When new [[A]] arrives - restarts current B instance with [[restart]]
  * @param restart
  *   shows how to restart current instance[[B]] with new input [[A]]
  * @param stop
  *   shows how to stop last instance[[B]] when corresponded Reloadable is released Note: Use this in cases, when [[B]]
  *   supports restart.
  */
final case class Restart[F[_], A, B](restart: (A, B) => F[B], stop: B => F[Unit]) extends ReloadBehaviour[F, A, B] {

  override val release: Option[B => F[Unit]] = None

  override val shutdown: Option[B => F[Unit]] = Some(stop)
}

object ReloadBehaviour {
  private val noOpBehaviour = new Simple[Nothing, Nothing, Nothing]()

  def simpleBehaviour[F[_], A, B]: ReloadBehaviour[F, A, B] =
    noOpBehaviour.asInstanceOf[Simple[F, A, B]]
}
