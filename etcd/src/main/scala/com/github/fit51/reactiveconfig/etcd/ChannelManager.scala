package com.github.fit51.reactiveconfig.etcd

import java.net.URI

import com.coreos.jetcd.resolver.URIResolverLoader
import com.typesafe.scalalogging.StrictLogging
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener
import io.grpc.Status.Code
import io.grpc._
import io.grpc.netty.{GrpcSslContexts, NettyChannelBuilder}
import io.netty.handler.ssl.ClientAuth
import javax.net.ssl.TrustManagerFactory
import com.github.fit51.reactiveconfig.etcd.gen.rpc.{AuthGrpc, AuthenticateRequest, AuthenticateResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object ChannelManager {

  /**
    * @param endpoints Format: https://host1.d.c:2379,https://host2.d.c:2379
    * @return
    */
  def noAuth(
      endpoints: String,
      authority: Option[String] = None,
      trustManagerFactory: Option[TrustManagerFactory] = None
  )(implicit exec: ExecutionContext): ChannelManager = {
    val uris = endpoints.split(',').map(new URI(_)).toList
    new ChannelManager(uris, authority, trustManagerFactory)
  }

  def apply(
      endpoints: String,
      credential: Credentials,
      authority: Option[String] = None,
      trustManagerFactory: Option[TrustManagerFactory] = None
  )(implicit exec: ExecutionContext): ChannelManager with Authorization = {
    val uris = endpoints.split(',').map(new URI(_)).toList
    new ChannelManager(uris, authority, trustManagerFactory) with Authorization {
      override val credentials = credential
    }
  }
}

/**
  *
  * @param uris
  *                  Connection URI. Ex: https://host1.d.c:2379,https://host2.d.c:2379
  * @param authority
  *                  Authority enables TLS, Certificate provided by Server should contain Authority as CN or SAN
  * @param exec
  */
class ChannelManager(
    uris: List[URI],
    authority: Option[String],
    tmf: Option[TrustManagerFactory]
)(implicit val exec: ExecutionContext)
    extends StrictLogging {

  protected[etcd] def channelBuilder = {
    val builder = NettyChannelBuilder
      .forTarget("etcd")
      .nameResolverFactory(
        new SmartNameResolverFactory(uris, authority.getOrElse("etcd"), URIResolverLoader.defaultLoader)
      )
      .defaultLoadBalancingPolicy("pick_first")

    if (authority.isDefined)
      builder.sslContext(getSslContext)
    else
      builder.usePlaintext()
  }

  protected def getSslContext() = {
    val sslContext = GrpcSslContexts
      .forClient()
      .clientAuth(ClientAuth.REQUIRE)
    (tmf match {
      case Some(ca) => sslContext.trustManager(ca)
      case None     => sslContext
    }) build
  }

  lazy val channel = channelBuilder.build()
}

trait Authorization extends ChannelManager {

  val credentials: Credentials

  override protected[etcd] def channelBuilder =
    super.channelBuilder
      .intercept(new AuthTokenInterceptor)

  @volatile private var token: Option[String] = None
  private lazy val api                        = AuthGrpc.blockingStub(channel)
  private lazy val apiAsync                   = AuthGrpc.stub(channel)

  def authenticate: Option[String] = {
    extractToken(Try {
      api.authenticate(
        AuthenticateRequest(credentials.user, credentials.password)
      )
    }).toOption
  }

  def authenticateAsync: Future[String] = {
    apiAsync
      .authenticate(
        AuthenticateRequest(credentials.user, credentials.password)
      )
      .transform[String](extractToken _)
  }

  private def extractToken(resp: Try[AuthenticateResponse]) = resp match {
    case Success(r) =>
      val t = r.token
      logger.info(s"ETCD: Got token $t")
      token = Some(t)
      Success(t)
    case Failure(e) =>
      logger.error(s"ETCD: Error authenticating", e)
      Failure(e)
  }

  private def isInvalidTokenError(status: Status) =
    status.getCode == Code.UNAUTHENTICATED && status.getDescription == "etcdserver: invalid auth token"

  private def isServerUnavaliable(status: Status) =
    status.getCode == Code.UNAVAILABLE

  protected class AuthTokenInterceptor extends ClientInterceptor {
    private val TOKEN      = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER)
    private val authMethod = "etcdserverpb.Auth/Authenticate"

    override def interceptCall[ReqT, RespT](
        method: MethodDescriptor[ReqT, RespT],
        callOptions: CallOptions,
        next: Channel
    ) =
      new SimpleForwardingClientCall[ReqT, RespT](next.newCall(method, callOptions)) {

        override def start(responseListener: ClientCall.Listener[RespT], headers: Metadata): Unit = {
          if (method.getFullMethodName != authMethod)
            token.orElse(authenticate).map(t => headers.put(TOKEN, t))
          super.start(
            new SimpleForwardingClientCallListener[RespT](responseListener) {
              override def onClose(status: Status, trailers: Metadata) = {
                if (isInvalidTokenError(status)) {
                  logger.warn("ETCD: Invalid token")
                  authenticateAsync
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
  }
}
