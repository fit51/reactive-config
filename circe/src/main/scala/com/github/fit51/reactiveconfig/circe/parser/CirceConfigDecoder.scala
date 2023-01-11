package com.github.fit51.reactiveconfig.circe.parser

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.typesafe.scalalogging.StrictLogging
import io.circe.{Decoder, Json}

import scala.util.{Failure, Try}

object CirceConfigDecoder {

  implicit def circeConfigDecoder[T: Decoder]: ConfigDecoder[T, Json] =
    new CirceConfigDecoder[T]()
}

class CirceConfigDecoder[T](implicit d: Decoder[T]) extends ConfigDecoder[T, Json] with StrictLogging {
  override def decode(parsed: Json): Try[T] =
    d.decodeJson(parsed).toTry.recoverWith { case e =>
      logger.error(s"Unable to decode json: $parsed", e)
      Failure(ReactiveConfigException.unableToParse("Unable to parse json", e))
    }
}
