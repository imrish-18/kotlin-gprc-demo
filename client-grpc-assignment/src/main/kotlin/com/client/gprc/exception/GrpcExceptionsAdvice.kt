//package com.client.gprc.exception
//
//import io.grpc.Status
//import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler
//import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice
//
//@GRpcServiceAdvice
//class GrpcExceptionsAdvice {
//
//    @GRpcExceptionHandler
//    open fun handleInvalidArgument(e: IllegalArgumentException?): Status? {
//        return Status.INVALID_ARGUMENT.withDescription("Your description").withCause(e)
//    }
//}