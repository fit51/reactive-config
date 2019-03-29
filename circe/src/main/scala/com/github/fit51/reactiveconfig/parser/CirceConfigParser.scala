package com.github.fit51.reactiveconfig.parser

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.typesafe.scalalogging.StrictLogging
import scala.util.{Failure, Try}
import io.circe._

object CirceConfigParser {
  implicit lazy val parser: ConfigParser[Json] = new CirceConfigParser
}

class CirceConfigParser extends ConfigParser[Json] with StrictLogging {
  override def parse(rawData: String): Try[Json] =
    parser.parse(rawData).toTry.recoverWith {
      case e =>
        logger.error(s"Unable to parse json from String: $rawData", e)
        Failure(ReactiveConfigException(e.getMessage))
    }
}
