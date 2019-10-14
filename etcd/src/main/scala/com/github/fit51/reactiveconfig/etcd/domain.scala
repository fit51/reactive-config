package com.github.fit51.reactiveconfig.etcd

import java.net.URI

import cats.effect.{Async, ContextShift}
import com.google.protobuf.ByteString
import com.coreos.jetcd.resolver.URIResolverLoader
import io.grpc.{Attributes, NameResolver}

import scala.collection.JavaConverters._
import scala.concurrent.Future

case class Credentials(user: String, password: String)

case class KeyRange(start: String, end: String)

object EtcdUtils {
  import scala.language.implicitConversions

  final class FutureLiftOps[F[_]: Async: ContextShift, A](f: Future[A]) {
    def liftToF: F[A] = Async.fromFuture(Async[F].pure(f))
  }
  implicit def futureSyntaxLift[F[_]: Async: ContextShift, A](fa: Future[A]) = new FutureLiftOps[F, A](fa)

  implicit class BytesToString(s: String) {
    def bytes: ByteString = ByteString.copyFromUtf8(s)
  }
  implicit class StringToBytes(b: ByteString) {
    def utf8: String = b.toStringUtf8
  }

  def getRange(key: String): KeyRange =
    if (key.isEmpty)
      KeyRange(nullByte, maxByte)
    else
      KeyRange(key, key + maxByte)

  val nullByte = "\u0000"
  val maxByte  = "\uFFFF"
}

class EtcdException(m: String) extends Exception(m)

class SmartNameResolverFactory(uris: List[URI], authority: String, loader: URIResolverLoader)
    extends NameResolver.Factory {
  override def getDefaultScheme: String = "etcd"

  override def newNameResolver(targetUri: URI, params: Attributes): NameResolver =
    new SmartNameResolver(authority, uris.asJava, loader)
}
