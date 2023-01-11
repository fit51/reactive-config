package com.github.fit51.reactiveconfig.zio.typesafe

import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typeclasses.Effect
import com.github.fit51.reactiveconfig.typesafe.TypesafeUtils
import com.github.fit51.reactiveconfig.zio.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable._
import zio._
import zio.nio.file.{Path => ZPath, WatchService}

class TypesafeReactiveConfig[D](stateRef: RefM[ConfigState[UIO, D]]) extends AbstractReactiveConfig[D](stateRef)

object TypesafeReactiveConfig {

  private val uioEffect: Effect[UIO] = Effect[UIO]

  def live[D: Tag](path: Path)(implicit encoder: ConfigParser[D]) = (for {
    watchService <- WatchService.forDefaultFileSystem
    zpath = ZPath(path.getParent().toUri())
    _        <- zpath.register(watchService, List(StandardWatchEventKinds.ENTRY_MODIFY)).toManaged(_.cancel)
    stateRef <- parseConfig(path, -1).toManaged_.map(ConfigState[UIO, D](_, Map.empty)) >>= ZRefM.makeManaged
    _ <- watchService.stream
      .mapChunksM(ZIO.foreach(_)(_.reset).as(Chunk.unit))
      .zipWithIndex
      .mapM { case (_, idx) => parseConfig(path, idx).either }
      .foreach {
        case Right(newMap) =>
          stateRef.update { state =>
            state.fireUpdates(newMap).as(state.copy(values = newMap))
          }
        case Left(e) =>
          uioEffect.warn("Unable to parse config", e)
      }
      .fork
      .toManaged_
  } yield new TypesafeReactiveConfig(stateRef)).toLayer

  private def parseConfig[D: ConfigParser](path: Path, index: Long): Task[Map[String, Value[D]]] =
    Task.effect(TypesafeUtils.parseConfig(path)).flatMap(TypesafeUtils.parseValuesInMap[UIO, D](_, index))
}
