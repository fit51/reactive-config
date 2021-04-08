package com.github.fit51.reactiveconfig.typesafe

import java.nio.file.{Path, WatchEvent, StandardWatchEventKinds => EventType}

import cats.effect.Blocker
import better.files.{File, FileMonitor}
import monix.reactive.Observable
import monix.reactive.subjects.PublishSubject

object FileWatch {

  def watch(
      file: File,
      output: PublishSubject[WatchEvent.Kind[Path]],
      blocker: Blocker
  ): Observable[WatchEvent.Kind[Path]] = {
    val monitor = new FileMonitor(file) {
      override def onEvent(eventType: WatchEvent.Kind[Path], file: File, count: Int): Unit = eventType match {
        case EventType.ENTRY_CREATE => output.onNext(EventType.ENTRY_CREATE)
        case EventType.ENTRY_MODIFY => output.onNext(EventType.ENTRY_MODIFY)
        case EventType.ENTRY_DELETE => output.onNext(EventType.ENTRY_DELETE)
      }
    }
    monitor.start()(blocker.blockingContext)
    output
  }
}
