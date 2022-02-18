package com.github.fit51.reactiveconfig.config

import cats.MonadError
import com.github.fit51.reactiveconfig.ReactiveConfigException
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Reloadable

import scala.util.{Failure, Try}
import cats.effect.Resource

private class ConstConfig[F[_]: MonadError[*[_], Throwable], Data](data: Map[String, Data])
    extends ReactiveConfig[F, Data] {

  override def get[T](key: String)(implicit decoder: ConfigDecoder[T, Data]): F[T] =
    MonadError[F, Throwable].fromTry(unsafeGet(key))

  override def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, Data]): Try[T] =
    data.get(key) match {
      case Some(value) => decoder.decode(value)
      case None        => Failure(ReactiveConfigException(key, "Failed to find value by key"))
    }

  override def reloadable[T](
      key: String
  )(implicit decoder: ConfigDecoder[T, Data]): Resource[F, Reloadable[F, T]] =
    Resource.liftF(get[T](key)).map(Reloadable.const[F](_))
}
