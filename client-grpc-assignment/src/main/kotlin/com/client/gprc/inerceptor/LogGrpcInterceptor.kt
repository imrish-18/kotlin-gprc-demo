package com.client.gprc.inerceptor

import com.client.gprc.exception.ExceptionHandler
import io.grpc.*
import org.apache.logging.log4j.LogManager
import jakarta.inject.Inject
import org.lognet.springboot.grpc.GRpcGlobalInterceptor
import org.springframework.beans.factory.annotation.Autowired

@GRpcGlobalInterceptor
class LogGrpcInterceptor @Autowired constructor(
    @Inject val handler: ExceptionHandler
): ServerInterceptor {
    companion object {
        val logger = LogManager.getLogger(LogGrpcInterceptor::class.java)!!
    }

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT>? {

        fun handleException(call: ServerCall<ReqT, RespT>, e: Exception) {
            val status = handler.handle(e)
            call.close(status, Metadata())
        }

        val listener: ServerCall.Listener<ReqT> = next.startCall(call, headers)

        return object : ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listener) {
            override fun onHalfClose() {
                try {
                    super.onHalfClose()
                } catch (ex: Exception) {
                    logger.info(
            "service: {}, method: {}, headers: {}",
            call.methodDescriptor.serviceName,
            call.methodDescriptor.bareMethodName,
            headers.toString()
        )
                    handleException(call, ex)
                    throw ex
                }
            }
        }
    }
//    override fun <ReqT, RespT> interceptCall(
//        call: ServerCall<ReqT, RespT>,
//        headers: Metadata,
//        next: ServerCallHandler<ReqT, RespT>
//    ): ServerCall.Listener<ReqT>? {
//        logger.info(
//            "service: {}, method: {}, headers: {}",
//            call.methodDescriptor.serviceName,
//            call.methodDescriptor.bareMethodName,
//            headers.toString()
//        )
//        return next.startCall(call, headers)
//    }
}