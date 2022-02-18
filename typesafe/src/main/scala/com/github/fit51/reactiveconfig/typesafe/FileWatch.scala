package com.github.fit51.reactiveconfig.typesafe

import java.nio.file.{Path, WatchEvent, StandardWatchEventKinds => EventType}

import cats.effect.Blocker
import cats.effect.Resource
import cats.effect.Sync
import better.files.{File, FileMonitor}
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject

object FileWatch {

  def watch[F[_]: Sync](
      file: File,
      blocker: Blocker
  ): Resource[F, Observable[WatchEvent.Kind[Path]]] =
    Resource
      .make(Sync[F].delay {
        val output = PublishSubject[WatchEvent.Kind[Path]]()
        val monitor = new FileMonitor(file) {
          override def onEvent(eventType: WatchEvent.Kind[Path], file: File, count: Int): Unit = eventType match {
            case EventType.ENTRY_CREATE => output.onNext(EventType.ENTRY_CREATE)
            case EventType.ENTRY_MODIFY => output.onNext(EventType.ENTRY_MODIFY)
            case EventType.ENTRY_DELETE => output.onNext(EventType.ENTRY_DELETE)
          }
        }
        monitor.start()(blocker.blockingContext)
        (monitor, output)
      })(tuple =>
        Sync[F].delay {
          tuple._1.close()
          tuple._2.onComplete()
        }
      ).map(_._2)
}
