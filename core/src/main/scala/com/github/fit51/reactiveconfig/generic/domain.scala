package com.github.fit51.reactiveconfig.generic

import scala.annotation.StaticAnnotation

trait RootReactiveConfig[D] {

  def prefix: String

  def sensitive: Boolean
}

class source(val path: String) extends StaticAnnotation
