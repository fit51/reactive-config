// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.rpc

/** @param iD
  *   ID is the member ID for this member.
  * @param name
  *   name is the human-readable name of the member. If the member is not started, the name will be an empty string.
  * @param peerURLs
  *   peerURLs is the list of URLs the member exposes to the cluster for communication.
  * @param clientURLs
  *   clientURLs is the list of URLs the member exposes to clients for communication. If the member is not started, clientURLs will be empty.
  */
@SerialVersionUID(0L)
final case class Member(
    iD: _root_.scala.Long = 0L,
    name: _root_.scala.Predef.String = "",
    peerURLs: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty,
    clientURLs: _root_.scala.Seq[_root_.scala.Predef.String] = _root_.scala.Seq.empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[Member] with scalapb.lenses.Updatable[Member] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = iD
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeUInt64Size(1, __value)
        }
      };
      
      {
        val __value = name
        if (__value != "") {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(2, __value)
        }
      };
      peerURLs.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(3, __value)
      }
      clientURLs.foreach { __item =>
        val __value = __item
        __size += _root_.com.google.protobuf.CodedOutputStream.computeStringSize(4, __value)
      }
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
          _output__.writeUInt64(1, __v)
        }
      };
      {
        val __v = name
        if (__v != "") {
          _output__.writeString(2, __v)
        }
      };
      peerURLs.foreach { __v =>
        val __m = __v
        _output__.writeString(3, __m)
      };
      clientURLs.foreach { __v =>
        val __m = __v
        _output__.writeString(4, __m)
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.rpc.Member = {
      var __iD = this.iD
      var __name = this.name
      val __peerURLs = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Predef.String] ++= this.peerURLs)
      val __clientURLs = (_root_.scala.collection.immutable.Vector.newBuilder[_root_.scala.Predef.String] ++= this.clientURLs)
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __iD = _input__.readUInt64()
          case 18 =>
            __name = _input__.readString()
          case 26 =>
            __peerURLs += _input__.readString()
          case 34 =>
            __clientURLs += _input__.readString()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Member(
          iD = __iD,
          name = __name,
          peerURLs = __peerURLs.result(),
          clientURLs = __clientURLs.result()
      )
    }
    def withID(__v: _root_.scala.Long): Member = copy(iD = __v)
    def withName(__v: _root_.scala.Predef.String): Member = copy(name = __v)
    def clearPeerURLs = copy(peerURLs = _root_.scala.Seq.empty)
    def addPeerURLs(__vs: _root_.scala.Predef.String*): Member = addAllPeerURLs(__vs)
    def addAllPeerURLs(__vs: Iterable[_root_.scala.Predef.String]): Member = copy(peerURLs = peerURLs ++ __vs)
    def withPeerURLs(__v: _root_.scala.Seq[_root_.scala.Predef.String]): Member = copy(peerURLs = __v)
    def clearClientURLs = copy(clientURLs = _root_.scala.Seq.empty)
    def addClientURLs(__vs: _root_.scala.Predef.String*): Member = addAllClientURLs(__vs)
    def addAllClientURLs(__vs: Iterable[_root_.scala.Predef.String]): Member = copy(clientURLs = clientURLs ++ __vs)
    def withClientURLs(__v: _root_.scala.Seq[_root_.scala.Predef.String]): Member = copy(clientURLs = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = iD
          if (__t != 0L) __t else null
        }
        case 2 => {
          val __t = name
          if (__t != "") __t else null
        }
        case 3 => peerURLs
        case 4 => clientURLs
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PLong(iD)
        case 2 => _root_.scalapb.descriptors.PString(name)
        case 3 => _root_.scalapb.descriptors.PRepeated(peerURLs.iterator.map(_root_.scalapb.descriptors.PString).toVector)
        case 4 => _root_.scalapb.descriptors.PRepeated(clientURLs.iterator.map(_root_.scalapb.descriptors.PString).toVector)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Member
}

object Member extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.Member] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.Member] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.rpc.Member = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.rpc.Member(
      __fieldsMap.getOrElse(__fields.get(0), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(1), "").asInstanceOf[_root_.scala.Predef.String],
      __fieldsMap.getOrElse(__fields.get(2), Nil).asInstanceOf[_root_.scala.Seq[_root_.scala.Predef.String]],
      __fieldsMap.getOrElse(__fields.get(3), Nil).asInstanceOf[_root_.scala.Seq[_root_.scala.Predef.String]]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.rpc.Member] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Member(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Predef.String]).getOrElse(""),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Seq[_root_.scala.Predef.String]]).getOrElse(_root_.scala.Seq.empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RpcProto.javaDescriptor.getMessageTypes.get(30)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RpcProto.scalaDescriptor.messages(30)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.rpc.Member(
    iD = 0L,
    name = "",
    peerURLs = _root_.scala.Seq.empty,
    clientURLs = _root_.scala.Seq.empty
  )
  implicit class MemberLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Member]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Member](_l) {
    def iD: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.iD)((c_, f_) => c_.copy(iD = f_))
    def name: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Predef.String] = field(_.name)((c_, f_) => c_.copy(name = f_))
    def peerURLs: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.peerURLs)((c_, f_) => c_.copy(peerURLs = f_))
    def clientURLs: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Seq[_root_.scala.Predef.String]] = field(_.clientURLs)((c_, f_) => c_.copy(clientURLs = f_))
  }
  final val ID_FIELD_NUMBER = 1
  final val NAME_FIELD_NUMBER = 2
  final val PEERURLS_FIELD_NUMBER = 3
  final val CLIENTURLS_FIELD_NUMBER = 4
  def of(
    iD: _root_.scala.Long,
    name: _root_.scala.Predef.String,
    peerURLs: _root_.scala.Seq[_root_.scala.Predef.String],
    clientURLs: _root_.scala.Seq[_root_.scala.Predef.String]
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.Member = _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.Member(
    iD,
    name,
    peerURLs,
    clientURLs
  )
}
