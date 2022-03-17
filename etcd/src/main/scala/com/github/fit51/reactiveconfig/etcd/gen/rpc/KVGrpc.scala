package com.github.fit51.reactiveconfig.etcd.gen.rpc

object KVGrpc {
  val METHOD_RANGE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.KV", "Range"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0).getMethods().get(0)))
      .build()
  
  val METHOD_PUT: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.KV", "Put"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0).getMethods().get(1)))
      .build()
  
  val METHOD_DELETE_RANGE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.KV", "DeleteRange"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0).getMethods().get(2)))
      .build()
  
  val METHOD_TXN: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.KV", "Txn"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0).getMethods().get(3)))
      .build()
  
  val METHOD_COMPACT: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.KV", "Compact"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0).getMethods().get(4)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.KV")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_RANGE)
      .addMethod(METHOD_PUT)
      .addMethod(METHOD_DELETE_RANGE)
      .addMethod(METHOD_TXN)
      .addMethod(METHOD_COMPACT)
      .build()
  
  trait KV extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = KV
    /** Range gets the keys in the range from the key-value store.
      */
    def range(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse]
    /** Put puts the given key into the key-value store.
      * A put request increments the revision of the key-value store
      * and generates one event in the event history.
      */
    def put(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse]
    /** DeleteRange deletes the given range from the key-value store.
      * A delete request increments the revision of the key-value store
      * and generates a delete event in the event history for every deleted key.
      */
    def deleteRange(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse]
    /** Txn processes multiple requests in a single transaction.
      * A txn request increments the revision of the key-value store
      * and generates events with the same revision for every completed request.
      * It is not allowed to modify the same key several times within one txn.
      */
    def txn(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse]
    /** Compact compacts the event history in the etcd key-value store. The key-value
      * store should be periodically compacted or the event history will continue to grow
      * indefinitely.
      */
    def compact(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse]
  }
  
  object KV extends _root_.scalapb.grpc.ServiceCompanion[KV] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[KV] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.scalaDescriptor.services(0)
    def bindService(serviceImpl: KV, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_RANGE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse]): Unit =
            serviceImpl.range(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_PUT,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse]): Unit =
            serviceImpl.put(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_DELETE_RANGE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse]): Unit =
            serviceImpl.deleteRange(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_TXN,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse]): Unit =
            serviceImpl.txn(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_COMPACT,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse]): Unit =
            serviceImpl.compact(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  trait KVBlockingClient {
    def serviceCompanion = KV
    /** Range gets the keys in the range from the key-value store.
      */
    def range(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse
    /** Put puts the given key into the key-value store.
      * A put request increments the revision of the key-value store
      * and generates one event in the event history.
      */
    def put(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse
    /** DeleteRange deletes the given range from the key-value store.
      * A delete request increments the revision of the key-value store
      * and generates a delete event in the event history for every deleted key.
      */
    def deleteRange(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse
    /** Txn processes multiple requests in a single transaction.
      * A txn request increments the revision of the key-value store
      * and generates events with the same revision for every completed request.
      * It is not allowed to modify the same key several times within one txn.
      */
    def txn(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse
    /** Compact compacts the event history in the etcd key-value store. The key-value
      * store should be periodically compacted or the event history will continue to grow
      * indefinitely.
      */
    def compact(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse
  }
  
  class KVBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[KVBlockingStub](channel, options) with KVBlockingClient {
    /** Range gets the keys in the range from the key-value store.
      */
    override def range(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_RANGE, options, request)
    }
    
    /** Put puts the given key into the key-value store.
      * A put request increments the revision of the key-value store
      * and generates one event in the event history.
      */
    override def put(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_PUT, options, request)
    }
    
    /** DeleteRange deletes the given range from the key-value store.
      * A delete request increments the revision of the key-value store
      * and generates a delete event in the event history for every deleted key.
      */
    override def deleteRange(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_DELETE_RANGE, options, request)
    }
    
    /** Txn processes multiple requests in a single transaction.
      * A txn request increments the revision of the key-value store
      * and generates events with the same revision for every completed request.
      * It is not allowed to modify the same key several times within one txn.
      */
    override def txn(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_TXN, options, request)
    }
    
    /** Compact compacts the event history in the etcd key-value store. The key-value
      * store should be periodically compacted or the event history will continue to grow
      * indefinitely.
      */
    override def compact(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_COMPACT, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): KVBlockingStub = new KVBlockingStub(channel, options)
  }
  
  class KVStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[KVStub](channel, options) with KV {
    /** Range gets the keys in the range from the key-value store.
      */
    override def range(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.RangeResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_RANGE, options, request)
    }
    
    /** Put puts the given key into the key-value store.
      * A put request increments the revision of the key-value store
      * and generates one event in the event history.
      */
    override def put(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.PutRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.PutResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_PUT, options, request)
    }
    
    /** DeleteRange deletes the given range from the key-value store.
      * A delete request increments the revision of the key-value store
      * and generates a delete event in the event history for every deleted key.
      */
    override def deleteRange(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.DeleteRangeResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_DELETE_RANGE, options, request)
    }
    
    /** Txn processes multiple requests in a single transaction.
      * A txn request increments the revision of the key-value store
      * and generates events with the same revision for every completed request.
      * It is not allowed to modify the same key several times within one txn.
      */
    override def txn(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.TxnResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_TXN, options, request)
    }
    
    /** Compact compacts the event history in the etcd key-value store. The key-value
      * store should be periodically compacted or the event history will continue to grow
      * indefinitely.
      */
    override def compact(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.CompactionResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_COMPACT, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): KVStub = new KVStub(channel, options)
  }
  
  def bindService(serviceImpl: KV, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = KV.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): KVBlockingStub = new KVBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): KVStub = new KVStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(0)
  
}