package com.github.fit51.reactiveconfig.typesafe

import java.nio.file.Path
import java.util

import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.typesafe.config._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

object TypesafeUtils {

  def parseConfig[Data](path: Path)(implicit encoder: ConfigParser[Data]): Map[String, Try[Data]] = {
    ConfigFactory.invalidateCaches()

    ConfigFactory
      .parseFile(path.toFile(), ConfigParseOptions.defaults())
      .resolve(ConfigResolveOptions.defaults())
      .root()
      .entrySet()
      .asScala
      .foldLeft(Map.empty[String, Try[Data]]) { (acc, entry) =>
        val mbData = parseHoconEntryToJson(entry)
        flattenHoconToJsonMap(
          key = entry.getKey,
          entry = entry,
          maybeJson = mbData,
          storage = acc
        )
      }
  }

  private def parseHoconEntryToJson[Data](
      entry: util.Map.Entry[String, ConfigValue]
  )(implicit encoder: ConfigParser[Data]): Try[Data] =
    encoder.parse(entry.getValue.render(ConfigRenderOptions.concise()))

  private def flattenHoconToJsonMap[Data](
      key: String,
      entry: util.Map.Entry[String, ConfigValue],
      maybeJson: Try[Data],
      storage: Map[String, Try[Data]]
  )(implicit encoder: ConfigParser[Data]): Map[String, Try[Data]] = {
    val updatedStorage = storage + (key -> maybeJson)
    entry.getValue match {
      case co: ConfigObject =>
        co.entrySet().asScala.foldLeft(updatedStorage) { (storage, subEntry) =>
          flattenHoconToJsonMap(
            s"$key.${subEntry.getKey}",
            subEntry,
            parseHoconEntryToJson(subEntry),
            storage
          )
        }
      case _ => updatedStorage
    }
  }

  def parseValuesInMap[F[_], D](map: Map[String, Try[D]], index: Long)(implicit
      encoder: ConfigParser[D],
      eff: Effect[F]
  ): F[Map[String, Value[D]]] = {
    import com.github.fit51.reactiveconfig.typeclasses.Effect._
    val errors =
      map.iterator.collect { case (key, Failure(e)) => key -> e }.foldLeft(eff.unit) { case (acc, (key, e)) =>
        eff.warn(s"Unable to parse key $key", e)
      }
    val parsed = map.collect { case (key, Success(d)) => key -> Value(d, index) }
    errors.as(parsed)
  }
}
