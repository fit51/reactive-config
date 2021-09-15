package com.github.fit51.reactiveconfig.parser

import scala.util.Try

trait ConfigDecoder[T, ParsedData] {

  /** Decode the given [[ParsedData]] to [[T]]
    */
  def decode(parsed: ParsedData): Try[T]
}

object ConfigDecoder {
  def apply[T, ParsedData](implicit d: ConfigDecoder[T, ParsedData]): ConfigDecoder[T, ParsedData] = d
}

object DecoderOps {
  def decode[T, ParsedData](parsed: ParsedData)(implicit d: ConfigDecoder[T, ParsedData]): Try[T] = d.decode(parsed)
}
