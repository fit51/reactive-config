// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.rpc

/** @param iD
  *   ID is the lease ID to revoke. When the ID is revoked, all associated keys will be deleted.
  */
@SerialVersionUID(0L)
final case class LeaseRevokeRequest(
    iD: _root_.scala.Long = 0L
    ) extends scalapb.GeneratedMessage with scalapb.Message[LeaseRevokeRequest] with scalapb.lenses.Updatable[LeaseRevokeRequest] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = iD
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(1, __value)
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
      {
        val __v = iD
        if (__v != 0L) {
          _output__.writeInt64(1, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest = {
      var __iD = this.iD
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __iD = _input__.readInt64()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest(
          iD = __iD
      )
    }
    def withID(__v: _root_.scala.Long): LeaseRevokeRequest = copy(iD = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = iD
          if (__t != 0L) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PLong(iD)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest
}

object LeaseRevokeRequest extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest(
      __fieldsMap.getOrElse(__fields.get(0), 0L).asInstanceOf[_root_.scala.Long]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Long]).getOrElse(0L)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RpcProto.javaDescriptor.getMessageTypes.get(24)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RpcProto.scalaDescriptor.messages(24)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest(
    iD = 0L
  )
  implicit class LeaseRevokeRequestLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest](_l) {
    def iD: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.iD)((c_, f_) => c_.copy(iD = f_))
  }
  final val ID_FIELD_NUMBER = 1
  def of(
    iD: _root_.scala.Long
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest = _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest(
    iD
  )
}
