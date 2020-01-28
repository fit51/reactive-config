// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package com.github.fit51.reactiveconfig.etcd.gen.rpc

/** @param result
  *   result is logical comparison operation for this comparison.
  * @param target
  *   target is the key-value field to inspect for the comparison.
  * @param key
  *   key is the subject key for the comparison operation.
  * @param rangeEnd
  *   range_end compares the given target to all keys in the range [key, range_end).
  *   See RangeRequest for more details on key ranges.
  *   TODO: fill out with most of the rest of RangeRequest fields when needed.
  */
@SerialVersionUID(0L)
final case class Compare(
    result: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL,
    target: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION,
    key: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY,
    rangeEnd: _root_.com.google.protobuf.ByteString = _root_.com.google.protobuf.ByteString.EMPTY,
    targetUnion: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Empty
    ) extends scalapb.GeneratedMessage with scalapb.Message[Compare] with scalapb.lenses.Updatable[Compare] {
    @transient
    private[this] var __serializedSizeCachedValue: _root_.scala.Int = 0
    private[this] def __computeSerializedValue(): _root_.scala.Int = {
      var __size = 0
      
      {
        val __value = result
        if (__value != com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(1, __value.value)
        }
      };
      
      {
        val __value = target
        if (__value != com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeEnumSize(2, __value.value)
        }
      };
      
      {
        val __value = key
        if (__value != _root_.com.google.protobuf.ByteString.EMPTY) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(3, __value)
        }
      };
      if (targetUnion.version.isDefined) {
        val __value = targetUnion.version.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(4, __value)
      };
      if (targetUnion.createRevision.isDefined) {
        val __value = targetUnion.createRevision.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(5, __value)
      };
      if (targetUnion.modRevision.isDefined) {
        val __value = targetUnion.modRevision.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeInt64Size(6, __value)
      };
      if (targetUnion._value.isDefined) {
        val __value = targetUnion._value.get
        __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(7, __value)
      };
      
      {
        val __value = rangeEnd
        if (__value != _root_.com.google.protobuf.ByteString.EMPTY) {
          __size += _root_.com.google.protobuf.CodedOutputStream.computeBytesSize(8, __value)
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
        val __v = result
        if (__v != com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL) {
          _output__.writeEnum(1, __v.value)
        }
      };
      {
        val __v = target
        if (__v != com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION) {
          _output__.writeEnum(2, __v.value)
        }
      };
      {
        val __v = key
        if (__v != _root_.com.google.protobuf.ByteString.EMPTY) {
          _output__.writeBytes(3, __v)
        }
      };
      targetUnion.version.foreach { __v =>
        val __m = __v
        _output__.writeInt64(4, __m)
      };
      targetUnion.createRevision.foreach { __v =>
        val __m = __v
        _output__.writeInt64(5, __m)
      };
      targetUnion.modRevision.foreach { __v =>
        val __m = __v
        _output__.writeInt64(6, __m)
      };
      targetUnion._value.foreach { __v =>
        val __m = __v
        _output__.writeBytes(7, __m)
      };
      {
        val __v = rangeEnd
        if (__v != _root_.com.google.protobuf.ByteString.EMPTY) {
          _output__.writeBytes(8, __v)
        }
      };
    }
    def mergeFrom(`_input__`: _root_.com.google.protobuf.CodedInputStream): com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare = {
      var __result = this.result
      var __target = this.target
      var __key = this.key
      var __rangeEnd = this.rangeEnd
      var __targetUnion = this.targetUnion
      var _done__ = false
      while (!_done__) {
        val _tag__ = _input__.readTag()
        _tag__ match {
          case 0 => _done__ = true
          case 8 =>
            __result = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.fromValue(_input__.readEnum())
          case 16 =>
            __target = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.fromValue(_input__.readEnum())
          case 26 =>
            __key = _input__.readBytes()
          case 32 =>
            __targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Version(_input__.readInt64())
          case 40 =>
            __targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.CreateRevision(_input__.readInt64())
          case 48 =>
            __targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.ModRevision(_input__.readInt64())
          case 58 =>
            __targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Value(_input__.readBytes())
          case 66 =>
            __rangeEnd = _input__.readBytes()
          case tag => _input__.skipField(tag)
        }
      }
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare(
          result = __result,
          target = __target,
          key = __key,
          rangeEnd = __rangeEnd,
          targetUnion = __targetUnion
      )
    }
    def withResult(__v: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult): Compare = copy(result = __v)
    def withTarget(__v: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget): Compare = copy(target = __v)
    def withKey(__v: _root_.com.google.protobuf.ByteString): Compare = copy(key = __v)
    def getVersion: _root_.scala.Long = targetUnion.version.getOrElse(0L)
    def withVersion(__v: _root_.scala.Long): Compare = copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Version(__v))
    def getCreateRevision: _root_.scala.Long = targetUnion.createRevision.getOrElse(0L)
    def withCreateRevision(__v: _root_.scala.Long): Compare = copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.CreateRevision(__v))
    def getModRevision: _root_.scala.Long = targetUnion.modRevision.getOrElse(0L)
    def withModRevision(__v: _root_.scala.Long): Compare = copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.ModRevision(__v))
    def getValue: _root_.com.google.protobuf.ByteString = targetUnion._value.getOrElse(_root_.com.google.protobuf.ByteString.EMPTY)
    def withValue(__v: _root_.com.google.protobuf.ByteString): Compare = copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Value(__v))
    def withRangeEnd(__v: _root_.com.google.protobuf.ByteString): Compare = copy(rangeEnd = __v)
    def clearTargetUnion: Compare = copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Empty)
    def withTargetUnion(__v: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion): Compare = copy(targetUnion = __v)
    def getFieldByNumber(__fieldNumber: _root_.scala.Int): _root_.scala.Any = {
      (__fieldNumber: @_root_.scala.unchecked) match {
        case 1 => {
          val __t = result.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 2 => {
          val __t = target.javaValueDescriptor
          if (__t.getNumber() != 0) __t else null
        }
        case 3 => {
          val __t = key
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
        case 4 => targetUnion.version.orNull
        case 5 => targetUnion.createRevision.orNull
        case 6 => targetUnion.modRevision.orNull
        case 7 => targetUnion._value.orNull
        case 8 => {
          val __t = rangeEnd
          if (__t != _root_.com.google.protobuf.ByteString.EMPTY) __t else null
        }
      }
    }
    def getField(__field: _root_.scalapb.descriptors.FieldDescriptor): _root_.scalapb.descriptors.PValue = {
      _root_.scala.Predef.require(__field.containingMessage eq companion.scalaDescriptor)
      (__field.number: @_root_.scala.unchecked) match {
        case 1 => _root_.scalapb.descriptors.PEnum(result.scalaValueDescriptor)
        case 2 => _root_.scalapb.descriptors.PEnum(target.scalaValueDescriptor)
        case 3 => _root_.scalapb.descriptors.PByteString(key)
        case 4 => targetUnion.version.map(_root_.scalapb.descriptors.PLong).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 5 => targetUnion.createRevision.map(_root_.scalapb.descriptors.PLong).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 6 => targetUnion.modRevision.map(_root_.scalapb.descriptors.PLong).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 7 => targetUnion._value.map(_root_.scalapb.descriptors.PByteString).getOrElse(_root_.scalapb.descriptors.PEmpty)
        case 8 => _root_.scalapb.descriptors.PByteString(rangeEnd)
      }
    }
    def toProtoString: _root_.scala.Predef.String = _root_.scalapb.TextFormat.printToUnicodeString(this)
    def companion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare
}

object Compare extends scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare] {
  implicit def messageCompanion: scalapb.GeneratedMessageCompanion[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare] = this
  def fromFieldsMap(__fieldsMap: scala.collection.immutable.Map[_root_.com.google.protobuf.Descriptors.FieldDescriptor, _root_.scala.Any]): com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare = {
    _root_.scala.Predef.require(__fieldsMap.keys.forall(_.getContainingType() == javaDescriptor), "FieldDescriptor does not match message type.")
    val __fields = javaDescriptor.getFields
    com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare(
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.fromValue(__fieldsMap.getOrElse(__fields.get(0), com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL.javaValueDescriptor).asInstanceOf[_root_.com.google.protobuf.Descriptors.EnumValueDescriptor].getNumber),
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.fromValue(__fieldsMap.getOrElse(__fields.get(1), com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION.javaValueDescriptor).asInstanceOf[_root_.com.google.protobuf.Descriptors.EnumValueDescriptor].getNumber),
      __fieldsMap.getOrElse(__fields.get(2), _root_.com.google.protobuf.ByteString.EMPTY).asInstanceOf[_root_.com.google.protobuf.ByteString],
      __fieldsMap.getOrElse(__fields.get(7), _root_.com.google.protobuf.ByteString.EMPTY).asInstanceOf[_root_.com.google.protobuf.ByteString],
      targetUnion = __fieldsMap.get(__fields.get(3)).asInstanceOf[_root_.scala.Option[_root_.scala.Long]].map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Version)
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(__fields.get(4)).asInstanceOf[_root_.scala.Option[_root_.scala.Long]].map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.CreateRevision))
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(__fields.get(5)).asInstanceOf[_root_.scala.Option[_root_.scala.Long]].map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.ModRevision))
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(__fields.get(6)).asInstanceOf[_root_.scala.Option[_root_.com.google.protobuf.ByteString]].map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Value))
    .getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Empty)
    )
  }
  implicit def messageReads: _root_.scalapb.descriptors.Reads[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare] = _root_.scalapb.descriptors.Reads{
    case _root_.scalapb.descriptors.PMessage(__fieldsMap) =>
      _root_.scala.Predef.require(__fieldsMap.keys.forall(_.containingMessage == scalaDescriptor), "FieldDescriptor does not match message type.")
      com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare(
        com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(1).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL.scalaValueDescriptor).number),
        com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.fromValue(__fieldsMap.get(scalaDescriptor.findFieldByNumber(2).get).map(_.as[_root_.scalapb.descriptors.EnumValueDescriptor]).getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION.scalaValueDescriptor).number),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(3).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY),
        __fieldsMap.get(scalaDescriptor.findFieldByNumber(8).get).map(_.as[_root_.com.google.protobuf.ByteString]).getOrElse(_root_.com.google.protobuf.ByteString.EMPTY),
        targetUnion = __fieldsMap.get(scalaDescriptor.findFieldByNumber(4).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Long]]).map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Version)
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(scalaDescriptor.findFieldByNumber(5).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Long]]).map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.CreateRevision))
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(scalaDescriptor.findFieldByNumber(6).get).flatMap(_.as[_root_.scala.Option[_root_.scala.Long]]).map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.ModRevision))
    .orElse[com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion](__fieldsMap.get(scalaDescriptor.findFieldByNumber(7).get).flatMap(_.as[_root_.scala.Option[_root_.com.google.protobuf.ByteString]]).map(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Value))
    .getOrElse(com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Empty)
      )
    case _ => throw new RuntimeException("Expected PMessage")
  }
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.Descriptor = RpcProto.javaDescriptor.getMessageTypes.get(9)
  def scalaDescriptor: _root_.scalapb.descriptors.Descriptor = RpcProto.scalaDescriptor.messages(9)
  def messageCompanionForFieldNumber(__number: _root_.scala.Int): _root_.scalapb.GeneratedMessageCompanion[_] = throw new MatchError(__number)
  lazy val nestedMessagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_ <: _root_.scalapb.GeneratedMessage]] = Seq.empty
  def enumCompanionForFieldNumber(__fieldNumber: _root_.scala.Int): _root_.scalapb.GeneratedEnumCompanion[_] = {
    (__fieldNumber: @_root_.scala.unchecked) match {
      case 1 => com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult
      case 2 => com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget
    }
  }
  lazy val defaultInstance = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare(
    result = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult.EQUAL,
    target = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget.VERSION,
    key = _root_.com.google.protobuf.ByteString.EMPTY,
    rangeEnd = _root_.com.google.protobuf.ByteString.EMPTY,
    targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Empty
  )
  sealed trait CompareResult extends _root_.scalapb.GeneratedEnum {
    type EnumType = CompareResult
    def isEqual: _root_.scala.Boolean = false
    def isGreater: _root_.scala.Boolean = false
    def isLess: _root_.scala.Boolean = false
    def isNotEqual: _root_.scala.Boolean = false
    def companion: _root_.scalapb.GeneratedEnumCompanion[CompareResult] = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult
  }
  
  object CompareResult extends _root_.scalapb.GeneratedEnumCompanion[CompareResult] {
    implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[CompareResult] = this
    @SerialVersionUID(0L)
    case object EQUAL extends CompareResult {
      val value = 0
      val index = 0
      val name = "EQUAL"
      override def isEqual: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object GREATER extends CompareResult {
      val value = 1
      val index = 1
      val name = "GREATER"
      override def isGreater: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object LESS extends CompareResult {
      val value = 2
      val index = 2
      val name = "LESS"
      override def isLess: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object NOT_EQUAL extends CompareResult {
      val value = 3
      val index = 3
      val name = "NOT_EQUAL"
      override def isNotEqual: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    final case class Unrecognized(value: _root_.scala.Int) extends CompareResult with _root_.scalapb.UnrecognizedEnum
    
    lazy val values = scala.collection.immutable.Seq(EQUAL, GREATER, LESS, NOT_EQUAL)
    def fromValue(value: _root_.scala.Int): CompareResult = value match {
      case 0 => EQUAL
      case 1 => GREATER
      case 2 => LESS
      case 3 => NOT_EQUAL
      case __other => Unrecognized(__other)
    }
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.javaDescriptor.getEnumTypes.get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.scalaDescriptor.enums(0)
  }
  sealed trait CompareTarget extends _root_.scalapb.GeneratedEnum {
    type EnumType = CompareTarget
    def isVersion: _root_.scala.Boolean = false
    def isCreate: _root_.scala.Boolean = false
    def isMod: _root_.scala.Boolean = false
    def isValue: _root_.scala.Boolean = false
    def companion: _root_.scalapb.GeneratedEnumCompanion[CompareTarget] = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget
  }
  
  object CompareTarget extends _root_.scalapb.GeneratedEnumCompanion[CompareTarget] {
    implicit def enumCompanion: _root_.scalapb.GeneratedEnumCompanion[CompareTarget] = this
    @SerialVersionUID(0L)
    case object VERSION extends CompareTarget {
      val value = 0
      val index = 0
      val name = "VERSION"
      override def isVersion: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object CREATE extends CompareTarget {
      val value = 1
      val index = 1
      val name = "CREATE"
      override def isCreate: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object MOD extends CompareTarget {
      val value = 2
      val index = 2
      val name = "MOD"
      override def isMod: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    case object VALUE extends CompareTarget {
      val value = 3
      val index = 3
      val name = "VALUE"
      override def isValue: _root_.scala.Boolean = true
    }
    
    @SerialVersionUID(0L)
    final case class Unrecognized(value: _root_.scala.Int) extends CompareTarget with _root_.scalapb.UnrecognizedEnum
    
    lazy val values = scala.collection.immutable.Seq(VERSION, CREATE, MOD, VALUE)
    def fromValue(value: _root_.scala.Int): CompareTarget = value match {
      case 0 => VERSION
      case 1 => CREATE
      case 2 => MOD
      case 3 => VALUE
      case __other => Unrecognized(__other)
    }
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.EnumDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.javaDescriptor.getEnumTypes.get(1)
    def scalaDescriptor: _root_.scalapb.descriptors.EnumDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.scalaDescriptor.enums(1)
  }
  sealed trait TargetUnion extends _root_.scalapb.GeneratedOneof {
    def isEmpty: _root_.scala.Boolean = false
    def isDefined: _root_.scala.Boolean = true
    def isVersion: _root_.scala.Boolean = false
    def isCreateRevision: _root_.scala.Boolean = false
    def isModRevision: _root_.scala.Boolean = false
    def isValue: _root_.scala.Boolean = false
    def version: _root_.scala.Option[_root_.scala.Long] = _root_.scala.None
    def createRevision: _root_.scala.Option[_root_.scala.Long] = _root_.scala.None
    def modRevision: _root_.scala.Option[_root_.scala.Long] = _root_.scala.None
    def _value: _root_.scala.Option[_root_.com.google.protobuf.ByteString] = _root_.scala.None
  }
  object TargetUnion extends {
    @SerialVersionUID(0L)
    case object Empty extends com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion {
      type ValueType = _root_.scala.Nothing
      override def isEmpty: _root_.scala.Boolean = true
      override def isDefined: _root_.scala.Boolean = false
      override def number: _root_.scala.Int = 0
      override def value: _root_.scala.Nothing = throw new java.util.NoSuchElementException("Empty.value")
    }
  
    @SerialVersionUID(0L)
    final case class Version(value: _root_.scala.Long) extends com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion {
      type ValueType = _root_.scala.Long
      override def isVersion: _root_.scala.Boolean = true
      override def version: _root_.scala.Option[_root_.scala.Long] = Some(value)
      override def number: _root_.scala.Int = 4
    }
    @SerialVersionUID(0L)
    final case class CreateRevision(value: _root_.scala.Long) extends com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion {
      type ValueType = _root_.scala.Long
      override def isCreateRevision: _root_.scala.Boolean = true
      override def createRevision: _root_.scala.Option[_root_.scala.Long] = Some(value)
      override def number: _root_.scala.Int = 5
    }
    @SerialVersionUID(0L)
    final case class ModRevision(value: _root_.scala.Long) extends com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion {
      type ValueType = _root_.scala.Long
      override def isModRevision: _root_.scala.Boolean = true
      override def modRevision: _root_.scala.Option[_root_.scala.Long] = Some(value)
      override def number: _root_.scala.Int = 6
    }
    @SerialVersionUID(0L)
    final case class Value(value: _root_.com.google.protobuf.ByteString) extends com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion {
      type ValueType = _root_.com.google.protobuf.ByteString
      override def isValue: _root_.scala.Boolean = true
      override def _value: _root_.scala.Option[_root_.com.google.protobuf.ByteString] = Some(value)
      override def number: _root_.scala.Int = 7
    }
  }
  implicit class CompareLens[UpperPB](_l: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare]) extends _root_.scalapb.lenses.ObjectLens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare](_l) {
    def result: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult] = field(_.result)((c_, f_) => c_.copy(result = f_))
    def target: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget] = field(_.target)((c_, f_) => c_.copy(target = f_))
    def key: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.key)((c_, f_) => c_.copy(key = f_))
    def version: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.getVersion)((c_, f_) => c_.copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Version(f_)))
    def createRevision: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.getCreateRevision)((c_, f_) => c_.copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.CreateRevision(f_)))
    def modRevision: _root_.scalapb.lenses.Lens[UpperPB, _root_.scala.Long] = field(_.getModRevision)((c_, f_) => c_.copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.ModRevision(f_)))
    def _value: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.getValue)((c_, f_) => c_.copy(targetUnion = com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion.Value(f_)))
    def rangeEnd: _root_.scalapb.lenses.Lens[UpperPB, _root_.com.google.protobuf.ByteString] = field(_.rangeEnd)((c_, f_) => c_.copy(rangeEnd = f_))
    def targetUnion: _root_.scalapb.lenses.Lens[UpperPB, com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion] = field(_.targetUnion)((c_, f_) => c_.copy(targetUnion = f_))
  }
  final val RESULT_FIELD_NUMBER = 1
  final val TARGET_FIELD_NUMBER = 2
  final val KEY_FIELD_NUMBER = 3
  final val VERSION_FIELD_NUMBER = 4
  final val CREATE_REVISION_FIELD_NUMBER = 5
  final val MOD_REVISION_FIELD_NUMBER = 6
  final val VALUE_FIELD_NUMBER = 7
  final val RANGE_END_FIELD_NUMBER = 8
  def of(
    result: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareResult,
    target: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.CompareTarget,
    key: _root_.com.google.protobuf.ByteString,
    rangeEnd: _root_.com.google.protobuf.ByteString,
    targetUnion: com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare.TargetUnion
  ): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare = _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.Compare(
    result,
    target,
    key,
    rangeEnd,
    targetUnion
  )
}