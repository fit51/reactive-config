package com.github.fit51.reactiveconfig.etcd.gen.rpc

object WatchGrpc {
  val METHOD_WATCH: _root_.io.grpc.MethodDescriptor[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Watch", "Watch"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices.get(1).getMethods.get(0)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.Watch")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_WATCH)
      .build()
  
  trait Watch extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Watch
    /** Watch watches for events happening or that have happened. Both input and output
      * are streams; the input stream is for creating and canceling watchers and the output
      * stream sends events. One watch RPC can watch on multiple key ranges, streaming events
      * for several watches at once. The entire event history can be watched starting from the
      * last compaction revision.
      */
    def watch(responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest]
  }
  
  object Watch extends _root_.scalapb.grpc.ServiceCompanion[Watch] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Watch] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(1)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = RpcProto.scalaDescriptor.services(1)
  }
  
  trait WatchBlockingClient {
    def serviceCompanion = Watch
  }
  
  class WatchBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[WatchBlockingStub](channel, options) with WatchBlockingClient {
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): WatchBlockingStub = new WatchBlockingStub(channel, options)
  }
  
  class WatchStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[WatchStub](channel, options) with Watch {
    /** Watch watches for events happening or that have happened. Both input and output
      * are streams; the input stream is for creating and canceling watchers and the output
      * stream sends events. One watch RPC can watch on multiple key ranges, streaming events
      * for several watches at once. The entire event history can be watched starting from the
      * last compaction revision.
      */
    override def watch(responseObserver: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest] = {
      _root_.scalapb.grpc.ClientCalls.asyncBidiStreamingCall(channel, METHOD_WATCH, options, responseObserver)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): WatchStub = new WatchStub(channel, options)
  }
  
  def bindService(serviceImpl: Watch, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
    _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
    .addMethod(
      METHOD_WATCH,
      _root_.io.grpc.stub.ServerCalls.asyncBidiStreamingCall(new _root_.io.grpc.stub.ServerCalls.BidiStreamingMethod[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest, _root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse] {
        override def invoke(observer: _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchResponse]): _root_.io.grpc.stub.StreamObserver[_root_.com.github.fit51.reactiveconfig.etcd.gen.rpc.WatchRequest] =
          serviceImpl.watch(observer)
      }))
    .build()
  
  def blockingStub(channel: _root_.io.grpc.Channel): WatchBlockingStub = new WatchBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): WatchStub = new WatchStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(1)
  
}