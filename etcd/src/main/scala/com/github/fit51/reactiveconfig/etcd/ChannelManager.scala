package com.github.fit51.reactiveconfig.etcd

import java.net.URI
import java.time.Clock
import java.util.concurrent.TimeUnit

import com.github.fit51.reactiveconfig.etcd.auth.gen.rpc.{AuthenticateRequest, AuthenticateResponse, AuthGrpc}
import com.google.common.net.HostAndPort
import com.typesafe.scalalogging.StrictLogging
import io.grpc._
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import io.grpc.NameResolver.Args
import io.grpc.Status.Code
import io.grpc.netty.{GrpcSslContexts, NettyChannelBuilder}
import io.netty.channel.ChannelOption
import io.netty.handler.ssl.ClientAuth
import javax.net.ssl.TrustManagerFactory
import pdi.jwt.{Jwt, JwtOptions}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object ChannelManager {

  /** @param endpoints
    *   Format: https://host1.d.c:2379,https://host2.d.c:2379
    * @return
    */
  def noAuth(
      endpoints: String,
      options: ChannelOptions,
      authority: Option[String] = None,
      trustManagerFactory: Option[TrustManagerFactory] = None
  )(implicit exec: ExecutionContext): ChannelManager =
    new ChannelManager(endpoints, None, options, authority, trustManagerFactory)

  def apply(
      endpoints: String,
      credentials: Credentials,
      options: ChannelOptions,
      authority: Option[String] = None,
      trustManagerFactory: Option[TrustManagerFactory] = None
  )(implicit exec: ExecutionContext): ChannelManager = {
    val uris = endpoints
      .split(',')
      .map(new URI(_))
      .map(e => e.getHost() + (if (e.getPort() != -1) s":${e.getPort()}" else ""))
      .mkString(",")
    val target = s"etcd://${authority.getOrElse("etcd")}/$uris"
    new ChannelManager(target, Some(credentials), options, authority, trustManagerFactory)
  }

  NameResolverRegistry
    .getDefaultRegistry().register(new NameResolverProvider() {
      override def newNameResolver(targetUri: URI, args: Args): NameResolver = {
        val addresses = targetUri
          .getPath()
          .split(",")
          .map(_.trim())
          .map(a => if (a.startsWith("/")) a.substring(1) else a)
          .map(HostAndPort.fromString)
          .toList
          .asJava
        new MultipleAddressesResolver(targetUri, addresses)
      }

      override def getDefaultScheme(): String =
        "etcd"

      override protected def isAvailable(): Boolean =
        true

      override protected def priority(): Int =
        Int.MinValue
    })
}

/** @param uris
  *   Connection URI. Ex: https://host1.d.c:2379,https://host2.d.c:2379
  * @param authority
  *   Authority enables TLS, Certificate provided by Server should contain Authority as CN or SAN
  * @param exec
  */
class ChannelManager(
    target: String,
    mbCredentials: Option[Credentials],
    options: ChannelOptions,
    authority: Option[String],
    tmf: Option[TrustManagerFactory]
)(implicit val exec: ExecutionContext)
    extends StrictLogging {

  def channelBuilder = {

    /** In current version 1.22.3 of grcp-netty ManagedChannelImpl would automatically reconnect with exponential
      * backoff Stub Api would fail and we have to retry it ourselves.
      */
    val builder0 = NettyChannelBuilder
      .forTarget(target)
      .defaultLoadBalancingPolicy("pick_first")
      .keepAliveTime(options.keepAliveTime.toSeconds, TimeUnit.SECONDS)
      .keepAliveTimeout(options.keepAliveTimeout.toSeconds, TimeUnit.SECONDS)
      .withOption[Integer](ChannelOption.CONNECT_TIMEOUT_MILLIS, options.connectTimeout.toMillis.toInt)

    val builder1 =
      if (authority.isDefined)
        builder0.sslContext(getSslContext())
      else
        builder0.usePlaintext()

    mbCredentials match {
      case Some(credentials) =>
        builder1.intercept(new AuthTokenInterceptor(credentials))
      case None =>
        builder1
    }
  }

  protected def getSslContext() = {
    val sslContext = GrpcSslContexts.forClient().clientAuth(ClientAuth.REQUIRE)
    (tmf match {
      case Some(ca) => sslContext.trustManager(ca)
      case None     => sslContext
    }) build
  }

  lazy val channel = channelBuilder.build()
}

class AuthTokenInterceptor(
    credentials: Credentials
)(implicit ec: ExecutionContext)
    extends ClientInterceptor
    with StrictLogging {

  private val TokenKey   = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER)
  private val authMethod = "etcdserverpb.Auth/Authenticate"

  @volatile private var token: Option[Token] = None

  val clock = Clock.systemUTC()

  override def interceptCall[ReqT, RespT](
      method: MethodDescriptor[ReqT, RespT],
      callOptions: CallOptions,
      next: Channel
  ) =
    new SimpleForwardingClientCall[ReqT, RespT](next.newCall(method, callOptions)) {

      override def start(responseListener: ClientCall.Listener[RespT], headers: Metadata): Unit = {
        if (method.getFullMethodName != authMethod) {
          (token match {
            case Some(SimpleToken(value))                             => Some(value)
            case Some(JwtToken(value, claim)) if claim.isValid(clock) => Some(value)
            case _ =>
              authenticate(AuthGrpc.blockingStub(next))
          }).foreach(headers.put(TokenKey, _))
        }
        super.start(
          new SimpleForwardingClientCallListener[RespT](responseListener) {
            override def onClose(status: Status, trailers: Metadata) = {
              if (isInvalidTokenError(status)) {
                logger.warn("ETCD: Invalid token")
                authenticateAsync(AuthGrpc.stub(next))
              }
              if (isServerUnavaliable(status)) {
                token = None
              }
              super.onClose(status, trailers)
            }
          },
          headers
        )
      }
    }

  private def isInvalidTokenError(status: Status) =
    status.getCode == Code.UNAUTHENTICATED && status.getDescription == "etcdserver: invalid auth token"

  private def isServerUnavaliable(status: Status) =
    status.getCode == Code.UNAVAILABLE

  def authenticate(api: AuthGrpc.AuthBlockingStub): Option[String] =
    extractToken(Try {
      api.authenticate(AuthenticateRequest(credentials.user, credentials.password))
    }).toOption

  def authenticateAsync(api: AuthGrpc.AuthStub): Future[String] =
    api.authenticate(AuthenticateRequest(credentials.user, credentials.password)).transform[String](extractToken _)

  private def extractToken(resp: Try[AuthenticateResponse]) = resp match {
    case Success(r) =>
      val tokenStr = r.token
      val t = Jwt.decode(tokenStr, JwtOptions(signature = false)) match {
        case Success(claim) =>
          logger.info("ETCD: Got jwt token")
          JwtToken(tokenStr, claim)
        case _ =>
          logger.info("ETCD: Got simple token")
          SimpleToken(tokenStr)
      }
      token = Some(t)
      Success(tokenStr)
    case Failure(e) =>
      logger.error(s"ETCD: Error authenticating", e)
      Failure(e)
  }
}
