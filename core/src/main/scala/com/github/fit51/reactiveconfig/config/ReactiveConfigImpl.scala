package com.github.fit51.reactiveconfig.config

import cats.MonadError
import cats.effect.Resource
import monix.eval.TaskLift
import monix.eval.TaskLike
import monix.execution.Scheduler
import monix.reactive.Observable
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.{ParsedKeyValue, ReactiveConfigException, Value}
import com.github.fit51.reactiveconfig.reloadable._

import scala.collection.concurrent.TrieMap
import scala.util.{Failure, Success, Try}

private class ReactiveConfigImpl[F[_], ParsedData](
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

  override def reloadable[T](
      key: String
  )(implicit decoder: ConfigDecoder[T, ParsedData]): Resource[F, Reloadable[F, T]] =
    for {
      initial <- Resource.liftF(get(key))
      observable = watch.filter(_.key == key).map(kv => decoder.decode(kv.value.parsedData)).collect {
        case Success(v) => v
      }
      result <- Reloadable(initial, observable, None, Some(key))
    } yield result
}
