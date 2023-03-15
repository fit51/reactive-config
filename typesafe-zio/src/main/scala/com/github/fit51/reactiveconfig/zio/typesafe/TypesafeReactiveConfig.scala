package com.github.fit51.reactiveconfig.zio.typesafe

import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds

import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.typesafe.TypesafeUtils
import com.github.fit51.reactiveconfig.zio.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable._
import zio._
import zio.nio.file.{Path => ZPath, WatchService}

class TypesafeReactiveConfig[D](stateRef: Ref.Synchronized[ConfigState[UIO, D]])
    extends AbstractReactiveConfig[D](stateRef)

object TypesafeReactiveConfig {

  def live[D: Tag](path: Path)(implicit encoder: ConfigParser[D]): TaskLayer[TypesafeReactiveConfig[D]] = ZLayer.scoped(
    for {
      watchService <- WatchService.forDefaultFileSystem
      zpath = ZPath(path.getParent().toUri())
      _        <- ZIO.acquireRelease(zpath.register(watchService, List(StandardWatchEventKinds.ENTRY_MODIFY)))(_.cancel)
      stateRef <- parseConfig(path, -1).map(ConfigState[UIO, D](_, Map.empty)).flatMap(Ref.Synchronized.make(_))
      _ <- watchService.stream
        .mapChunksZIO(ZIO.foreach(_)(_.reset).as(Chunk.unit))
        .zipWithIndex
        .mapZIO { case (_, idx) => parseConfig(path, idx).either }
        .foreach {
          case Right(newMap) =>
            stateRef.updateZIO { state =>
              state.fireUpdates(newMap).as(state.copy(values = newMap))
            }
          case Left(e) =>
            ZIO.logWarningCause("Unable to parse config", Cause.fail(e))
        }
        .fork
    } yield new TypesafeReactiveConfig(stateRef)
  )

  private def parseConfig[D: ConfigParser](path: Path, index: Long): Task[Map[String, Value[D]]] =
    ZIO.attempt(TypesafeUtils.parseConfig(path)).flatMap(TypesafeUtils.parseValuesInMap[UIO, D](_, index))
}
