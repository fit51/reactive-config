package com.github.fit51.reactiveconfig.etcd.gen.rpc

object LeaseGrpc {
  val METHOD_LEASE_GRANT: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Lease", "LeaseGrant"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(2).getMethods.get(0)))
      .build()
  
  val METHOD_LEASE_REVOKE: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Lease", "LeaseRevoke"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(2).getMethods.get(1)))
      .build()
  
  val METHOD_LEASE_KEEP_ALIVE: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Lease", "LeaseKeepAlive"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(2).getMethods.get(2)))
      .build()
  
  val METHOD_LEASE_TIME_TO_LIVE: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Lease", "LeaseTimeToLive"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(2).getMethods.get(3)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.Lease")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_LEASE_GRANT)
      .addMethod(METHOD_LEASE_REVOKE)
      .addMethod(METHOD_LEASE_KEEP_ALIVE)
      .addMethod(METHOD_LEASE_TIME_TO_LIVE)
      .build()
  
  trait Lease extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Lease
    /** LeaseGrant creates a lease which expires if the server does not receive a keepAlive
      * within a given time to live period. All keys attached to the lease will be expired and
      * deleted if the lease expires. Each expired key generates a delete event in the event history.
      */
    def leaseGrant(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse]
    /** LeaseRevoke revokes a lease. All keys attached to the lease will expire and be deleted.
      */
    def leaseRevoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse]
    /** LeaseKeepAlive keeps the lease alive by streaming keep alive requests from the client
      * to the server and streaming keep alive responses from the server to the client.
      */
    def leaseKeepAlive(responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest]
    /** LeaseTimeToLive retrieves lease information.
      */
    def leaseTimeToLive(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse]
  }
  
  object Lease extends _root_.scalapb.grpc.ServiceCompanion[Lease] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Lease] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(2)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = RpcProto.scalaDescriptor.services(2)
  }
  
  trait LeaseBlockingClient {
    def serviceCompanion = Lease
    /** LeaseGrant creates a lease which expires if the server does not receive a keepAlive
      * within a given time to live period. All keys attached to the lease will be expired and
      * deleted if the lease expires. Each expired key generates a delete event in the event history.
      */
    def leaseGrant(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse
    /** LeaseRevoke revokes a lease. All keys attached to the lease will expire and be deleted.
      */
    def leaseRevoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse
    /** LeaseTimeToLive retrieves lease information.
      */
    def leaseTimeToLive(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse
  }
  
  class LeaseBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LeaseBlockingStub](channel, options) with LeaseBlockingClient {
    /** LeaseGrant creates a lease which expires if the server does not receive a keepAlive
      * within a given time to live period. All keys attached to the lease will be expired and
      * deleted if the lease expires. Each expired key generates a delete event in the event history.
      */
    override def leaseGrant(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_LEASE_GRANT, options, request)
    }
    
    /** LeaseRevoke revokes a lease. All keys attached to the lease will expire and be deleted.
      */
    override def leaseRevoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_LEASE_REVOKE, options, request)
    }
    
    /** LeaseTimeToLive retrieves lease information.
      */
    override def leaseTimeToLive(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_LEASE_TIME_TO_LIVE, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LeaseBlockingStub = new LeaseBlockingStub(channel, options)
  }
  
  class LeaseStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[LeaseStub](channel, options) with Lease {
    /** LeaseGrant creates a lease which expires if the server does not receive a keepAlive
      * within a given time to live period. All keys attached to the lease will be expired and
      * deleted if the lease expires. Each expired key generates a delete event in the event history.
      */
    override def leaseGrant(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_LEASE_GRANT, options, request)
    }
    
    /** LeaseRevoke revokes a lease. All keys attached to the lease will expire and be deleted.
      */
    override def leaseRevoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_LEASE_REVOKE, options, request)
    }
    
    /** LeaseKeepAlive keeps the lease alive by streaming keep alive requests from the client
      * to the server and streaming keep alive responses from the server to the client.
      */
    override def leaseKeepAlive(responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest] = {
      _root_.scalapb.grpc.ClientCalls.asyncBidiStreamingCall(channel, METHOD_LEASE_KEEP_ALIVE, options, responseObserver)
    }
    
    /** LeaseTimeToLive retrieves lease information.
      */
    override def leaseTimeToLive(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_LEASE_TIME_TO_LIVE, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): LeaseStub = new LeaseStub(channel, options)
  }
  
  def bindService(serviceImpl: Lease, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
    _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
    .addMethod(
      METHOD_LEASE_GRANT,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseGrantResponse]): Unit =
          serviceImpl.leaseGrant(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_LEASE_REVOKE,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseRevokeResponse]): Unit =
          serviceImpl.leaseRevoke(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_LEASE_KEEP_ALIVE,
      _root_.io.grpc.stub.ServerCalls.asyncBidiStreamingCall(new _root_.io.grpc.stub.ServerCalls.BidiStreamingMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse] {
        override def invoke(observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseKeepAliveRequest] =
          serviceImpl.leaseKeepAlive(observer)
      }))
    .addMethod(
      METHOD_LEASE_TIME_TO_LIVE,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.LeaseTimeToLiveResponse]): Unit =
          serviceImpl.leaseTimeToLive(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .build()
  
  def blockingStub(channel: _root_.io.grpc.Channel): LeaseBlockingStub = new LeaseBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): LeaseStub = new LeaseStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(2)
  
}