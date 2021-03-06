// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.rpc

/** @param header
  *   header has the current key-value store information. The first header in the snapshot
  *   stream indicates the point in time of the snapshot.
  * @param remainingBytes
  *   remaining_bytes is the number of blob bytes to be sent after this message
  * @param blob
  *   blob contains the next chunk of the snapshot in the snapshot stream.
  */
@SerialVersionUID(0L)
final case class SnapshotResponse(
    header: _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader] = _root_.scala.None,
    remainingBytes: _root_.scala.Long = 0L,
    blob: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY
    ) extends scalapb.GeneratedMessage with scalapb.Message[SnapshotResponse] with scalapb.lenses.Updatable[SnapshotResponse] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (header.isDefined) {
        val __value = header.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = remainingBytes
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt64Size(2, __value)
        }
      };
      
      {
        val __value = blob
        if (__value != _root_.com.google.protobuf.ByteString.EMPTY) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(3, __value)
        }
      };
      __size
    }
    final override def serializedSize: _root_.scala.Int = {
      var read = __serializedSizeCachedValue
      if (read == 0) {
        read = __computeSerializedValue()
        __serializedSizeCachedValue = read
      }
      read
    }
    def writeTo(`_output__`: _root_.com.google.protobuf.CodedOutputStream): _root_.scala.Unit = {
      header.foreach { __v =>
        val __m = __v
        _output__.writeTag(1, 2)
        _output__.writeUInt32NoTag(__m.serializedSize)
        __m.writeTo(_output__)
      };
      {
        val __v = remainingBytes
        if (__v != 0L) {
          _output__.writeUInt64(2, __v)
        }
      };
      {
        val __v = blob
        if (__v != _root_.com.google.protobuf.ByteString.EMPTY) {
          _output__.writeBytes(3, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse = {
      var __header = this.header
      var __remainingBytes = this.remainingBytes
      var __blob = this.blob
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __header = Option(_root_.scalapb.LiteParser.readMessage(_input__, __header.getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader.defaultInstance)))
          case 16 =>
            __remainingBytes = _input__.readUInt64()
          case 26 =>
            __blob = _input__.readBytes()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse(
          header = __header,
          remainingBytes = __remainingBytes,
          blob = __blob
      )
    }
    def getHeader: com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader = header.getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader.defaultInstance)
    def clearHeader: SnapshotResponse = copy(header = _root_.scala.None)
    def withHeader(__v: com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader): SnapshotResponse = copy(header = Option(__v))
    def withRemainingBytes(__v: _root_.scala.Long): SnapshotResponse = copy(remainingBytes = __v)
    def withBlob(__v: _root_.com.google.protobuf.ByteString): SnapshotResponse = copy(blob = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => header.orNull
        case 2 => {
          val __t = remainingBytes
          if (__t != 0L) __t else null
        }
        case 3 => {
          val __t = blob
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => header.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PLong(remainingBytes)
        case 3 => _root_.scalapb.descriptors.PByteString(blob)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse
}

object SnapshotResponse extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse(
      __fieldsMap.get(__fields.get(0)).asInstanceOf[_root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]],
      __fieldsMap.getOrElse(__fields.get(1), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(2), _root_.com.google.protobuf.ByteString.EMPTY).asInstanceOf[_root_.com.google.protobuf.ByteString]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]]),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RpcProto.javaDescriptor.getMessageTypes.get(17)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RpcProto.scalaDescriptor.messages(17)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse(
    header = _root_.scala.None,
    remainingBytes = 0L,
    blob = _root_.com.google.protobuf.ByteString.EMPTY
  )
  implicit class SnapshotResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse](_l) {
    def header: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader] = field(_.getHeader)((c_, f_) => c_.copy(header = Option(f_)))
    def optionalHeader: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]] = field(_.header)((c_, f_) => c_.copy(header = f_))
    def remainingBytes: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.remainingBytes)((c_, f_) => c_.copy(remainingBytes = f_))
    def blob: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.blob)((c_, f_) => c_.copy(blob = f_))
  }
  final val HEADER_FIELD_NUMBER = 1
  final val REMAINING_BYTES_FIELD_NUMBER = 2
  final val BLOB_FIELD_NUMBER = 3
  def of(
    header: _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader],
    remainingBytes: _root_.scala.Long,
    blob: _root_.com.google.protobuf.ByteString
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse = _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse(
    header,
    remainingBytes,
    blob
  )
}
