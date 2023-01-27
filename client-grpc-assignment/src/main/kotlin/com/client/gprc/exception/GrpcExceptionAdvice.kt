package com.client.gprc.exception

import io.grpc.Status
import org.springframework.stereotype.Component

/**
 * Grpc exception handlers
 */
@Component
class ExceptionHandler {
    fun handle(e: Exception): Status {
        val status = when (e) {
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message)
            else -> Status.INVALID_ARGUMENT.withDescription("error message..." + e.message)


        }
        return status.withCause(e)
    }
}