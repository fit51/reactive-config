package com.github.fit51.reactiveconfig.zio.generic

import com.github.fit51.reactiveconfig.{ReactiveConfigException, Sensitive}
import com.github.fit51.reactiveconfig.generic.CommonReloadableMacro
import com.github.fit51.reactiveconfig.generic.Configuration
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.config.ReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

import scala.reflect.macros.whitebox

class ReloadableMacro(override val c: whitebox.Context) extends CommonReloadableMacro(c) {
  import c.universe.{Scope => _, _}

  def reloadableImpl0[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]]
  )(cfg: c.Expr[Configuration]): c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl1[D, A](config, c.Expr[String](q"$emptyString"))(cfg)

  def reloadableImpl1[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String]
  )(cfg: c.Expr[Configuration]): c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl[D, A](config, prefix, cfg, false)

  def reloadableImpl2[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]]
  )(
      decoder: c.Expr[ConfigDecoder[Sensitive, D]],
      cfg: c.Expr[Configuration]
  ): c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl3[D, A](config, c.Expr[String](q"$emptyString"))(decoder, cfg)

  def reloadableImpl3[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String]
  )(
      decoder: c.Expr[ConfigDecoder[Sensitive, D]],
      cfg: c.Expr[Configuration]
  ): c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl[D, A](config, prefix, cfg, true)

  def reloadableImpl[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String],
      cfg: c.Expr[Configuration],
      sensitive: Boolean
  ): c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]] = {
    val classSymbol   = ensureCaseClass[A]
    val annotations   = extractConstructorAnnotations(classSymbol)
    val prefixLiteral = extractPrefixLiteral(prefix)
    c.Expr[ZIO[Scope, ReactiveConfigException, Reloadable[A]]](
      reloadableDefs(weakTypeOf[D], prefixLiteral, classSymbol.asType.name, annotations, cfg)
    )
  }

  override def makeFinalTree(tpname: TypeName, forExpressions: List[Tree], result: TermName): Tree =
    q"""{
       for (..$forExpressions) yield $result
    }"""
}
