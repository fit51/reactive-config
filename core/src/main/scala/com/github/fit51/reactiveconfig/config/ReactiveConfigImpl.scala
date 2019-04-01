package com.github.fit51.reactiveconfig.config

import cats.MonadError
import cats.syntax.all._
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
  def apply[F[_]: TaskLike, ParsedData](configStorage: ConfigStorage[F, ParsedData])(
      implicit s: Scheduler,
      F: MonadError[F, Throwable]): F[ReactiveConfigImpl[F, ParsedData]] =
    configStorage.load().map(storage => new ReactiveConfigImpl[F, ParsedData](storage, configStorage.watch()))
}

class ReactiveConfigImpl[F[_], ParsedData](
    storage: TrieMap[String, Value[ParsedData]],
    watch: Observable[ParsedKeyValue[ParsedData]])(implicit s: Scheduler, F: MonadError[F, Throwable], T: TaskLike[F])
    extends ReactiveConfig[F, ParsedData] {

  def get[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Option[T] = getTry(key).toOption

  def getOrThrow[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): T = getTry(key).get

  def getTry[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Try[T] =
    storage.get(key) match {
      case Some(v) => decoder.decode(v.parsedData)
      case None    => Failure(ReactiveConfigException(key, "Failed to find ValueByKey"))
    }

  def reloadable[T](key: String)(implicit decoder: ConfigDecoder[T, ParsedData]): Reloadable[F, T, T] =
    ReloadableImpl[F, T, T](
      initial = getOrThrow[T](key),
      start = F.pure,
      ob = watch
        .filter(_.key == key)
        .map(kv => decoder.decode(kv.value.parsedData))
        .collect { case Success(v) => v }
    )
}
