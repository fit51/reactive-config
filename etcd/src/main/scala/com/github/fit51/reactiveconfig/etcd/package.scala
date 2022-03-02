package com.github.fit51.reactiveconfig

import com.google.protobuf.ByteString

package object etcd {

  val nullByte = "\u0000"
  val maxByte  = "\uFFFF"

  final case class KeyRange(start: String, end: String)

  implicit class StringOps(private val s: String) extends AnyVal {

    def bytes: ByteString =
      ByteString.copyFromUtf8(s)

    def asKeyRange: KeyRange =
      if (s.isEmpty)
        KeyRange(nullByte, maxByte)
      else
        KeyRange(s, s + maxByte)
  }

  implicit class ByteStringOps(private val bs: ByteString) extends AnyVal {

    def utf8: String = bs.toStringUtf8
  }
}
