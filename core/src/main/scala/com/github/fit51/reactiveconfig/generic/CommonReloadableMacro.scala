package com.github.fit51.reactiveconfig.generic

import scala.reflect.macros.whitebox

abstract class CommonReloadableMacro(val c: whitebox.Context) {
  import c.universe._

  val emptyString = ""

  type ClassParam = (String, Type, String) // (name, type, path)

  def ensureCaseClass[A: WeakTypeTag]: ClassSymbol =
    weakTypeOf[A] match {
      case TypeRef(_, classSymbol, _) if classSymbol.isClass && classSymbol.asClass.isCaseClass =>
        classSymbol.asClass
      case _ =>
        c.abort(c.enclosingPosition, "Only for case classes")
    }

  def extractConstructorAnnotations(clazz: ClassSymbol): List[ClassParam] =
    clazz.primaryConstructor.asMethod.paramLists.head.map { param =>
      param.annotations.map(_.tree).collectFirst {
        case tree @ q"new $parent($path)" if tree.tpe =:= typeOf[source] =>
          (param.name.toTermName.encodedName.toString, param.asTerm.info, c.eval(c.Expr[String](path)))
      } getOrElse c.abort(c.enclosingPosition, s"${clazz.fullName}.${param.name} does not have source annotation")
    }

  def extractPrefixLiteral(prefix: c.Expr[String]): String =
    prefix.tree match {
      case Literal(Constant(prefix: String)) => prefix
      case _                                 => c.abort(c.enclosingPosition, "Prefix is not a literal")
    }

  def getFullMemberPath(prefix: String, param: ClassParam): String = {
    val path = param._3
    if (path.startsWith("//")) {
      path.drop(2)
    } else if (prefix.nonEmpty) {
      s"$prefix.$path"
    } else {
      path
    }
  }

  def reloadableDefs(cfgTpe: Type, prefix: String, tpname: TypeName, params: List[ClassParam]): Tree =
    params.size match {
      case 0 =>
        c.abort(c.enclosingPosition, "Invalid annotation target: case class must have at least one member")
      case 1 =>
        val result = TermName("result")
        makeFinalTree(
          tpname,
          makeRootReloadables(cfgTpe, prefix, params) :+ fq"$result <- ${rootReloadableName(params.head)}.map(${tpname.toTermName}.apply)",
          result
        )
      case len if len <= 22 =>
        makeFinalTree(
          tpname,
          makeRootReloadables(cfgTpe, prefix, params) ::: combinedReloadablesForMediumClasses(tpname, params),
          combinedReloadable(0)
        )
      case _ =>
        makeFinalTree(
          tpname,
          makeRootReloadables(cfgTpe, prefix, params) ::: combinedReloadablesForHugeClasses(tpname, params),
          TermName("result")
        )
    }

  def makeFinalTree(tpname: TypeName, forExpressions: List[Tree], result: TermName): Tree

  /** Generates set of following lines for each member of case class
    * {{{
    *   intR <- config.reloadable[Int]("asdf")
    * }}}
    */
  def makeRootReloadables(cfgTpe: Type, prefix: String, params: List[ClassParam]): List[Tree] =
    params.map { param =>
      val fullPath = getFullMemberPath(prefix, param)
      val tpe      = c.typecheck(q"${param._2}", c.TYPEmode).tpe
      fq"""${rootReloadableName(param)} <- config.reloadable[$tpe]($fullPath)"""
    }

  def rootReloadableName(param: ClassParam): TermName =
    TermName(s"${param._1}R")

  def combinedReloadable(idx: Int): TermName =
    TermName(s"comb${idx}R")

  /** Produces such expression:
    * {{{
    *   Reloadable.combine(rootR0, rootR1, ...)(CaseClass.apply _)
    * }}}
    */
  def combinedReloadablesForMediumClasses(tpname: TypeName, params: List[ClassParam]): List[Tree] = {
    val reloadables = params.map(rootReloadableName)
    List(fq"""
      ${combinedReloadable(0)} <- Reloadable.combine(..$reloadables)(${tpname.toTermName}.apply _)
    """)
  }

  def combinedReloadablesForHugeClasses(tpname: TypeName, params: List[ClassParam]): List[Tree] = {
    val result = TermName("result")
    val resultReloadable = fq"""
      $result <- ${constructHugeClassFromList(tpname, params, combinedReloadable((params.length / 22) + 1))}
    """
    combinedReloadablesForHugeClasses0(params) :+ resultReloadable
  }

  /** Produces such expressions:
    * {{{
    *   comb1 <- Reloadables.combine(r1, r2, ..., r22)(_ :: _ :: ... :: Nil)
    *   comb2 <- Reloadables.combine(r1, r2, ..., rK)(_ :: _ :: ... :: Nil)
    *   ....
    *   comb3 <- Reloadables.combine(comb1, comb2, ...)(List(_, _, ...).flatten)
    * }}}
    */
  def combinedReloadablesForHugeClasses0(params: List[ClassParam]): List[Tree] = {
    val groups = params.grouped(22).toList
    val (listReloadableNames, listReloadables) = groups.zipWithIndex.map { case (group, i) =>
      val (names, args) = group.zipWithIndex.map { case (param, idx) =>
        val name = TermName("v" + idx)
        (name, q"val $name: Any")
      }.unzip
      val reloadables    = group.map(rootReloadableName)
      val reloadableName = combinedReloadable(i)
      (reloadableName, fq"""$reloadableName <- Reloadable.combine(..$reloadables)((..$args) => List(..$names))""")
    }.unzip

    val (names, args) = groups.zipWithIndex.map { case (param, idx) =>
      val name = TermName("v" + idx)
      (name, q"val $name: List[Any]")
    }.unzip
    val finalReloadable = fq"""
      ${combinedReloadable(
        groups.size
      )} <- Reloadable.combine(..$listReloadableNames)((..$args) => List(..$names).flatten)
    """

    listReloadables :+ finalReloadable
  }

  /** Produces such expression
    * {{{
    *   reloadable map { tail0 =>
    *     val arg0  = tail0.head.asInstanceOf[Field1Type]
    *     val tail1 = tail0.tail
    *     val arg1  = tail1.head.asInstanceOf[Field2Type]
    *     val tail2 = tail1.tail
    *       ....
    *     A(arg0, arg1, ...)
    *   }
    * }}}
    */
  def constructHugeClassFromList(tpname: TypeName, params: List[ClassParam], anyListReloadable: TermName): Tree = {
    val allArgs =
      params.zipWithIndex.map { case (param, idx) =>
        val input = TermName(s"tail$idx")
        val arg   = TermName(s"arg$idx")
        val tail  = TermName(s"tail${idx + 1}")
        val tpe   = c.typecheck(tq"${param._2}", c.TYPEmode).tpe
        q"val $arg = $input.head.asInstanceOf[$tpe]; val $tail = $input.tail"
      } reduce { (tree1, tree2) => q"..$tree1; ..$tree2" }
    val constructorParams = params.indices.map(idx => TermName(s"arg$idx"))
    val name              = tpname.toTermName
    val caseClass         = q"$name.apply(..$constructorParams)"

    q"""
      $anyListReloadable map { tail0 =>
        ..$allArgs
        $caseClass
      }
    """
  }

  protected def info(any: Any): Unit =
    c.info(c.enclosingPosition, any.toString, true)
}
