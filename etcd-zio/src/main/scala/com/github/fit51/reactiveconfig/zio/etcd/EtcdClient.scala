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

  val live =
    (for {
      kvClient <- ZIO.service[KVClient.ZService[Any, Any]]
    } yield new EtcdClientImpl(kvClient): EtcdClient).toLayer
}

class EtcdClientImpl(kvClient: KVClient.ZService[Any, Any]) extends EtcdClient {

  override def get(key: String): IO[Status, Option[KeyValue]] =
    kvClient.range(RangeRequest(key.bytes)).map(_.kvs.headOption)

  override def getRange(key: String, limit: Long): IO[Status, (Long, Seq[KeyValue])] = {
    val range = key.asKeyRange
    kvClient
      .range(
        RangeRequest(
          key = range.start.bytes,
          rangeEnd = range.end.bytes,
          limit = limit
        )
      ).map { resp =>
        resp.getHeader.revision -> resp.kvs
      }
  }

  override def getRangeSince(key: String, lastRevision: Long, limit: Long): IO[Status, (Long, Seq[KeyValue])] =
    if (lastRevision == 0L) {
      getRange(key, limit)
    } else {
      val range = key.asKeyRange
      for {
        resp1 <- kvClient.range(
          RangeRequest(
            key = range.start.bytes,
            rangeEnd = range.end.bytes,
            limit = limit,
            minCreateRevision = lastRevision + 1
          )
        )
        newRevision = resp1.getHeader.revision
        resp2 <- kvClient.range(
          RangeRequest(
            key = range.start.bytes,
            rangeEnd = range.end.bytes,
            limit = limit,
            minModRevision = lastRevision + 1,
            maxModRevision = newRevision
          )
        )
      } yield newRevision -> (resp1.kvs ++ resp2.kvs).distinct
    }

  override def put(key: String, value: String): IO[Status, PutResponse] =
    kvClient.put(PutRequest(key.bytes, value.bytes))

  override def delete(key: String): IO[Status, DeleteRangeResponse] =
    kvClient.deleteRange(DeleteRangeRequest(key.bytes))

  override def deleteRange(key: String): IO[Status, DeleteRangeResponse] = {
    val range = key.asKeyRange
    kvClient.deleteRange(DeleteRangeRequest(key = range.start.bytes, rangeEnd = range.end.bytes))
  }
}
