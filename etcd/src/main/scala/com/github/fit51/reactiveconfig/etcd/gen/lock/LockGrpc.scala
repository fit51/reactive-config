package com.github.fit51.reactiveconfig.etcd.gen.lock

object LockGrpc {
  val METHOD_LOCK: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest, com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("v3lockpb.Lock", "Lock"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.javaDescriptor.getServices().get(0).getMethods().get(0)))
      .build()
  
  val METHOD_UNLOCK: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest, com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("v3lockpb.Lock", "Unlock"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.javaDescriptor.getServices().get(0).getMethods().get(1)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("v3lockpb.Lock")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.javaDescriptor))
      .addMethod(METHOD_LOCK)
      .addMethod(METHOD_UNLOCK)
      .build()
  
  /** The lock service exposes client-side locking facilities as a gRPC interface.
    */
  trait Lock extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Lock
    /** Lock acquires a distributed shared lock on a given named lock.
      * On success, it will return a unique key that exists so long as the
      * lock is held by the caller. This key can be used in conjunction with
      * transactions to safely ensure updates to etcd only occur while holding
      * lock ownership. The lock is held until Unlock is called on the key or the
      * lease associate with the owner expires.
      */
    def lock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse]
    /** Unlock takes a key returned by Lock and releases the hold on lock. The
      * next Lock caller waiting for the lock will then be woken up and given
      * ownership of the lock.
      */
    def unlock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse]
  }
  
  object Lock extends _root_.scalapb.grpc.ServiceCompanion[Lock] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Lock] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.javaDescriptor.getServices().get(0)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.scalaDescriptor.services(0)
    def bindService(serviceImpl: Lock, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_LOCK,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest, com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse]): Unit =
            serviceImpl.lock(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_UNLOCK,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest, com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse]): Unit =
            serviceImpl.unlock(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  /** The lock service exposes client-side locking facilities as a gRPC interface.
    */
  trait LockBlockingClient {
    def serviceCompanion = Lock
    /** Lock acquires a distributed shared lock on a given named lock.
      * On success, it will return a unique key that exists so long as the
      * lock is held by the caller. This key can be used in conjunction with
      * transactions to safely ensure updates to etcd only occur while holding
      * lock ownership. The lock is held until Unlock is called on the key or the
      * lease associate with the owner expires.
      */
    def lock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest): com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse
    /** Unlock takes a key returned by Lock and releases the hold on lock. The
      * next Lock caller waiting for the lock will then be woken up and given
      * ownership of the lock.
      */
    def unlock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest): com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse
  }
  
  class LockBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LockBlockingStub](channel, options) with LockBlockingClient {
    /** Lock acquires a distributed shared lock on a given named lock.
      * On success, it will return a unique key that exists so long as the
      * lock is held by the caller. This key can be used in conjunction with
      * transactions to safely ensure updates to etcd only occur while holding
      * lock ownership. The lock is held until Unlock is called on the key or the
      * lease associate with the owner expires.
      */
    override def lock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest): com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_LOCK, options, request)
    }
    
    /** Unlock takes a key returned by Lock and releases the hold on lock. The
      * next Lock caller waiting for the lock will then be woken up and given
      * ownership of the lock.
      */
    override def unlock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest): com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_UNLOCK, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LockBlockingStub = new LockBlockingStub(channel, options)
  }
  
  class LockStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LockStub](channel, options) with Lock {
    /** Lock acquires a distributed shared lock on a given named lock.
      * On success, it will return a unique key that exists so long as the
      * lock is held by the caller. This key can be used in conjunction with
      * transactions to safely ensure updates to etcd only occur while holding
      * lock ownership. The lock is held until Unlock is called on the key or the
      * lease associate with the owner expires.
      */
    override def lock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.LockRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.lock.LockResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_LOCK, options, request)
    }
    
    /** Unlock takes a key returned by Lock and releases the hold on lock. The
      * next Lock caller waiting for the lock will then be woken up and given
      * ownership of the lock.
      */
    override def unlock(request: com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.lock.UnlockResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_UNLOCK, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LockStub = new LockStub(channel, options)
  }
  
  def bindService(serviceImpl: Lock, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = Lock.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): LockBlockingStub = new LockBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): LockStub = new LockStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.lock.LockProto.javaDescriptor.getServices().get(0)
  
}