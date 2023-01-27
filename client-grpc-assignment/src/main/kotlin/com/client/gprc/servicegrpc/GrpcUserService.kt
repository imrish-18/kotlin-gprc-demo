package com.client.gprc.servicegrpc

import com.client.gprc.inerceptor.LogGrpcInterceptor
import com.client.gprc.mapper.UserMapper
import com.client.gprc.model.UserDto
import com.client.gprc.service.UserService
import com.client.gprc.validations.UserValidator
import com.globallogic.grpc.AuthenticateRequest
import com.globallogic.grpc.AuthenticateResponse
import com.globallogic.grpc.CreateUserRequest
import com.globallogic.grpc.CreateUserResponse
import com.globallogic.grpc.GetUserByIdRequest
import com.globallogic.grpc.GetUserByIdResponse
import com.globallogic.grpc.ReactorUserServiceGrpc
import io.grpc.Status

import org.apache.logging.log4j.LogManager
import org.lognet.springboot.grpc.GRpcService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.sleuth.annotation.NewSpan
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


@GRpcService(interceptors = [LogGrpcInterceptor::class])
class UserGrpcService: ReactorUserServiceGrpc.UserServiceImplBase(){
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userValidator: UserValidator

    companion object {
        val logger = LogManager.getLogger(UserGrpcService::class.java)!!
    }

    /**
     * Creates a user account
     */
    @NewSpan
    @Override
    override fun createUser(request: Mono<CreateUserRequest>): Mono<CreateUserResponse?>?
    {
        return userValidator.validateUserRequest(request)
            .onErrorMap { err->
                logger.info(err.message)
               err}
                .flatMap { response ->
                    userService.createUser(buildUserDto(response))
                        .map { user ->
                            UserMapper.buildCreateUserResponse(user)
                        }
                }
                .doOnSuccess {  result-> logger.info("created user  with userId..{${result.user.id}}",result.user) }
        }


    /**
     * get User By Id
     */
    @NewSpan
    @Override
    override fun getUserById(request: Mono<GetUserByIdRequest>): Mono<GetUserByIdResponse>? {
        return request.flatMap { req->userService.getUserById(req.userId)
            .switchIfEmpty { Mono.empty() }
            .map { user->UserMapper.buildGetUserByIdResponse(user) }}
            .doOnSuccess { response-> logger.info("user by id: {}", response.user)}
    }
    /**
     * Authenticates the user credentials
     */
    @NewSpan
    @Override
    override fun authenticateUserCredentials(request: Mono<AuthenticateRequest>):Mono<AuthenticateResponse>
    {
        return request.flatMap { req->
            userService.authenticateUserWithUserName(req.userName,req.password)
        }.map { user->UserMapper.buildAuthenticationResponse(user) }
            .doOnSuccess { response->response.message }
    }
    fun buildUserDto(userRequest: CreateUserRequest): UserDto =
        UserDto(
            userId = userRequest.userId,
            firstName = userRequest.firstName,
            lastName = userRequest.lastName,
            email = userRequest.email,
            contact = userRequest.contact,
            password = userRequest.password
        )
}