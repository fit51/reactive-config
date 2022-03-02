package com.github.fit51.reactiveconfig.ce.etcd

import cats.effect.ConcurrentEffect
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.github.fit51.reactiveconfig.etcd._
import com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
import com.github.fit51.reactiveconfig.etcd.gen.rpc._
import io.grpc.Channel
import io.grpc.Metadata

trait EtcdClient[F[_]] {

  def get(key: String): F[Option[KeyValue]]

  def getRange(key: String, limit: Long): F[(Long, Seq[KeyValue])]

  def getRangeSince(key: String, lastRevision: Long, limit: Long): F[(Long, Seq[KeyValue])]

  def put(key: String, value: String): F[PutResponse]

  def delete(key: String): F[DeleteRangeResponse]

  def deleteRange(key: String): F[DeleteRangeResponse]
}

object EtcdClient {

  def apply[F[_]: ConcurrentEffect](channel: Channel): EtcdClient[F] =
    new EtcdClientImpl[F](channel)
}

class EtcdClientImpl[F[_]: ConcurrentEffect](channel: Channel) extends EtcdClient[F] {

  private val client = KVFs2Grpc.stub[F](channel)

  override def get(key: String): F[Option[KeyValue]] =
    client.range(RangeRequest(key.bytes), new Metadata()).map(_.kvs.headOption)

  override def getRange(key: String, limit: Long): F[(Long, Seq[KeyValue])] = {
    val range = key.asKeyRange
    client
      .range(
        RangeRequest(
          key = range.start.bytes,
          rangeEnd = range.end.bytes,
          limit = limit
        ),
        new Metadata()
      ).map { resp =>
        resp.getHeader.revision -> resp.kvs
      }
  }

  override def getRangeSince(key: String, lastRevision: Long, limit: Long): F[(Long, Seq[KeyValue])] =
    if (lastRevision == 0L) {
      getRange(key, limit)
    } else {
      val range = key.asKeyRange
      for {
        resp1 <- client.range(
          RangeRequest(
            key = range.start.bytes,
            rangeEnd = range.end.bytes,
            limit = limit,
            minCreateRevision = lastRevision + 1
          ),
          new Metadata()
        )
        newRevision = resp1.getHeader.revision
        resp2 <- client.range(
          RangeRequest(
            key = range.start.bytes,
            rangeEnd = range.end.bytes,
            limit = limit,
            minModRevision = lastRevision + 1,
            maxModRevision = newRevision
          ),
          new Metadata()
        )
      } yield newRevision -> (resp1.kvs ++ resp2.kvs).distinct
    }

  override def put(key: String, value: String): F[PutResponse] =
    client.put(PutRequest(key.bytes, value.bytes), new Metadata())

  override def delete(key: String): F[DeleteRangeResponse] =
    client.deleteRange(DeleteRangeRequest(key.bytes), new Metadata())

  override def deleteRange(key: String): F[DeleteRangeResponse] = {
    val range = key.asKeyRange
    client.deleteRange(DeleteRangeRequest(key = range.start.bytes, rangeEnd = range.end.bytes), new Metadata())
  }
}
