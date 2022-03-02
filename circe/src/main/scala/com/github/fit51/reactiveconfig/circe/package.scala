package com.github.fit51.reactiveconfig

import com.github.fit51.reactiveconfig.circe.parser.{CirceConfigDecoder, CirceConfigParser}
import com.github.fit51.reactiveconfig.parser.{ConfigDecoder, ConfigParser}
import io.circe.{Decoder, Json}

package object circe {

  implicit val circeConfigParser: ConfigParser[Json] =
    CirceConfigParser.parser

  implicit def circeConfigDecoder[T: Decoder]: ConfigDecoder[T, Json] =
    CirceConfigDecoder.circeConfigDecoder
}
