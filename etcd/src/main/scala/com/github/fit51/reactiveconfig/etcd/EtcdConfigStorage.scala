package com.github.fit51.reactiveconfig.etcd
import cats.effect.{Async, ContextShift}
import cats.syntax.all._
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
      prefix: String
  )(implicit s: Scheduler, encoder: ConfigParser[Json]): EtcdConfigStorage[F, Json] =
    new EtcdConfigStorage[F, Json](etcd, prefix)
}

class EtcdConfigStorage[F[_]: Async: ContextShift, Json](etcd: EtcdClient[F] with Watch[F], prefix: String)(
    implicit s: Scheduler,
    encoder: ConfigParser[Json]
) extends ConfigStorage[F, Json] with LazyLogging {
  @volatile
  private var revision                              = 0L
  private val storage: TrieMap[String, Value[Json]] = TrieMap.empty

  def load(): F[TrieMap[String, Value[Json]]] =
    etcd
      .getRecursiveSinceRevision(prefix, revision)
      .map {
        case (kvs, rev) =>
          revision = rev
          logger.info(s"EtcdConfig: Updated to rev: $rev")
          kvs.foreach(saveKeyValue(_, checkVersions = true))
          storage
      }

  def watch(): Observable[ParsedKeyValue[Json]] =
    Observable
      .fromTask(etcd.watch(EtcdUtils.getRange(prefix)))
      .flatten
      .map(saveKeyValue(_))
      .collect { case Some(value) => value }

  private def saveKeyValue(kv: KeyValue, checkVersions: Boolean = false): Option[ParsedKeyValue[Json]] =
    encoder.parse(kv.value.utf8) match {
      case Failure(failure) =>
        logger.error(s"Key ${kv.key.utf8}. ${failure.getMessage()}")
        None
      case Success(parsed) =>
        val key   = kv.key.utf8
        val value = Value(parsed, kv.version)
        if (checkVersions) {
          storage.get(key).map { storedValue =>
            if (storedValue.version < value.version)
              storage.put(key, value)
          }
        } else
          storage.update(key, value)
        Some(ParsedKeyValue[Json](key, value))
    }
}
