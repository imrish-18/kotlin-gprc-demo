package com.client.gprc.validations

import com.globallogic.grpc.CreateUserRequest
import io.grpc.Status
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class UserValidator {
    /**
     * Validates the data in the User creation request
     */
    fun validateUserRequest(req: Mono<CreateUserRequest>): Mono<CreateUserRequest> {

        val pattern = Regex("[A-Za-z]+")
        val emailPattern = Regex("[A-Za-z0-9.]+@[A-Za-z0-9.]+")
        val contactPattern = Regex("[0-9]+")
        return req.flatMap { request ->
           if(request.contact.length!=10)
           {
               return@flatMap Mono.error{ Status.INVALID_ARGUMENT.withDescription("contact length must be 10 ")
                   .asRuntimeException()}
           }
           if(request.firstName.isNullOrBlank() || request.lastName.isNullOrBlank())
          {
             return@flatMap Mono.error{ Status.INVALID_ARGUMENT.withDescription(" First name and Last name cannot be blank")
                   .asRuntimeException()}
           }
            if(request.password.length<=8 || request.password.length>=15){
              return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("password length must be between 8 to 15 and password length is ... "+request.password.length)
                  .asRuntimeException()}
           }
            if (!pattern.matches(request.firstName)) {
              return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("Not valid: First name should contain only characters ")
                  .asRuntimeException()}
            }
            if (!pattern.matches(request.lastName)) {
                return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("Not valid: Last name should contain only characters")
                    .asRuntimeException()}
            }
            if (request.email.isNullOrBlank()) {
                return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("Email cannot be blank")
                    .asRuntimeException()}
            }

            if (request.contact.length != 10 || !contactPattern.matches(request.contact)) {
                return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("Not valid: Please provide a 10 digit numerical contact")
                    .asRuntimeException()}
            }
            if (!emailPattern.matches(request.email)) {
                return@flatMap Mono.error{Status.INVALID_ARGUMENT.withDescription("Not valid: Please check the email provided")
                    .asRuntimeException()}
            }
           else {
               return@flatMap Mono.just(request)
           }
        }
    }


}