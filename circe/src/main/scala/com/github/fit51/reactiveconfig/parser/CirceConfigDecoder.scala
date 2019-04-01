package com.github.fit51.reactiveconfig.parser

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.typesafe.scalalogging.StrictLogging
import io.circe.{Decoder, Json}
import scala.util.{Failure, Try}

object CirceConfigDecoder {
  implicit def decoder[T](implicit d: Decoder[T]): CirceConfigDecoder[T] =
    new CirceConfigDecoder[T]()
}

class CirceConfigDecoder[T](implicit d: Decoder[T]) extends ConfigDecoder[T, Json] with StrictLogging {
  override def decode(parsed: Json): Try[T] =
    d.decodeJson(parsed).toTry.recoverWith {
      case e =>
        logger.error(s"Unable to decode json: $parsed", e)
        Failure(ReactiveConfigException(e.getMessage))
    }
}
