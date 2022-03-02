package com.github.fit51.reactiveconfig.zio.generic

import com.github.fit51.reactiveconfig.{ReactiveConfigException, Sensitive}
import com.github.fit51.reactiveconfig.generic.CommonReloadableMacro
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.config.ReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

import scala.reflect.macros.whitebox

class ReloadableMacro(override val c: whitebox.Context) extends CommonReloadableMacro(c) {
  import c.universe._

  def reloadableImpl0[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]]
  ): c.Expr[Managed[ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl1[D, A](config, c.Expr[String](q"$emptyString"))

  def reloadableImpl1[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String]
  ): c.Expr[Managed[ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl[D, A](config, prefix, false)

  def reloadableImpl2[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]]
  )(decoder: c.Expr[ConfigDecoder[Sensitive, D]]): c.Expr[Managed[ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl3[D, A](config, c.Expr[String](q"$emptyString"))(decoder)

  def reloadableImpl3[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String]
  )(decoder: c.Expr[ConfigDecoder[Sensitive, D]]): c.Expr[Managed[ReactiveConfigException, Reloadable[A]]] =
    reloadableImpl[D, A](config, prefix, true)

  def reloadableImpl[D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[D]],
      prefix: c.Expr[String],
      sensitive: Boolean
  ): c.Expr[Managed[ReactiveConfigException, Reloadable[A]]] = {
    val classSymbol   = ensureCaseClass[A]
    val annotations   = extractConstructorAnnotations(classSymbol)
    val prefixLiteral = extractPrefixLiteral(prefix)
    c.Expr[Managed[ReactiveConfigException, Reloadable[A]]](
      reloadableDefs(weakTypeOf[D], prefixLiteral, classSymbol.asType.name, annotations)
    )
  }

  override def makeFinalTree(tpname: TypeName, forExpressions: List[Tree], result: TermName): Tree =
    q"""{
       for (..$forExpressions) yield $result
    }"""
}
