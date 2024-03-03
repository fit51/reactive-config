package com.github.fit51.reactiveconfig.zio.etcd

import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import com.github.fit51.reactiveconfig.etcd.gen.rpc._
import com.github.fit51.reactiveconfig.etcd.gen.rpc.ZioRpc._
import io.grpc.Status
import zio._

trait EtcdClient {

  def get(key: String): IO[Status, Option[KeyValue]]

  def getRange(key: String, limit: Long): IO[Status, (Long, Seq[KeyValue])]

  def getRangeSince(key: String, lastRevision: Long, limit: Long): IO[Status, (Long, Seq[KeyValue])]

  def put(key: String, value: String): IO[Status, PutResponse]

  def delete(key: String): IO[Status, DeleteRangeResponse]

  def deleteRange(key: String): IO[Status, DeleteRangeResponse]
}

object EtcdClient {

  val live: RLayer[KVClient, EtcdClient] =
    ZLayer.fromFunction(EtcdClientImpl.apply _)
}

private case class EtcdClientImpl(kvClient: KVClient) extends EtcdClient {

  override def get(key: String): IO[Status, Option[KeyValue]] =
    kvClient.range(RangeRequest(key.bytes)).mapError(_.getStatus).map(_.kvs.headOption)

  override def getRange(key: String, limit: Long): IO[Status, (Long, Seq[KeyValue])] = {
    val range = key.asKeyRange
    kvClient
      .range(
        RangeRequest(
          key = range.start.bytes,
          rangeEnd = range.end.bytes,
          limit = limit
        )
      )
      .mapError(_.getStatus)
      .map { resp =>
        resp.getHeader.revision -> resp.kvs
      }
  }

  override def getRangeSince(key: String, lastRevision: Long, limit: Long): IO[Status, (Long, Seq[KeyValue])] =
    if (lastRevision == 0L) {
      getRange(key, limit)
    } else {
      val range = key.asKeyRange
      for {
        resp1 <- kvClient
          .range(
            RangeRequest(
              key = range.start.bytes,
              rangeEnd = range.end.bytes,
              limit = limit,
              minCreateRevision = lastRevision + 1
            )
          ).mapError(_.getStatus)
        newRevision = resp1.getHeader.revision
        resp2 <- kvClient
          .range(
            RangeRequest(
              key = range.start.bytes,
              rangeEnd = range.end.bytes,
              limit = limit,
              minModRevision = lastRevision + 1,
              maxModRevision = newRevision
            )
          ).mapError(_.getStatus)
      } yield newRevision -> (resp1.kvs ++ resp2.kvs).distinct
    }

  override def put(key: String, value: String): IO[Status, PutResponse] =
    kvClient.put(PutRequest(key.bytes, value.bytes)).mapError(_.getStatus)

  override def delete(key: String): IO[Status, DeleteRangeResponse] =
    kvClient.deleteRange(DeleteRangeRequest(key.bytes)).mapError(_.getStatus)

  override def deleteRange(key: String): IO[Status, DeleteRangeResponse] = {
    val range = key.asKeyRange
    kvClient.deleteRange(DeleteRangeRequest(key = range.start.bytes, rangeEnd = range.end.bytes)).mapError(_.getStatus)
  }
}
