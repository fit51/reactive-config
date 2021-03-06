// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.rpc

/** @param iD
  *   ID is the lease ID from the keep alive request.
  * @param tTL
  *   TTL is the new time-to-live for the lease.
  */
@SerialVersionUID(0L)
final case class LeaseKeepAliveResponse(
    header: _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader] = _root_.scala.None,
    iD: _root_.scala.Long = 0L,
    tTL: _root_.scala.Long = 0L
    ) extends scalapb.GeneratedMessage with scalapb.Message[LeaseKeepAliveResponse] with scalapb.lenses.Updatable[LeaseKeepAliveResponse] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      if (header.isDefined) {
        val __value = header.get
        __size += 1 + _root_.com.google.protobuf.CodedOutputStream.computeUInt32SizeNoTag(__value.serializedSize) + __value.serializedSize
      };
      
      {
        val __value = iD
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(2, __value)
        }
      };
      
      {
        val __value = tTL
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(3, __value)
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
        val __v = iD
        if (__v != 0L) {
          _output__.writeInt64(2, __v)
        }
      };
      {
        val __v = tTL
        if (__v != 0L) {
          _output__.writeInt64(3, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse = {
      var __header = this.header
      var __iD = this.iD
      var __tTL = this.tTL
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __header = Option(_root_.scalapb.LiteParser.readMessage(_input__, __header.getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader.defaultInstance)))
          case 16 =>
            __iD = _input__.readInt64()
          case 24 =>
            __tTL = _input__.readInt64()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse(
          header = __header,
          iD = __iD,
          tTL = __tTL
      )
    }
    def getHeader: com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader = header.getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader.defaultInstance)
    def clearHeader: LeaseKeepAliveResponse = copy(header = _root_.scala.None)
    def withHeader(__v: com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader): LeaseKeepAliveResponse = copy(header = Option(__v))
    def withID(__v: _root_.scala.Long): LeaseKeepAliveResponse = copy(iD = __v)
    def withTTL(__v: _root_.scala.Long): LeaseKeepAliveResponse = copy(tTL = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => header.orNull
        case 2 => {
          val __t = iD
          if (__t != 0L) __t else null
        }
        case 3 => {
          val __t = tTL
          if (__t != 0L) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => header.map(_.toPMessage).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 2 => _root_.scalapb.descriptors.PLong(iD)
        case 3 => _root_.scalapb.descriptors.PLong(tTL)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse
}

object LeaseKeepAliveResponse extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse(
      __fieldsMap.get(__fields.get(0)).asInstanceOf[_root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]],
      __fieldsMap.getOrElse(__fields.get(1), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(2), 0L).asInstanceOf[_root_.scala.Long]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).flatMap(_.as[_root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]]),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Long]).getOrElse(0L)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RpcProto.javaDescriptor.getMessageTypes.get(27)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RpcProto.scalaDescriptor.messages(27)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = {
    var __out: _root_.scalapb.GeneratedMessageCompanion[_] = null
    (__number: @_root_.scala.unchecked) match {
      case 1 => __out = com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader
    }
    __out
  }
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse(
    header = _root_.scala.None,
    iD = 0L,
    tTL = 0L
  )
  implicit class LeaseKeepAliveResponseLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse](_l) {
    def header: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader] = field(_.getHeader)((c_, f_) => c_.copy(header = Option(f_)))
    def optionalHeader: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader]] = field(_.header)((c_, f_) => c_.copy(header = f_))
    def iD: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.iD)((c_, f_) => c_.copy(iD = f_))
    def tTL: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.tTL)((c_, f_) => c_.copy(tTL = f_))
  }
  final val HEADER_FIELD_NUMBER = 1
  final val ID_FIELD_NUMBER = 2
  final val TTL_FIELD_NUMBER = 3
  def of(
    header: _root_.scala.Option[com.github.fit51.reactiveconfig.etcd.gen.rpc.ResponseHeader],
    iD: _root_.scala.Long,
    tTL: _root_.scala.Long
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse = _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse(
    header,
    iD,
    tTL
  )
}
