package com.github.fit51.reactiveconfig.config

import cats.syntax.either._
import cats.syntax.monoid._
import com.github.fit51.reactiveconfig.{ReactiveConfigException, Value}
import com.github.fit51.reactiveconfig.parser.ConfigDecoder
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.typeclasses.Effect._

import scala.util.{Failure, Success}

final case class Listener[F[_], D, T](
    decoder: ConfigDecoder[T, D],
    callback: T => F[Unit]
) {
  def apply(rawValue: D)(implicit eff: Effect[F]): F[Unit] =
    decoder.decode(rawValue) match {
      case Success(value) => callback(value)
      case Failure(e)     => eff.warn(s"Unable to parse value $rawValue", e)
    }
}

final case class ConfigState[F[_], D](
    values: Map[String, Value[D]],
    listeners: Map[String, List[Listener[F, D, _]]]
) {

  def extractValue[T](key: String)(implicit decoder: ConfigDecoder[T, D]): Either[ReactiveConfigException, T] =
    values
      .get(key)
      .map(_.parsedData)
      .toRight(ReactiveConfigException.unknownKey(key))
      .flatMap(
        decoder
          .decode(_)
          .toEither
          .leftMap(ReactiveConfigException.unableToParse(s"Unable to parse JSON for key $key", _))
      )

  def updateValue(key: String, value: Value[D]): ConfigState[F, D] =
    copy(values = values + (key -> value))

  def removeKey(key: String): ConfigState[F, D] =
    copy(values = values - key)

  def addListener(key: String, listener: Listener[F, D, _]): ConfigState[F, D] =
    copy(listeners = listeners |+| Map(key -> List(listener)))

  def removeListener(key: String, listener: Listener[F, D, _]): ConfigState[F, D] = {
    val newListeners = listeners.get(key) match {
      case Some(keyListeners) =>
        keyListeners.filter(_ != listener) match {
          case Nil      => listeners - key
          case nonEmpty => listeners.updated(key, nonEmpty)
        }
      case None =>
        listeners
    }
    copy(listeners = newListeners)
  }

  def fireUpdate(key: String, newValue: Value[D])(implicit eff: Effect[F]): F[Unit] =
    listeners.get(key) match {
      case Some(keyListeners) if values.get(key).forall(_.isUpdated(newValue)) =>
        eff.info(s"Updating key $key") *> Effect.traverse(keyListeners)(_.apply(newValue.parsedData)).fireAndForget
      case _ =>
        eff.unit
    }

  def fireUpdates(newMap: Map[String, Value[D]])(implicit eff: Effect[F]): F[Unit] =
    Effect.traverse(newMap) { case (key, newValue) =>
      fireUpdate(key, newValue)
    }
}
