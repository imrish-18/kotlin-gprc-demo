package com.client.gprc.handler
import com.client.gprc.model.User
import com.client.gprc.model.UserDto
import com.client.gprc.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


/**
 * The Class UserHandler.
 */
@Component
class UserHandler {
    /** The user service. */
    @Autowired
    private lateinit var userService: UserService
    /**
     * Data.
     *
     * @param serverRequest the server request
     * @return the mono
     */
    fun createUser(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(UserDto::class.java).flatMap { user: UserDto ->
            ok().contentType(MediaType.APPLICATION_JSON)
                .body(userService.createUser(user), UserDto::class.java)
        }
            .onErrorResume { e :Throwable->
                Mono.just<Any>("Error " + e.message)
                    .flatMap { s: Any? ->
                        status(HttpStatusCode.valueOf(400))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(s!!)
                    }
            }
    }


    /**
     * Gets the user by id.
     *
     * @param serverRequest the server request
     * @return the user by id
     */
    fun getUserById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val userId: String = serverRequest.pathVariable("userId")
        return userService.getUserById(userId.toInt())
            .flatMap { response ->
                ok().body(
                    Mono.just(response),
                    User::class.java::class.java::class.java
                )}
            .switchIfEmpty {
                noContent().build()
            }


    }
    /**
     * Authenticate user.
     *
     * @param serverRequest the server request
     * @return the mono
     */
    fun authenticateUserWithUserName(serverRequest: ServerRequest): Mono<ServerResponse> {
        val userName: String = serverRequest.pathVariable("userName")
        val password: String = serverRequest.pathVariable("password")
        return userService.authenticateUserWithUserName(userName, password)
            . flatMap { response: String ->
                ok().body(
                    Mono.just(response),
                    String::class.java
                )}
            .switchIfEmpty {
                noContent().build()
            }
    }
}