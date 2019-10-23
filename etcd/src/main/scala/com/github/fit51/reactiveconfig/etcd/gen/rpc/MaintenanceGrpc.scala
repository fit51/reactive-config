package com.github.fit51.reactiveconfig.etcd.gen.rpc

object MaintenanceGrpc {
  val METHOD_ALARM: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Maintenance", "Alarm"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(4).getMethods.get(0)))
      .build()
  
  val METHOD_STATUS: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Maintenance", "Status"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(4).getMethods.get(1)))
      .build()
  
  val METHOD_DEFRAGMENT: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Maintenance", "Defragment"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(4).getMethods.get(2)))
      .build()
  
  val METHOD_HASH: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Maintenance", "Hash"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(4).getMethods.get(3)))
      .build()
  
  val METHOD_SNAPSHOT: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Maintenance", "Snapshot"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(4).getMethods.get(4)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.Maintenance")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_ALARM)
      .addMethod(METHOD_STATUS)
      .addMethod(METHOD_DEFRAGMENT)
      .addMethod(METHOD_HASH)
      .addMethod(METHOD_SNAPSHOT)
      .build()
  
  trait Maintenance extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Maintenance
    /** Alarm activates, deactivates, and queries alarms regarding cluster health.
      */
    def alarm(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse]
    /** Status gets the status of the member.
      */
    def status(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse]
    /** Defragment defragments a member's backend database to recover storage space.
      */
    def defragment(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse]
    /** Hash returns the hash of the local KV state for consistency checking purpose.
      * This is designed for testing; do not use this in production when there
      * are ongoing transactions.
      */
    def hash(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse]
    /** Snapshot sends a snapshot of the entire backend from a member over a stream to a client.
      */
    def snapshot(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest, responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse]): Unit
  }
  
  object Maintenance extends _root_.scalapb.grpc.ServiceCompanion[Maintenance] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Maintenance] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(4)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = RpcProto.scalaDescriptor.services(4)
  }
  
  trait MaintenanceBlockingClient {
    def serviceCompanion = Maintenance
    /** Alarm activates, deactivates, and queries alarms regarding cluster health.
      */
    def alarm(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse
    /** Status gets the status of the member.
      */
    def status(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse
    /** Defragment defragments a member's backend database to recover storage space.
      */
    def defragment(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse
    /** Hash returns the hash of the local KV state for consistency checking purpose.
      * This is designed for testing; do not use this in production when there
      * are ongoing transactions.
      */
    def hash(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse
    /** Snapshot sends a snapshot of the entire backend from a member over a stream to a client.
      */
    def snapshot(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest): scala.collection.Iterator[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse]
  }
  
  class MaintenanceBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[MaintenanceBlockingStub](channel, options) with MaintenanceBlockingClient {
    /** Alarm activates, deactivates, and queries alarms regarding cluster health.
      */
    override def alarm(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ALARM, options, request)
    }
    
    /** Status gets the status of the member.
      */
    override def status(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_STATUS, options, request)
    }
    
    /** Defragment defragments a member's backend database to recover storage space.
      */
    override def defragment(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_DEFRAGMENT, options, request)
    }
    
    /** Hash returns the hash of the local KV state for consistency checking purpose.
      * This is designed for testing; do not use this in production when there
      * are ongoing transactions.
      */
    override def hash(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_HASH, options, request)
    }
    
    /** Snapshot sends a snapshot of the entire backend from a member over a stream to a client.
      */
    override def snapshot(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest): scala.collection.Iterator[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] = {
      _root_.scalapb.grpc.ClientCalls.blockingServerStreamingCall(channel, METHOD_SNAPSHOT, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): MaintenanceBlockingStub = new MaintenanceBlockingStub(channel, options)
  }
  
  class MaintenanceStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[MaintenanceStub](channel, options) with Maintenance {
    /** Alarm activates, deactivates, and queries alarms regarding cluster health.
      */
    override def alarm(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ALARM, options, request)
    }
    
    /** Status gets the status of the member.
      */
    override def status(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_STATUS, options, request)
    }
    
    /** Defragment defragments a member's backend database to recover storage space.
      */
    override def defragment(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_DEFRAGMENT, options, request)
    }
    
    /** Hash returns the hash of the local KV state for consistency checking purpose.
      * This is designed for testing; do not use this in production when there
      * are ongoing transactions.
      */
    override def hash(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_HASH, options, request)
    }
    
    /** Snapshot sends a snapshot of the entire backend from a member over a stream to a client.
      */
    override def snapshot(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest, responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse]): Unit = {
      _root_.scalapb.grpc.ClientCalls.asyncServerStreamingCall(channel, METHOD_SNAPSHOT, options, request, responseObserver)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): MaintenanceStub = new MaintenanceStub(channel, options)
  }
  
  def bindService(serviceImpl: Maintenance, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
    _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
    .addMethod(
      METHOD_ALARM,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.AlarmResponse]): Unit =
          serviceImpl.alarm(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_STATUS,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.StatusResponse]): Unit =
          serviceImpl.status(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_DEFRAGMENT,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.DefragmentResponse]): Unit =
          serviceImpl.defragment(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_HASH,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.HashResponse]): Unit =
          serviceImpl.hash(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_SNAPSHOT,
      _root_.io.grpc.stub.ServerCalls.asyncServerStreamingCall(new _root_.io.grpc.stub.ServerCalls.ServerStreamingMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.SnapshotResponse]): Unit =
          serviceImpl.snapshot(request, observer)
      }))
    .build()
  
  def blockingStub(channel: _root_.io.grpc.Channel): MaintenanceBlockingStub = new MaintenanceBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): MaintenanceStub = new MaintenanceStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(4)
  
}