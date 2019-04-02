package com.github.fit51.reactiveconfig.typesafe

import java.io.FileNotFoundException
import java.nio.file.{Files, Path, WatchEvent}
import java.util

import better.files.File
import cats.MonadError
import cats.effect.Sync
import cats.syntax.option._
import com.github.fit51.reactiveconfig.{ParsedKeyValue, Value}
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.storage.ConfigStorage
import com.typesafe.config._
import com.typesafe.scalalogging.LazyLogging
import monix.execution.Scheduler
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject
import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.util.{Failure, Success}

object TypesafeConfigStorage {

  /**
    * Creates new ConfigStorage for TypesafeConfig fetching data from HOCON file.
    * Note: Were use here [[Json]] as type parameter for [[ConfigParser]]
    * Keep in mind, that, internally, storage renders HOCON to JSON and passes it to ConfigParser
    * You have to provide Json Parser to [[TypesafeConfigStorage]]
    **/
  def apply[F[_]: Sync, Json](path: Path)(
      implicit error: MonadError[F, Throwable],
      s: Scheduler,
      encoder: ConfigParser[Json]
  ): TypesafeConfigStorage[F, Json] =
    new TypesafeConfigStorage[F, Json](path)
}

class TypesafeConfigStorage[F[_]: Sync, Json](path: Path)(implicit s: Scheduler, encoder: ConfigParser[Json])
    extends ConfigStorage[F, Json] with LazyLogging {

  private val storage: TrieMap[String, Value[Json]] = TrieMap.empty
  private val fileWatch: Observable[WatchEvent.Kind[Path]] =
    FileWatch.watch(File(path.getParent), PublishSubject[WatchEvent.Kind[Path]])

  def load(): F[TrieMap[String, Value[Json]]] =
    if (!Files.exists(path))
      MonadError[F, Throwable].raiseError(new FileNotFoundException(path.toString))
    else
      Sync[F].delay {
        ConfigFactory.invalidateCaches()
        ConfigFactory
          .parseFile(File(path).toJava, ConfigParseOptions.defaults())
          .resolve(ConfigResolveOptions.defaults())
          .root()
          .entrySet()
          .asScala
          .foldLeft(storage)(
            (storage, entry) => flattenHoconToJsonMap(entry.getKey, entry, parseHoconEntryToJson(entry), 0L, storage)
          )
      }

  def watch(): Observable[ParsedKeyValue[Json]] =
    fileWatch.flatMap { _ =>
      Observable.fromIterable {
        ConfigFactory.invalidateCaches()
        ConfigFactory
          .parseFile(File(path).toJava, ConfigParseOptions.defaults())
          .resolve(ConfigResolveOptions.defaults())
          .root()
          .entrySet()
          .asScala
          .map { entry =>
            val maybeJson = parseHoconEntryToJson(entry)
            if (maybeJson.exists(json => storage.get(entry.getKey).exists(_.parsedData == json)))
              None
            else
              flattenHoconToJsonMap(
                key = entry.getKey,
                entry = entry,
                maybeJson = maybeJson,
                revision = storage.get(entry.getKey).map(_.version).getOrElse(0L) + 1,
                storage = mutable.Map.empty[String, Value[Json]]
              ).map(kv => ParsedKeyValue(kv._1, kv._2)).some
          }
          .collect { case Some(kvs) => kvs }
          .flatten
      }
    }

  private def flattenHoconToJsonMap[T <: mutable.Map[String, Value[Json]]](
      key: String,
      entry: util.Map.Entry[String, ConfigValue],
      maybeJson: Option[Json],
      revision: Long,
      storage: T
  ): T = {
    maybeJson.foreach(json => storage.update(key, Value(json, revision)))
    entry.getValue match {
      case co: ConfigObject =>
        co.entrySet().asScala.foldLeft(storage) { (storage, subEntry) =>
          flattenHoconToJsonMap(
            s"$key.${subEntry.getKey}",
            subEntry,
            parseHoconEntryToJson(subEntry),
            revision,
            storage
          )
        }
      case _ => storage
    }
  }

  private def parseHoconEntryToJson(entry: util.Map.Entry[String, ConfigValue]): Option[Json] =
    ConfigParser[Json].parse(entry.getValue.render(ConfigRenderOptions.concise())) match {
      case Success(value) => Some(value)
      case Failure(th) =>
        logger.error(s"Error occurred while trying to parse config entry with key ${entry.getKey}: ${th.getMessage}")
        None
    }
}
