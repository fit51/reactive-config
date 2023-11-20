package com.github.fit51.reactiveconfig.circe

import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import io.circe._

import scala.util.{Failure, Success}

trait SensitiveDecoderTransformer {

  implicit def sensitiveDecoderConverter(implicit
      sensitiveConfigDecoder: ConfigDecoder[Sensitive, Json]
  ): Decoder[Sensitive] =
    (cursor: HCursor) =>
      sensitiveConfigDecoder.decode(cursor.value) match {
        case Success(value) => Right(value)
        case Failure(e)     => Left(DecodingFailure.fromThrowable(e, Nil))
      }
}
