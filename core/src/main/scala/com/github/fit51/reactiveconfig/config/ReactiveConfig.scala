package com.github.fit51.reactiveconfig.config

import cats.MonadError
import cats.effect.Resource
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.reloadable.Reloadable
import com.github.fit51.reactiveconfig.storage.ConfigStorage
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler

import scala.util.Try

trait ReactiveConfig[F[_], ParsedData] { self =>
  def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T]

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[T]

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Resource[F, Reloadable[F, T]]
}

object ReactiveConfig {

  /** Creates new instance of Config using arbitrary F for reloading.
    */
  def apply[F[_]: TaskLike: TaskLift, ParsedData](
      configStorage: ConfigStorage[F, ParsedData]
  )(implicit s: Scheduler, F: MonadError[F, Throwable]): Resource[F, ReactiveConfig[F, ParsedData]] =
    for {
      storage    <- Resource.liftF(configStorage.load)
      observable <- configStorage.watch
    } yield new ReactiveConfigImpl(storage, observable)

  def const[F[_]]: ConstBuilder[F] = new ConstBuilder[F]

  final class ConstBuilder[F[_]](val dummy: Boolean = true) extends AnyVal {

    def apply[Data](map: Map[String, Data])(implicit me: MonadError[F, Throwable]): ReactiveConfig[F, Data] =
      new ConstConfig[F, Data](map)
  }
}
