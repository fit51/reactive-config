package com.github.fit51.reactiveconfig.circe.parser

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.typesafe.scalalogging.StrictLogging
import io.circe._

import scala.util.{Failure, Try}

object CirceConfigParser {
  implicit val parser: ConfigParser[Json] = new CirceConfigParser
}

class CirceConfigParser extends ConfigParser[Json] with StrictLogging {
  override def parse(rawData: String): Try[Json] =
    parser.parse(rawData).toTry.recoverWith { case e =>
      logger.error(s"Unable to parse json from String: $rawData", e)
      Failure(ReactiveConfigException.unableToParse("Unable to parse json", e))
    }
}
