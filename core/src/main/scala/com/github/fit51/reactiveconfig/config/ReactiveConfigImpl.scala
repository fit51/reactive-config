package com.github.fit51.reactiveconfig.config

import cats.MonadError
import cats.syntax.all._
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler
import monix.reactive.Observable
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.{ParsedKeyValue, ReactiveConfigException, Value}
import com.github.fit51.reactiveconfig.reloadable._
import com.github.fit51.reactiveconfig.storage.ConfigStorage

import scala.collection.concurrent.TrieMap
import scala.util.{Failure, Success, Try}

object ReactiveConfigImpl {

  /**
    * Creates new instance of Config using arbitrary F for reloading.
    **/
  def apply[F[_]: TaskLike: TaskLift, ParsedData](
      configStorage: ConfigStorage[F, ParsedData]
  )(implicit s: Scheduler, F: MonadError[F, Throwable]): F[ReactiveConfig[F, ParsedData]] =
    for {
      storage    <- configStorage.load()
      observable <- configStorage.watch()
    } yield new ReactiveConfigImpl(storage, observable)
}

class ReactiveConfigImpl[F[_], ParsedData](
    storage: TrieMap[String, Value[ParsedData]],
    watch: Observable[ParsedKeyValue[ParsedData]]
)(implicit s: Scheduler, F: MonadError[F, Throwable], T: TaskLike[F], L: TaskLift[F])
    extends ReactiveConfig[F, ParsedData] {

  override def unsafeGet[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T] =
    storage.get(key) match {
      case Some(v) => decoder.decode(v.parsedData)
      case None    => Failure(ReactiveConfigException(key, "Failed to find ValueByKey"))
    }

  override def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[T] =
    storage.get(key) match {
      case Some(v) => F.fromTry(decoder.decode(v.parsedData))
      case None    => F.raiseError(ReactiveConfigException(key, "Failed to find ValueByKey"))
    }

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): F[Reloadable[F, T]] =
    for {
      initial <- get(key)
      observable = watch
        .filter(_.key == key)
        .map(kv => decoder.decode(kv.value.parsedData))
        .collect { case Success(v) => v }
      result <- Reloadable(initial, observable)
    } yield result
}
