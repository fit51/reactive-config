package com.github.fit51.reactiveconfig.etcd.gen.rpc

object ClusterGrpc {
  val METHOD_MEMBER_ADD: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Cluster", "MemberAdd"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(3).getMethods.get(0)))
      .build()
  
  val METHOD_MEMBER_REMOVE: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Cluster", "MemberRemove"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(3).getMethods.get(1)))
      .build()
  
  val METHOD_MEMBER_UPDATE: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Cluster", "MemberUpdate"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(3).getMethods.get(2)))
      .build()
  
  val METHOD_MEMBER_LIST: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Cluster", "MemberList"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(3).getMethods.get(3)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.Cluster")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_MEMBER_ADD)
      .addMethod(METHOD_MEMBER_REMOVE)
      .addMethod(METHOD_MEMBER_UPDATE)
      .addMethod(METHOD_MEMBER_LIST)
      .build()
  
  trait Cluster extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Cluster
    /** MemberAdd adds a member into the cluster.
      */
    def memberAdd(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse]
    /** MemberRemove removes an existing member from the cluster.
      */
    def memberRemove(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse]
    /** MemberUpdate updates the member configuration.
      */
    def memberUpdate(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse]
    /** MemberList lists all the members in the cluster.
      */
    def memberList(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse]
  }
  
  object Cluster extends _root_.scalapb.grpc.ServiceCompanion[Cluster] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Cluster] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(3)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = RpcProto.scalaDescriptor.services(3)
  }
  
  trait ClusterBlockingClient {
    def serviceCompanion = Cluster
    /** MemberAdd adds a member into the cluster.
      */
    def memberAdd(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse
    /** MemberRemove removes an existing member from the cluster.
      */
    def memberRemove(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse
    /** MemberUpdate updates the member configuration.
      */
    def memberUpdate(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse
    /** MemberList lists all the members in the cluster.
      */
    def memberList(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse
  }
  
  class ClusterBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[ClusterBlockingStub](channel, options) with ClusterBlockingClient {
    /** MemberAdd adds a member into the cluster.
      */
    override def memberAdd(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_MEMBER_ADD, options, request)
    }
    
    /** MemberRemove removes an existing member from the cluster.
      */
    override def memberRemove(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_MEMBER_REMOVE, options, request)
    }
    
    /** MemberUpdate updates the member configuration.
      */
    override def memberUpdate(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_MEMBER_UPDATE, options, request)
    }
    
    /** MemberList lists all the members in the cluster.
      */
    override def memberList(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest): _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_MEMBER_LIST, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): ClusterBlockingStub = new ClusterBlockingStub(channel, options)
  }
  
  class ClusterStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[ClusterStub](channel, options) with Cluster {
    /** MemberAdd adds a member into the cluster.
      */
    override def memberAdd(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_MEMBER_ADD, options, request)
    }
    
    /** MemberRemove removes an existing member from the cluster.
      */
    override def memberRemove(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_MEMBER_REMOVE, options, request)
    }
    
    /** MemberUpdate updates the member configuration.
      */
    override def memberUpdate(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_MEMBER_UPDATE, options, request)
    }
    
    /** MemberList lists all the members in the cluster.
      */
    override def memberList(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest): scala.concurrent.Future[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_MEMBER_LIST, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): ClusterStub = new ClusterStub(channel, options)
  }
  
  def bindService(serviceImpl: Cluster, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
    _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
    .addMethod(
      METHOD_MEMBER_ADD,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberAddResponse]): Unit =
          serviceImpl.memberAdd(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_MEMBER_REMOVE,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberRemoveResponse]): Unit =
          serviceImpl.memberRemove(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_MEMBER_UPDATE,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberUpdateResponse]): Unit =
          serviceImpl.memberUpdate(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .addMethod(
      METHOD_MEMBER_LIST,
      _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse] {
        override def invoke(request: _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListRequest, observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.MemberListResponse]): Unit =
          serviceImpl.memberList(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
            executionContext)
      }))
    .build()
  
  def blockingStub(channel: _root_.io.grpc.Channel): ClusterBlockingStub = new ClusterBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): ClusterStub = new ClusterStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(3)
  
}