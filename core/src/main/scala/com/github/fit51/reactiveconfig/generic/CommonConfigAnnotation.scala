package com.github.fit51.reactiveconfig.generic

import scala.reflect.macros.whitebox

abstract class CommonConfigAnnotation(val c: whitebox.Context) {
  import c.universe._

  val reactiveconfigType = c.weakTypeOf[com.github.fit51.reactiveconfig.generic.RootReactiveConfig[_]]

  def annotationMacro(annottees: c.Expr[Any]*): c.Expr[Any] = {
    val (configType, prefix, sensitive) = c.macroApplication match {
      case Apply(Select(tree @ q"new $annName(..$args)", _), _) =>
        val tpe = c.typecheck(tree).tpe
        if (tpe <:< reactiveconfigType) {
          val baseType = tpe.baseType(reactiveconfigType.typeSymbol)
          val configType =
            baseType.typeArgs.headOption.getOrElse(c.abort(c.enclosingPosition, "Unable to define base config type"))
          args match {
            case Literal(Constant(prefix: String)) :: Literal(Constant(sensitive: Boolean)) :: Nil =>
              (configType, prefix, sensitive)
            case Literal(Constant(prefix: String)) :: Nil =>
              (configType, prefix, false)
            case _ =>
              val prefix = args collectFirst { case q"prefix = $prefix" =>
                c.eval(c.Expr[String](prefix))
              } getOrElse ""
              val sensitive = args collectFirst { case q"sensitive = $sensitive" =>
                c.eval(c.Expr[Boolean](sensitive))
              } getOrElse false
              (configType, prefix, sensitive)
          }
        } else {
          c.abort(c.enclosingPosition, "Must be used only with macro annotation reactiveconfig")
        }
      case _ =>
        c.abort(c.enclosingPosition, "Must be used only with macro annotation reactiveconfig")
    }

    val result = annottees map (_.tree) match {
      case (classDef @ q"$mods class $tpname[..$tparams] $ctorMods(..$params) extends { ..$earlydefns } with ..$parents { $self => ..$stats }")
          :: Nil if mods.hasFlag(Flag.CASE) =>
        q"""
         $classDef
         object ${tpname.toTermName} {
           ..${reloadableAndVolatile(configType, tpname, prefix, sensitive)}
         }
         """

      case (classDef @ q"$mods class $tpname[..$tparams] $ctorMods(..$params) extends { ..$earlydefns } with ..$parents { $self => ..$stats }")
          :: q"$objMods object $objName extends { ..$objEarlyDefs } with ..$objParents { $objSelf => ..$objDefs }"
          :: Nil if mods.hasFlag(Flag.CASE) =>
        q"""
         $classDef
         $objMods object $objName extends { ..$objEarlyDefs } with ..$objParents { $objSelf =>
           ..$objDefs

           ..${reloadableAndVolatile(configType, tpname, prefix, sensitive)}
         }
         """

      case _ =>
        c.abort(c.enclosingPosition, "Invalid annotation target: must be a case class")
    }
    c.Expr[Any](result)
  }

  def reloadableAndVolatile(dataType: Type, tpname: TypeName, prefix: String, sensitive: Boolean): Tree
}
