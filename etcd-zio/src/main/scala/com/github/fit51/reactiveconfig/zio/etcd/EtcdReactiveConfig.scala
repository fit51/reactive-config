package com.github.fit51.reactiveconfig.zio.etcd

import cats.data.NonEmptySet
import cats.syntax.foldable._
import com.github.fit51.reactiveconfig.Value
import com.github.fit51.reactiveconfig.config.ConfigState
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.config.Utils
import com.github.fit51.reactiveconfig.etcd.gen.kv.Event.EventType
import com.github.fit51.reactiveconfig.etcd.gen.rpc._
import com.github.fit51.reactiveconfig.etcd.gen.rpc.ZioRpc._
import com.github.fit51.reactiveconfig.parser.ConfigParser
import com.github.fit51.reactiveconfig.zio.config.AbstractReactiveConfig
import com.github.fit51.reactiveconfig.zio.reloadable.Reloadable._
import io.grpc.Status
import zio._
import zio.stream.{Stream, ZStream}

import scala.util.{Failure, Success}

class EtcdReactiveConfig[D](stateRef: Ref.Synchronized[ConfigState[UIO, D]]) extends AbstractReactiveConfig[D](stateRef)

object EtcdReactiveConfig {

  def live[D: ConfigParser: Tag](
      prefixes: NonEmptySet[String],
      retryDuration: Duration
  ): ZLayer[KVClient & WatchClient, ReactiveEtcdConfigException, EtcdReactiveConfig[D]] =
    ZLayer.fromZIO(
      for {
        _           <- ZIO.fail(new IntersectionError(prefixes)).when(Utils.doIntersect(prefixes))
        kvClient    <- ZIO.service[KVClient]
        watchClient <- ZIO.service[WatchClient]
        stateRef <- loadInitials(prefixes, kvClient)
          .map(ConfigState[UIO, D](_, Map.empty)).flatMap(Ref.Synchronized.make(_))
        _ <- prefixes.foldLeft[Stream[GrpcError, WatchResponse]](ZStream.empty) { case (acc, prefix) =>
          acc.merge(watchKey(watchClient, prefix, retryDuration))
        } foreach (handleUpdate(_, stateRef)) fork
      } yield new EtcdReactiveConfig(stateRef)
    )

  private def loadInitials[D](
      prefixes: NonEmptySet[String],
      kvClient: KVClient
  )(implicit decoder: ConfigParser[D]): IO[GrpcError, Map[String, Value[D]]] =
    ZIO.foreachPar(prefixes.toNonEmptyList.toList.toVector) { key =>
      val keyRange = key.asKeyRange
      kvClient
        .range(
          RangeRequest(
            key = keyRange.start.bytes,
            rangeEnd = keyRange.end.bytes
          )
        )
        .mapError(e => new GrpcError(e.getStatus)).map(_.kvs.toVector)
    } map (_.flatten) map { allKvs =>
      allKvs.partitionEither { kv =>
        decoder.parse(kv.value.utf8) match {
          case Success(parsed) =>
            val key   = kv.key.utf8
            val value = Value(parsed, kv.version)
            Right(key -> value)
          case Failure(exception) =>
            Left(kv.key.utf8 -> exception.getMessage)
        }
      }
    } flatMap { case (errors, parsedKvs) =>
      ZIO.foreach(errors) { case (key, message) =>
        ZIO.logWarning(s"Unable to parse key $key: $message")
      } as parsedKvs.toMap
    }

  private def watchKey(
      watchClient: WatchClient,
      key: String,
      retry: Duration
  ): Stream[GrpcError, WatchResponse] = {
    val keyRange = key.asKeyRange
    watchClient.watch(
      ZStream(
        WatchRequest(
          WatchRequest.RequestUnion.CreateRequest(
            WatchCreateRequest(
              key = keyRange.start.bytes,
              rangeEnd = keyRange.end.bytes
            )
          )
        )
      )
    ) catchAll { error =>
      val statusCode = error.getStatus
      if (statusCode.getCode == Status.Code.UNAVAILABLE) {
        ZStream.execute(ZIO.logInfo(s"Retrying watch for $key")) ++
        ZStream.execute(ZIO.sleep(retry)) ++
        watchKey(watchClient, key, retry)
      } else {
        ZStream.execute(ZIO.logWarning(s"Unrecoverable error: ${statusCode.getCode}")) ++ ZStream.fail(
          new GrpcError(statusCode)
        )
      }
    }
  }

  private def handleUpdate[D](
      update: WatchResponse,
      stateRef: Ref.Synchronized[ConfigState[UIO, D]]
  )(implicit decoder: ConfigParser[D]): UIO[Unit] =
    if (update.created) {
      ZIO.logInfo(s"Subscribed on updates with ${update.watchId}")
    } else if (update.canceled) {
      ZIO.logInfo(s"Etcd watch ${update.watchId} cancelled")
    } else {
      ZIO.foreachDiscard(for {
        event <- update.events
        kv    <- event.kv
      } yield (event.`type` == EventType.PUT, kv)) {
        case (true, kv) =>
          val key = kv.key.utf8
          decoder.parse(kv.value.utf8) match {
            case Success(parsed) =>
              stateRef.updateZIO { state =>
                val newValue = Value(parsed, kv.version)
                state.fireUpdate(key, newValue).as(state.updateValue(key, newValue))
              }
            case Failure(exception) =>
              ZIO.logWarningCause(s"Unable to parse key $key", Cause.fail(exception))
          }
        case (false, kv) =>
          stateRef.update(_.removeKey(kv.value.utf8))
      }
    }
}

sealed abstract class ReactiveEtcdConfigException(message: String) extends Exception(message)

final class IntersectionError(prefixes: NonEmptySet[String])
    extends ReactiveEtcdConfigException("Prefixes should not intersect")

final class GrpcError(status: Status) extends ReactiveEtcdConfigException(s"GRPC exception $status")
