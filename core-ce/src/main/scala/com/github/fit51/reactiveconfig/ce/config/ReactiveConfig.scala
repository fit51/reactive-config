package com.github.fit51.reactiveconfig.ce.config

import cats.{~>, MonadThrow}
import cats.effect.Resource
import cats.syntax.applicativeError._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.ce.reloadable.Reloadable
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Volatile

import scala.util.{Failure, Success}

trait ReactiveConfig[F[_], ParsedData] {

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[T]

  def hasKey(key: String): F[Boolean]

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Resource[F, Reloadable[F, T]]

  def volatile[G[_], T](key: String)(implicit
      decoder: ConfigDecoder[T, ParsedData],
      nat: F ~> G
  ): Resource[F, Volatile[G, T]]
}

object ReactiveConfig {

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  def combine[F[_], D](cfg1: ReactiveConfig[F, D], cfg2: ReactiveConfig[F, D])(implicit
      mt: MonadThrow[F]
  ): ReactiveConfig[F, D] =
    new ReactiveConfig[F, D] {

      override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): F[T] =
        cfg1.get[T](key).handleErrorWith(_ => cfg2.get[T](key))

      override def hasKey(key: String): F[Boolean] =
        cfg1.hasKey(key).flatMap {
          case true  => mt.pure(true)
          case false => cfg2.hasKey(key)
        }

      override def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, D]): Resource[F, Reloadable[F, T]] =
        Resource.eval(cfg1.hasKey(key)) >>= {
          case true =>
            cfg1.reloadable(key)
          case false =>
            cfg2.reloadable(key)
        }

      override def volatile[G[_], T](key: String)(implicit
          decoder: ConfigDecoder[T, D],
          nat: F ~> G
      ): Resource[F, Volatile[G, T]] =
        reloadable(key).flatMap(_.makeVolatile(nat))
    }

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {

    def apply[D](map: Map[String, D])(implicit mt: MonadThrow[F]): ReactiveConfig[F, D] =
      new ReactiveConfig[F, D] {

        override def get[T](key: String)(implicit decoder: ConfigDecoder[T, D]): F[T] =
          mt.fromEither(for {
            value <- map.get(key).toRight(ReactiveConfigException.unknownKey(key))
            parsed <- decoder.decode(value) match {
              case Success(value) =>
                Right(value)
              case Failure(exception) =>
                Left(ReactiveConfigException.unableToParse(key, exception))
            }
          } yield parsed)

        override def hasKey(key: String): F[Boolean] =
          mt.pure(map.contains(key))

        override def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, D]): Resource[F, Reloadable[F, T]] =
          Resource.eval(get[T](key).map(v => Reloadable.const[F](v)))

        override def volatile[G[_], T](key: String)(implicit
            decoder: ConfigDecoder[T, D],
            nat: F ~> G
        ): Resource[F, Volatile[G, T]] =
          reloadable(key).flatMap(_.makeVolatile(nat))
      }
  }
}
