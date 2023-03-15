package com.github.fit51.reactiveconfig.ce.generic

import cats.Parallel
import cats.effect.{Concurrent, Resource}
import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.ce.config.ReactiveConfig
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.generic.CommonReloadableMacro
import com.github.fit51.reactiveconfig.generic.Configuration
import com.github.fit51.reactiveconfig.parser.ConfigDecoder

import scala.reflect.macros.whitebox

class ReloadableMacro(override val c: whitebox.Context) extends CommonReloadableMacro(c) {
  import c.universe._

  def reloadableImpl0[F[_], D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[F, D]]
  )(
      F: c.Expr[Concurrent[F]],
      P: c.Expr[Parallel[F]],
      cfg: c.Expr[Configuration]
  ): c.Expr[Resource[F, Reloadable[F, A]]] =
    reloadableImpl1[F, D, A](config, c.Expr[String](q"$emptyString"))(F, P, cfg)

  def reloadableImpl1[F[_], D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[F, D]],
      prefix: c.Expr[String]
  )(
      F: c.Expr[Concurrent[F]],
      P: c.Expr[Parallel[F]],
      cfg: c.Expr[Configuration]
  ): c.Expr[Resource[F, Reloadable[F, A]]] =
    reloadableImpl[F, D, A](config, prefix, cfg, false)

  def reloadableImpl2[F[_], D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[F, D]]
  )(
      decoder: c.Expr[ConfigDecoder[Sensitive, D]],
      F: c.Expr[Concurrent[F]],
      P: c.Expr[Parallel[F]],
      cfg: c.Expr[Configuration]
  ): c.Expr[Resource[F, Reloadable[F, A]]] =
    reloadableImpl3[F, D, A](config, c.Expr[String](q"$emptyString"))(decoder, F, P, cfg)

  def reloadableImpl3[F[_], D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[F, D]],
      prefix: c.Expr[String]
  )(
      decoder: c.Expr[ConfigDecoder[Sensitive, D]],
      F: c.Expr[Concurrent[F]],
      P: c.Expr[Parallel[F]],
      cfg: c.Expr[Configuration]
  ): c.Expr[Resource[F, Reloadable[F, A]]] =
    reloadableImpl[F, D, A](config, prefix, cfg, true)

  def reloadableImpl[F[_], D: WeakTypeTag, A: WeakTypeTag](
      config: c.Expr[ReactiveConfig[F, D]],
      prefix: c.Expr[String],
      cfg: c.Expr[Configuration],
      sensitive: Boolean
  ): c.Expr[Resource[F, Reloadable[F, A]]] = {
    val classSymbol   = ensureCaseClass[A]
    val annotations   = extractConstructorAnnotations(classSymbol)
    val prefixLiteral = extractPrefixLiteral(prefix)
    c.Expr[Resource[F, Reloadable[F, A]]](
      reloadableDefs(weakTypeOf[D], prefixLiteral, classSymbol.asType.name, annotations, cfg)
    )
  }

  def makeFinalTree(tpname: TypeName, forExpressions: List[Tree], result: TermName): Tree =
    q"""{
       import cats.syntax.flatMap._
       import cats.syntax.functor._

       for (..$forExpressions) yield $result
    }"""
}
