package com.github.fit51.reactiveconfig

import cats.Apply
import cats.effect.Resource
import com.github.fit51.reactiveconfig.reloadable.Reloadable

package object instances {

  type X[F[_], A] = Resource[F, Reloadable[F, A]]

  def coerse[F[_], A](x: Resource[F, Reloadable[F, A]]): X[F, A] = x

  implicit def reloadbleFApply[F[_]]: Apply[Lambda[A => Resource[F, Reloadable[F, A]]]] =
    new Apply[Lambda[A => Resource[F, Reloadable[F, A]]]] {

      override def map[A, B](fa: Resource[F, Reloadable[F, A]])(f: A => B): Resource[F, Reloadable[F, B]] =
        fa.flatMap(_.map(f))

      override def ap[A, B](
          frr: Resource[F, Reloadable[F, A => B]]
      )(arr: Resource[F, Reloadable[F, A]]): Resource[F, Reloadable[F, B]] =
        frr.flatMap(fr => arr.flatMap(ar => fr.combine(ar)({ case (f, a) => f(a) })))
    }
}
