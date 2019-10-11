// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.kv

/** @param key
  *   key is the key in bytes. An empty key is not allowed.
  * @param createRevision
  *   create_revision is the revision of last creation on this key.
  * @param modRevision
  *   mod_revision is the revision of last modification on this key.
  * @param version
  *   version is the version of the key. A deletion resets
  *   the version to zero and any modification of the key
  *   increases its version.
  * @param value
  *   value is the value held by the key, in bytes.
  * @param lease
  *   lease is the ID of the lease that attached to key.
  *   When the attached lease expires, the key will be deleted.
  *   If lease is 0, then no lease is attached to the key.
  */
@SerialVersionUID(0L)
final case class KeyValue(
    key: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY,
    createRevision: _root_.scala.Long = 0L,
    modRevision: _root_.scala.Long = 0L,
    version: _root_.scala.Long = 0L,
    value: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY,
    lease: _root_.scala.Long = 0L
    ) extends scalapb.GeneratedMessage with scalapb.Message[KeyValue] with scalapb.lenses.Updatable[KeyValue] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = key
        if (__value != _root_.com.google.protobuf.ByteString.EMPTY) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(1, __value)
        }
      };
      
      {
        val __value = createRevision
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(2, __value)
        }
      };
      
      {
        val __value = modRevision
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(3, __value)
        }
      };
      
      {
        val __value = version
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(4, __value)
        }
      };
      
      {
        val __value = value
        if (__value != _root_.com.google.protobuf.ByteString.EMPTY) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(5, __value)
        }
      };
      
      {
        val __value = lease
        if (__value != 0L) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(6, __value)
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
        val __v = key
        if (__v != _root_.com.google.protobuf.ByteString.EMPTY) {
          _output__.writeBytes(1, __v)
        }
      };
      {
        val __v = createRevision
        if (__v != 0L) {
          _output__.writeInt64(2, __v)
        }
      };
      {
        val __v = modRevision
        if (__v != 0L) {
          _output__.writeInt64(3, __v)
        }
      };
      {
        val __v = version
        if (__v != 0L) {
          _output__.writeInt64(4, __v)
        }
      };
      {
        val __v = value
        if (__v != _root_.com.google.protobuf.ByteString.EMPTY) {
          _output__.writeBytes(5, __v)
        }
      };
      {
        val __v = lease
        if (__v != 0L) {
          _output__.writeInt64(6, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue = {
      var __key = this.key
      var __createRevision = this.createRevision
      var __modRevision = this.modRevision
      var __version = this.version
      var __value = this.value
      var __lease = this.lease
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 10 =>
            __key = _input__.readBytes()
          case 16 =>
            __createRevision = _input__.readInt64()
          case 24 =>
            __modRevision = _input__.readInt64()
          case 32 =>
            __version = _input__.readInt64()
          case 42 =>
            __value = _input__.readBytes()
          case 48 =>
            __lease = _input__.readInt64()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue(
          key = __key,
          createRevision = __createRevision,
          modRevision = __modRevision,
          version = __version,
          value = __value,
          lease = __lease
      )
    }
    def withKey(__v: _root_.com.google.protobuf.ByteString): KeyValue = copy(key = __v)
    def withCreateRevision(__v: _root_.scala.Long): KeyValue = copy(createRevision = __v)
    def withModRevision(__v: _root_.scala.Long): KeyValue = copy(modRevision = __v)
    def withVersion(__v: _root_.scala.Long): KeyValue = copy(version = __v)
    def withValue(__v: _root_.com.google.protobuf.ByteString): KeyValue = copy(value = __v)
    def withLease(__v: _root_.scala.Long): KeyValue = copy(lease = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = key
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
        case 2 => {
          val __t = createRevision
          if (__t != 0L) __t else null
        }
        case 3 => {
          val __t = modRevision
          if (__t != 0L) __t else null
        }
        case 4 => {
          val __t = version
          if (__t != 0L) __t else null
        }
        case 5 => {
          val __t = value
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
        case 6 => {
          val __t = lease
          if (__t != 0L) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PByteString(key)
        case 2 => _root_.scalapb.descriptors.PLong(createRevision)
        case 3 => _root_.scalapb.descriptors.PLong(modRevision)
        case 4 => _root_.scalapb.descriptors.PLong(version)
        case 5 => _root_.scalapb.descriptors.PByteString(value)
        case 6 => _root_.scalapb.descriptors.PLong(lease)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue
}

object KeyValue extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue(
      __fieldsMap.getOrElse(__fields.get(0), _root_.com.google.protobuf.ByteString.EMPTY).asInstanceOf[_root_.com.google.protobuf.ByteString],
      __fieldsMap.getOrElse(__fields.get(1), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(2), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(3), 0L).asInstanceOf[_root_.scala.Long],
      __fieldsMap.getOrElse(__fields.get(4), _root_.com.google.protobuf.ByteString.EMPTY).asInstanceOf[_root_.com.google.protobuf.ByteString],
      __fieldsMap.getOrElse(__fields.get(5), 0L).asInstanceOf[_root_.scala.Long]
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue(
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).map(_.as[_root_.scala.Long]).getOrElse(0L),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).map(_.as[_root_.scala.Long]).getOrElse(0L)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = KvProto.javaDescriptor.getMessageTypes.get(0)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = KvProto.scalaDescriptor.messages(0)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = throw new MatchError(__fieldNumber)
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue(
    key = _root_.com.google.protobuf.ByteString.EMPTY,
    createRevision = 0L,
    modRevision = 0L,
    version = 0L,
    value = _root_.com.google.protobuf.ByteString.EMPTY,
    lease = 0L
  )
  implicit class KeyValueLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue](_l) {
    def key: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.key)((c_, f_) => c_.copy(key = f_))
    def createRevision: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.createRevision)((c_, f_) => c_.copy(createRevision = f_))
    def modRevision: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.modRevision)((c_, f_) => c_.copy(modRevision = f_))
    def version: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.version)((c_, f_) => c_.copy(version = f_))
    def value: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.value)((c_, f_) => c_.copy(value = f_))
    def lease: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.lease)((c_, f_) => c_.copy(lease = f_))
  }
  final val KEY_FIELD_NUMBER = 1
  final val CREATE_REVISION_FIELD_NUMBER = 2
  final val MOD_REVISION_FIELD_NUMBER = 3
  final val VERSION_FIELD_NUMBER = 4
  final val VALUE_FIELD_NUMBER = 5
  final val LEASE_FIELD_NUMBER = 6
  def of(
    key: _root_.com.google.protobuf.ByteString,
    createRevision: _root_.scala.Long,
    modRevision: _root_.scala.Long,
    version: _root_.scala.Long,
    value: _root_.com.google.protobuf.ByteString,
    lease: _root_.scala.Long
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue = _root_.com.github.fit51.reactiveconfig.etcd.gen.kv.KeyValue(
    key,
    createRevision,
    modRevision,
    version,
    value,
    lease
  )
}
