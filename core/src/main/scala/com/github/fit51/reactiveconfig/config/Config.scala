package com.github.fit51.reactiveconfig.config

import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import scala.util.Try

trait Config[F[_], ParsedData] {
  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Option[T]

  def getTry[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T]

  def getOrThrow[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): T

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Reloadable[F, T, T]
}
