package com.github.fit51.reactiveconfig.config

import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Reloadable

import scala.util.Try

trait ReactiveConfig[F[_], ParsedData] {
  def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T]

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[T]

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[Reloadable[F, T]]
}
