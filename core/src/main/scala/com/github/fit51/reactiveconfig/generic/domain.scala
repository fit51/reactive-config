package com.github.fit51.reactiveconfig.generic

import java.util.regex.Pattern

import scala.annotation.StaticAnnotation

trait RootReactiveConfig[D] {

  def prefix: String

  def sensitive: Boolean
}

class source(val path: String) extends StaticAnnotation

case class Configuration(
    transformMemberNames: String => String
) {

  def withSnakeCaseMemberNames: Configuration =
    copy(transformMemberNames = Configuration.snakeCaseTransformation)

  def withScreamingSnakeCaseTransformation: Configuration =
    copy(transformMemberNames = Configuration.screamingSnakeCaseTransformation)
}

object Configuration {

  val default: Configuration =
    Configuration(identity)

  private val basePattern: Pattern = Pattern.compile("([A-Z]+)([A-Z][a-z])")
  private val swapPattern: Pattern = Pattern.compile("([a-z\\d])([A-Z])")

  val snakeCaseTransformation: String => String = s => {
    val partial = basePattern.matcher(s).replaceAll("$1_$2")
    swapPattern.matcher(partial).replaceAll("$1_$2").toLowerCase
  }

  val screamingSnakeCaseTransformation: String => String = s => {
    val partial = basePattern.matcher(s).replaceAll("$1_$2")
    swapPattern.matcher(partial).replaceAll("$1_$2").toUpperCase
  }
}
