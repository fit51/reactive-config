package com.github.fit51.reactiveconfig.ce

import cats.Parallel
import cats.effect.{Async, Resource}
import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.generic.Configuration
import com.github.fit51.reactiveconfig.generic.RootReactiveConfig
import com.github.fit51.reactiveconfig.parser.ConfigDecoder

import scala.annotation.StaticAnnotation
import scala.language.experimental.macros

package object generic {

  def deriveReloadable[F[_], D, A](
      config: ReactiveConfig[F, D]
  )(implicit F: Async[F], P: Parallel[F], cfg: Configuration): Resource[F, Reloadable[F, A]] =
    macro ReloadableMacro.reloadableImpl0[F, D, A]

  def deriveReloadable[F[_], D, A](
      config: ReactiveConfig[F, D],
      prefix: String
  )(implicit F: Async[F], P: Parallel[F], cfg: Configuration): Resource[F, Reloadable[F, A]] =
    macro ReloadableMacro.reloadableImpl1[F, D, A]

  def deriveSensitiveReloadable[F[_], D, A](
      config: ReactiveConfig[F, D]
  )(implicit
      decoder: ConfigDecoder[Sensitive, D],
      F: Async[F],
      P: Parallel[F],
      cfg: Configuration
  ): Resource[F, Reloadable[F, A]] =
    macro ReloadableMacro.reloadableImpl2[F, D, A]

  def deriveSensitiveReloadable[F[_], D, A](
      config: ReactiveConfig[F, D],
      prefix: String
  )(implicit
      decoder: ConfigDecoder[Sensitive, D],
      F: Async[F],
      P: Parallel[F],
      cfg: Configuration
  ): Resource[F, Reloadable[F, A]] =
    macro ReloadableMacro.reloadableImpl3[F, D, A]

  // @compileTimeOnly("enable macro paradise to expand macro annotations")
  class reactiveconfig[Data](override val prefix: String = "", override val sensitive: Boolean = false)
      extends StaticAnnotation with RootReactiveConfig[Data] {
    def macroTransform(annottees: Any*): Any = macro ReactiveConfigAnnotation.annotationMacro
  }
}
