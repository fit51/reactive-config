package com.github.fit51.reactiveconfig.config

import cats.~>
import cats.MonadError
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import monix.eval.TaskLike
import monix.eval.TaskLift

import scala.util.Try

trait ReactiveConfig[F[_], ParsedData] { self =>
  def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T]

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[T]

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[Reloadable[F, T]]

  def mapK[G[_]: TaskLike: TaskLift: MonadError[*[_], Throwable]](transform: F ~> G): ReactiveConfig[G, ParsedData] =
    new ReactiveConfig[G, ParsedData] {
      override def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T] =
        self.unsafeGet(key)

      override def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): G[T] =
        transform(self.get(key))

      override def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): G[Reloadable[G, T]] =
        for {
          reloadableF <- transform(self.reloadable(key))
          reloadableG <- reloadableF.mapK
        } yield reloadableG
    }
}
