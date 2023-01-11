package com.github.fit51.reactiveconfig.etcd

import pdi.jwt.JwtClaim

import scala.concurrent.duration._

case class ChannelOptions(
    // Is disabled by default, if so default os socket timeout would cause channel failure (20 seconds)
    // Min value is 10 seconds
    keepAliveTime: FiniteDuration, // = GrpcUtil.KEEPALIVE_TIME_NANOS_DISABLED nanos,
    // Min value is 10 seconds
    keepAliveTimeout: FiniteDuration = 20 seconds,
    // ChannelOption.CONNECT_TIMEOUT_MILLIS let's us configure reconnect timeout.
    connectTimeout: FiniteDuration = 30.seconds
)

case class Credentials(user: String, password: String)

sealed trait Token {
  def value: String
}

final case class SimpleToken(override val value: String) extends Token

final case class JwtToken(override val value: String, claim: JwtClaim) extends Token
