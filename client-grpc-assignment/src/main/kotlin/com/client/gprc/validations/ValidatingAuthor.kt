package com.client.gprc.validations

import com.globallogic.grpc.CreateAuthorRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ValidatingAuthor {
    /**
     * Validates the data in the author creation req
     */
    fun validateAuthorreq(request: Mono<CreateAuthorRequest>): Mono<CreateAuthorRequest> {
        val pattern = Regex("[A-Za-z ]+")

        request.flatMap { req ->
            if (req.authorName.isNullOrBlank()) {
                throw IllegalArgumentException("Missing name")
            }

            if (!pattern.matches(req.authorName)) {
                throw IllegalArgumentException("Not valid Author Name: Should contain only characters")
            }
            if (req.age < 1) {
                throw IllegalArgumentException("Not valid: Age should be greater than 0")
            }
            if (req.numberOfBooksWritten < 1) {
                throw IllegalArgumentException("Not valid: Books number should be greater than 0")
            }
            return@flatMap request
        }
        return request
    }
}