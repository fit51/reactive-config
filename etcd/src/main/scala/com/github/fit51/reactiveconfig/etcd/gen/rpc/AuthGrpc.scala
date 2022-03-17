package com.github.fit51.reactiveconfig.etcd.gen.rpc

object AuthGrpc {
  val METHOD_AUTH_ENABLE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "AuthEnable"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(0)))
      .build()
  
  val METHOD_AUTH_DISABLE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "AuthDisable"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(1)))
      .build()
  
  val METHOD_AUTHENTICATE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "Authenticate"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(2)))
      .build()
  
  val METHOD_USER_ADD: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserAdd"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(3)))
      .build()
  
  val METHOD_USER_GET: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserGet"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(4)))
      .build()
  
  val METHOD_USER_LIST: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserList"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(5)))
      .build()
  
  val METHOD_USER_DELETE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserDelete"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(6)))
      .build()
  
  val METHOD_USER_CHANGE_PASSWORD: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserChangePassword"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(7)))
      .build()
  
  val METHOD_USER_GRANT_ROLE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserGrantRole"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(8)))
      .build()
  
  val METHOD_USER_REVOKE_ROLE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "UserRevokeRole"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(9)))
      .build()
  
  val METHOD_ROLE_ADD: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleAdd"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(10)))
      .build()
  
  val METHOD_ROLE_GET: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleGet"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(11)))
      .build()
  
  val METHOD_ROLE_LIST: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleList"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(12)))
      .build()
  
  val METHOD_ROLE_DELETE: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleDelete"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(13)))
      .build()
  
  val METHOD_ROLE_GRANT_PERMISSION: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleGrantPermission"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(14)))
      .build()
  
  val METHOD_ROLE_REVOKE_PERMISSION: _root_.io.grpc.MethodDescriptor[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse] =
    _root_.io.grpc.MethodDescriptor.newBuilder()
      .setType(_root_.io.grpc.MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(_root_.io.grpc.MethodDescriptor.generateFullMethodName("etcdserverpb.Auth", "RoleRevokePermission"))
      .setSampledToLocalTracing(true)
      .setRequestMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest])
      .setResponseMarshaller(_root_.scalapb.grpc.Marshaller.forMessage[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse])
      .setSchemaDescriptor(_root_.scalapb.grpc.ConcreteProtoMethodDescriptorSupplier.fromMethodDescriptor(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5).getMethods().get(15)))
      .build()
  
  val SERVICE: _root_.io.grpc.ServiceDescriptor =
    _root_.io.grpc.ServiceDescriptor.newBuilder("etcdserverpb.Auth")
      .setSchemaDescriptor(new _root_.scalapb.grpc.ConcreteProtoFileDescriptorSupplier(com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor))
      .addMethod(METHOD_AUTH_ENABLE)
      .addMethod(METHOD_AUTH_DISABLE)
      .addMethod(METHOD_AUTHENTICATE)
      .addMethod(METHOD_USER_ADD)
      .addMethod(METHOD_USER_GET)
      .addMethod(METHOD_USER_LIST)
      .addMethod(METHOD_USER_DELETE)
      .addMethod(METHOD_USER_CHANGE_PASSWORD)
      .addMethod(METHOD_USER_GRANT_ROLE)
      .addMethod(METHOD_USER_REVOKE_ROLE)
      .addMethod(METHOD_ROLE_ADD)
      .addMethod(METHOD_ROLE_GET)
      .addMethod(METHOD_ROLE_LIST)
      .addMethod(METHOD_ROLE_DELETE)
      .addMethod(METHOD_ROLE_GRANT_PERMISSION)
      .addMethod(METHOD_ROLE_REVOKE_PERMISSION)
      .build()
  
  trait Auth extends _root_.scalapb.grpc.AbstractService {
    override def serviceCompanion = Auth
    /** AuthEnable enables authentication.
      */
    def authEnable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse]
    /** AuthDisable disables authentication.
      */
    def authDisable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse]
    /** Authenticate processes an authenticate request.
      */
    def authenticate(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse]
    /** UserAdd adds a new user.
      */
    def userAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse]
    /** UserGet gets detailed user information.
      */
    def userGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse]
    /** UserList gets a list of all users.
      */
    def userList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse]
    /** UserDelete deletes a specified user.
      */
    def userDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse]
    /** UserChangePassword changes the password of a specified user.
      */
    def userChangePassword(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse]
    /** UserGrant grants a role to a specified user.
      */
    def userGrantRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse]
    /** UserRevokeRole revokes a role of specified user.
      */
    def userRevokeRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse]
    /** RoleAdd adds a new role.
      */
    def roleAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse]
    /** RoleGet gets detailed role information.
      */
    def roleGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse]
    /** RoleList gets lists of all roles.
      */
    def roleList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse]
    /** RoleDelete deletes a specified role.
      */
    def roleDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse]
    /** RoleGrantPermission grants a permission of a specified key or range to a specified role.
      */
    def roleGrantPermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse]
    /** RoleRevokePermission revokes a key or range permission of a specified role.
      */
    def roleRevokePermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse]
  }
  
  object Auth extends _root_.scalapb.grpc.ServiceCompanion[Auth] {
    implicit def serviceCompanion: _root_.scalapb.grpc.ServiceCompanion[Auth] = this
    def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5)
    def scalaDescriptor: _root_.scalapb.descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.scalaDescriptor.services(5)
    def bindService(serviceImpl: Auth, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition =
      _root_.io.grpc.ServerServiceDefinition.builder(SERVICE)
      .addMethod(
        METHOD_AUTH_ENABLE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse]): Unit =
            serviceImpl.authEnable(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_AUTH_DISABLE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse]): Unit =
            serviceImpl.authDisable(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_AUTHENTICATE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse]): Unit =
            serviceImpl.authenticate(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_ADD,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse]): Unit =
            serviceImpl.userAdd(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_GET,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse]): Unit =
            serviceImpl.userGet(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_LIST,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse]): Unit =
            serviceImpl.userList(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_DELETE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse]): Unit =
            serviceImpl.userDelete(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_CHANGE_PASSWORD,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse]): Unit =
            serviceImpl.userChangePassword(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_GRANT_ROLE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse]): Unit =
            serviceImpl.userGrantRole(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_USER_REVOKE_ROLE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse]): Unit =
            serviceImpl.userRevokeRole(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_ADD,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse]): Unit =
            serviceImpl.roleAdd(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_GET,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse]): Unit =
            serviceImpl.roleGet(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_LIST,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse]): Unit =
            serviceImpl.roleList(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_DELETE,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse]): Unit =
            serviceImpl.roleDelete(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_GRANT_PERMISSION,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse]): Unit =
            serviceImpl.roleGrantPermission(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .addMethod(
        METHOD_ROLE_REVOKE_PERMISSION,
        _root_.io.grpc.stub.ServerCalls.asyncUnaryCall(new _root_.io.grpc.stub.ServerCalls.UnaryMethod[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest, com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse] {
          override def invoke(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest, observer: _root_.io.grpc.stub.StreamObserver[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse]): Unit =
            serviceImpl.roleRevokePermission(request).onComplete(scalapb.grpc.Grpc.completeObserver(observer))(
              executionContext)
        }))
      .build()
  }
  
  trait AuthBlockingClient {
    def serviceCompanion = Auth
    /** AuthEnable enables authentication.
      */
    def authEnable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse
    /** AuthDisable disables authentication.
      */
    def authDisable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse
    /** Authenticate processes an authenticate request.
      */
    def authenticate(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse
    /** UserAdd adds a new user.
      */
    def userAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse
    /** UserGet gets detailed user information.
      */
    def userGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse
    /** UserList gets a list of all users.
      */
    def userList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse
    /** UserDelete deletes a specified user.
      */
    def userDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse
    /** UserChangePassword changes the password of a specified user.
      */
    def userChangePassword(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse
    /** UserGrant grants a role to a specified user.
      */
    def userGrantRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse
    /** UserRevokeRole revokes a role of specified user.
      */
    def userRevokeRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse
    /** RoleAdd adds a new role.
      */
    def roleAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse
    /** RoleGet gets detailed role information.
      */
    def roleGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse
    /** RoleList gets lists of all roles.
      */
    def roleList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse
    /** RoleDelete deletes a specified role.
      */
    def roleDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse
    /** RoleGrantPermission grants a permission of a specified key or range to a specified role.
      */
    def roleGrantPermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse
    /** RoleRevokePermission revokes a key or range permission of a specified role.
      */
    def roleRevokePermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse
  }
  
  class AuthBlockingStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[AuthBlockingStub](channel, options) with AuthBlockingClient {
    /** AuthEnable enables authentication.
      */
    override def authEnable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_AUTH_ENABLE, options, request)
    }
    
    /** AuthDisable disables authentication.
      */
    override def authDisable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_AUTH_DISABLE, options, request)
    }
    
    /** Authenticate processes an authenticate request.
      */
    override def authenticate(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_AUTHENTICATE, options, request)
    }
    
    /** UserAdd adds a new user.
      */
    override def userAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_ADD, options, request)
    }
    
    /** UserGet gets detailed user information.
      */
    override def userGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_GET, options, request)
    }
    
    /** UserList gets a list of all users.
      */
    override def userList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_LIST, options, request)
    }
    
    /** UserDelete deletes a specified user.
      */
    override def userDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_DELETE, options, request)
    }
    
    /** UserChangePassword changes the password of a specified user.
      */
    override def userChangePassword(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_CHANGE_PASSWORD, options, request)
    }
    
    /** UserGrant grants a role to a specified user.
      */
    override def userGrantRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_GRANT_ROLE, options, request)
    }
    
    /** UserRevokeRole revokes a role of specified user.
      */
    override def userRevokeRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_USER_REVOKE_ROLE, options, request)
    }
    
    /** RoleAdd adds a new role.
      */
    override def roleAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_ADD, options, request)
    }
    
    /** RoleGet gets detailed role information.
      */
    override def roleGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_GET, options, request)
    }
    
    /** RoleList gets lists of all roles.
      */
    override def roleList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_LIST, options, request)
    }
    
    /** RoleDelete deletes a specified role.
      */
    override def roleDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_DELETE, options, request)
    }
    
    /** RoleGrantPermission grants a permission of a specified key or range to a specified role.
      */
    override def roleGrantPermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_GRANT_PERMISSION, options, request)
    }
    
    /** RoleRevokePermission revokes a key or range permission of a specified role.
      */
    override def roleRevokePermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest): com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse = {
      _root_.scalapb.grpc.ClientCalls.blockingUnaryCall(channel, METHOD_ROLE_REVOKE_PERMISSION, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): AuthBlockingStub = new AuthBlockingStub(channel, options)
  }
  
  class AuthStub(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions = _root_.io.grpc.CallOptions.DEFAULT) extends _root_.io.grpc.stub.AbstractStub[AuthStub](channel, options) with Auth {
    /** AuthEnable enables authentication.
      */
    override def authEnable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthEnableResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_AUTH_ENABLE, options, request)
    }
    
    /** AuthDisable disables authentication.
      */
    override def authDisable(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthDisableResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_AUTH_DISABLE, options, request)
    }
    
    /** Authenticate processes an authenticate request.
      */
    override def authenticate(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthenticateResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_AUTHENTICATE, options, request)
    }
    
    /** UserAdd adds a new user.
      */
    override def userAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserAddResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_ADD, options, request)
    }
    
    /** UserGet gets detailed user information.
      */
    override def userGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGetResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_GET, options, request)
    }
    
    /** UserList gets a list of all users.
      */
    override def userList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserListResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_LIST, options, request)
    }
    
    /** UserDelete deletes a specified user.
      */
    override def userDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserDeleteResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_DELETE, options, request)
    }
    
    /** UserChangePassword changes the password of a specified user.
      */
    override def userChangePassword(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserChangePasswordResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_CHANGE_PASSWORD, options, request)
    }
    
    /** UserGrant grants a role to a specified user.
      */
    override def userGrantRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserGrantRoleResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_GRANT_ROLE, options, request)
    }
    
    /** UserRevokeRole revokes a role of specified user.
      */
    override def userRevokeRole(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthUserRevokeRoleResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_USER_REVOKE_ROLE, options, request)
    }
    
    /** RoleAdd adds a new role.
      */
    override def roleAdd(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleAddResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_ADD, options, request)
    }
    
    /** RoleGet gets detailed role information.
      */
    override def roleGet(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGetResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_GET, options, request)
    }
    
    /** RoleList gets lists of all roles.
      */
    override def roleList(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleListResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_LIST, options, request)
    }
    
    /** RoleDelete deletes a specified role.
      */
    override def roleDelete(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleDeleteResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_DELETE, options, request)
    }
    
    /** RoleGrantPermission grants a permission of a specified key or range to a specified role.
      */
    override def roleGrantPermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleGrantPermissionResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_GRANT_PERMISSION, options, request)
    }
    
    /** RoleRevokePermission revokes a key or range permission of a specified role.
      */
    override def roleRevokePermission(request: com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionRequest): scala.concurrent.Future[com.github.fit51.reactiveconfig.etcd.gen.rpc.AuthRoleRevokePermissionResponse] = {
      _root_.scalapb.grpc.ClientCalls.asyncUnaryCall(channel, METHOD_ROLE_REVOKE_PERMISSION, options, request)
    }
    
    override def build(channel: _root_.io.grpc.Channel, options: _root_.io.grpc.CallOptions): AuthStub = new AuthStub(channel, options)
  }
  
  def bindService(serviceImpl: Auth, executionContext: scala.concurrent.ExecutionContext): _root_.io.grpc.ServerServiceDefinition = Auth.bindService(serviceImpl, executionContext)
  
  def blockingStub(channel: _root_.io.grpc.Channel): AuthBlockingStub = new AuthBlockingStub(channel)
  
  def stub(channel: _root_.io.grpc.Channel): AuthStub = new AuthStub(channel)
  
  def javaDescriptor: _root_.com.google.protobuf.Descriptors.ServiceDescriptor = com.github.fit51.reactiveconfig.etcd.gen.rpc.RpcProto.javaDescriptor.getServices().get(5)
  
}