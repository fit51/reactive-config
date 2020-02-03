package com.github.fit51.reactiveconfig.etcd

import cats.data.NonEmptySet
import cats.effect.{Async, ContextShift}
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import monix.execution.Scheduler
import monix.reactive.Observable
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import com.github.fit51.reactiveconfig.etcd.EtcdUtils._
import com.github.fit51.reactiveconfig.{ParsedKeyValue, Value}
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.storage.ConfigStorage

import scala.collection.concurrent.TrieMap
import scala.util.{Failure, Success}

object EtcdConfigStorage {

  /**
    * Creates new ConfigStorage for etcd fetching data wrapped in arbitrary F
    **/
  def apply[F[_]: Async: ContextShift, Json](
      etcd: EtcdClient[F] with Watch[F],
      prefixes: NonEmptySet[String]
  )(implicit s: Scheduler, encoder: ConfigParser[Json]): EtcdConfigStorage[F, Json] =
    new EtcdConfigStorage[F, Json](etcd, prefixes)
}

class EtcdConfigStorage[F[_]: Async: ContextShift, ParsedData](
    etcd: EtcdClient[F] with Watch[F],
    prefixes: NonEmptySet[String]
)(
    implicit s: Scheduler,
    encoder: ConfigParser[ParsedData]
) extends ConfigStorage[F, ParsedData] with LazyLogging {
  @volatile
  private var revision                                    = 0L
  private val storage: TrieMap[String, Value[ParsedData]] = TrieMap.empty

  override def load(): F[TrieMap[String, Value[ParsedData]]] =
    prefixes.toList.traverse { prefix =>
      etcd
        .getRecursiveSinceRevision(prefix, revision)
        .map {
          case (kvs, rev) =>
            logger.info(s"EtcdConfig: Updated to rev: $rev")
            kvs.foreach(saveKeyValue(_, checkVersions = true))
        }
    }.flatMap(_ => storage.pure[F])

  override def watch(): F[Observable[ParsedKeyValue[ParsedData]]] =
    prefixes.toList
      .map(prefix => etcd.watch(EtcdUtils.getRange(prefix)))
      .sequence
      .map(obs => Observable.fromIterable(obs).merge)
      .map { merged =>
        val hotObservable = merged
          .map(saveKeyValue(_))
          .collect { case Some(value) => value }
          .publish
        hotObservable.connect()
        hotObservable
      }

  private def saveKeyValue(kv: KeyValue, checkVersions: Boolean = false): Option[ParsedKeyValue[ParsedData]] =
    encoder.parse(kv.value.utf8) match {
      case Failure(failure) =>
        logger.error(s"Key ${kv.key.utf8}. ${failure.getMessage()}")
        None
      case Success(parsed) =>
        val key   = kv.key.utf8
        val value = Value(parsed, kv.version)
        if (checkVersions) {
          storage
            .get(key)
            .map { storedValue =>
              if (storedValue.version < value.version)
                storage.put(key, value)
            }
            .getOrElse(storage.put(key, value))
        } else {
          storage.update(key, value)
        }
        Some(ParsedKeyValue[ParsedData](key, value))
    }
}
