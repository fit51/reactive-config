package com.github.fit51.reactiveconfig.ce.generic

import cats.effect.{ContextShift, IO}
import com.github.fit51.reactiveconfig.Sensitive
import com.github.fit51.reactiveconfig.generic.Configuration
import com.github.fit51.reactiveconfig.parser.ConfigDecoder

import scala.concurrent.ExecutionContext
import scala.util.{Success, Try}

object Decoders {

  implicit val intDecoder: ConfigDecoder[Int, String] =
    s => Try(s.toInt)

  implicit val booleanDecoder: ConfigDecoder[Boolean, String] =
    s => Try(s.toBoolean)

  implicit val doubleDecoder: ConfigDecoder[Double, String] =
    s => Try(s.toDouble)

  implicit val stringDecoder: ConfigDecoder[String, String] =
    ConfigDecoder.identity

  val sensitiveDecoder: ConfigDecoder[Sensitive, String] =
    s => Success(Sensitive(s * 2))

  implicit val plainDecoder: ConfigDecoder[Plain, String] =
    s => {
      val Array(rawInt, rawBoolean, rawDouble) = s.split(":")
      Success(Plain(rawInt.toInt, rawBoolean.toBoolean, rawDouble.toDouble))
    }

  implicit val reactiveConfigConfiguration: Configuration =
    Configuration.default.withSnakeCaseMemberNames
}

object instances {

  implicit val ioContextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.Implicits.global)
}
