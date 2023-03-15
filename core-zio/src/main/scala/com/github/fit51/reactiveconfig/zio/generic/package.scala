package com.github.fit51.reactiveconfig.zio

import com.github.fit51.reactiveconfig.{ReactiveConfigException, Sensitive}
import com.github.fit51.reactiveconfig.generic.Configuration
import com.github.fit51.reactiveconfig.generic.RootReactiveConfig
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.config.ReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly
import scala.language.experimental.macros

package object generic {

  def deriveReloadable[D, A](
      config: ReactiveConfig[D]
  )(implicit cfg: Configuration): ZIO[Scope, ReactiveConfigException, Reloadable[A]] =
    macro ReloadableMacro.reloadableImpl0[D, A]

  def deriveReloadable[D, A](
      config: ReactiveConfig[D],
      prefix: String
  )(implicit cfg: Configuration): ZIO[Scope, ReactiveConfigException, Reloadable[A]] =
    macro ReloadableMacro.reloadableImpl1[D, A]

  def deriveSensitiveReloadable[D, A](
      config: ReactiveConfig[D]
  )(implicit
      decoder: ConfigDecoder[Sensitive, D],
      cfg: Configuration
  ): ZIO[Scope, ReactiveConfigException, Reloadable[A]] =
    macro ReloadableMacro.reloadableImpl2[D, A]

  def deriveSensitiveReloadable[D, A](
      config: ReactiveConfig[D],
      prefix: String
  )(implicit
      decoder: ConfigDecoder[Sensitive, D],
      cfg: Configuration
  ): ZIO[Scope, ReactiveConfigException, Reloadable[A]] =
    macro ReloadableMacro.reloadableImpl3[D, A]

  @compileTimeOnly("enable macro paradise to expand macro annotations")
  class reactiveconfig[Data](override val prefix: String = "", override val sensitive: Boolean = false)
      extends StaticAnnotation with RootReactiveConfig[Data] {

    def macroTransform(annottees: Any*): Any = macro ReactiveConfigAnnotation.annotationMacro
  }
}
