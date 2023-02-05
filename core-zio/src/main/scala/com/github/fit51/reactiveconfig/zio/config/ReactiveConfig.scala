package com.github.fit51.reactiveconfig.zio.config

import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable
import zio._

import scala.util.{Failure, Success}

trait ReactiveConfig[ParsedData] {

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): IO[ReactiveConfigException, T]

  def hasKey(key: String): UIO[Boolean]

  def reloadable[T](key: String)(implicit
      decoder: ConfigDecoder[T, ParsedData]
  ): ZIO[Scope, ReactiveConfigException, Reloadable[T]]
}

object ReactiveConfig {

  def const[D](map: Map[String, D]): ReactiveConfig[D] =
    new ReactiveConfig[D] {

      override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): IO[ReactiveConfigException, T] =
        ZIO.fromEither(for {
          value <- map.get(key).toRight(ReactiveConfigException.unknownKey(key))
          parsed <- decoder.decode(value) match {
            case Success(value) =>
              Right(value)
            case Failure(exception) =>
              Left(ReactiveConfigException.unableToParse(key, exception))
          }
        } yield parsed)

      override def hasKey(key: String): UIO[Boolean] =
        ZIO.succeed(map.contains(key))

      override def reloadable[T](
          key: String
      )(implicit decoder: ConfigDecoder[T, D]): ZIO[Scope, ReactiveConfigException, Reloadable[T]] =
        get(key).map(Reloadable.const)
    }

  def combine[D](cfg1: ReactiveConfig[D], cfg2: ReactiveConfig[D]): ReactiveConfig[D] =
    new ReactiveConfig[D] {

      override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): IO[ReactiveConfigException, T] =
        cfg1.get(key).orElse(cfg2.get(key))

      override def hasKey(key: String): UIO[Boolean] =
        ZIO.ifZIO(cfg1.hasKey(key))(ZIO.succeed(true), cfg2.hasKey(key))

      override def reloadable[T](key: String)(implicit
          decoder: ConfigDecoder[T, D]
      ): ZIO[Scope, ReactiveConfigException, Reloadable[T]] =
        ZIO.ifZIO(cfg1.hasKey(key))(cfg1.reloadable(key), cfg2.reloadable(key))
    }
}
