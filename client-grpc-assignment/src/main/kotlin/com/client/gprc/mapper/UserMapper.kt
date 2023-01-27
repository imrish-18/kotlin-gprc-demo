package com.client.gprc.mapper

import com.client.gprc.model.User
import com.globallogic.grpc.AuthenticateResponse
import com.globallogic.grpc.CreateUserResponse
import com.globallogic.grpc.GetUserByIdResponse
import com.globallogic.grpc.user_proto



class UserMapper {
    companion object{
        fun buildGetUserByIdResponse(user: User): GetUserByIdResponse =
            GetUserByIdResponse
                .newBuilder()
                .setUser(
                    user_proto.User.newBuilder()
                        .setId(user.userId)
                        .setFirstName(user.firstName)
                        .setLastName(user.lastName)
                        .setEmail(user.email)
                        .setContact(user.contact)
                        .setPassword(user.password)
                        .build()
                )
                .build()

        fun buildCreateUserResponse(user: User): CreateUserResponse =
            CreateUserResponse
                .newBuilder()
                .setUser(
                    user_proto.User.newBuilder()
                        .setId(user.userId)
                        .setFirstName(user.firstName)
                        .setLastName(user.lastName)
                        .setEmail(user.email)
                        .setContact(user.contact)
                        .setPassword(user.password)
                        .build()
                )
                .build()

            fun buildAuthenticationResponse(message: String): AuthenticateResponse =
            AuthenticateResponse
                .newBuilder()
                .setMessage(message)
                .build()

}
}