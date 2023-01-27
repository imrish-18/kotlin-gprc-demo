package com.client.gprc.servicegrpc

import com.globallogic.grpc.AuthenticateRequest
import com.globallogic.grpc.CreateUserRequest
import com.globallogic.grpc.GetUserByIdRequest
import com.globallogic.grpc.ReactorUserServiceGrpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserGrpcServiceTest {

    private  lateinit var userGrpcService:  ReactorUserServiceGrpc.ReactorUserServiceStub

    @BeforeAll
    fun setUp()
    {
        val managedChannel: ManagedChannel =
            ManagedChannelBuilder.forAddress("localhost",9890)
                .usePlaintext().build()
        this.userGrpcService=ReactorUserServiceGrpc.newReactorStub(managedChannel)
    }

    @Test
    fun createUser(){
       val request:CreateUserRequest =  CreateUserRequest.newBuilder()
            .setFirstName("rishabh")
            .setLastName("sharma")
            .setContact("9012790090")
            .setEmail("rishabh.kumar@globallogic.com")
            .setPassword("password@12")
            .build()

       val response= this.userGrpcService.createUser(Mono.just(request))
       StepVerifier.create(response).assertNext{userResponse->
           Assertions.assertEquals("9012790090",userResponse.user.contact)

       }
    }
    @Test
    fun getUserByIdTest(){
        val request:GetUserByIdRequest = GetUserByIdRequest.newBuilder().setUserId(123)
            .build()

        val response= this.userGrpcService.getUserById(Mono.just(request))

        StepVerifier.create(response).assertNext{res->
            Assertions.assertEquals(123,res.user.id)
        }
    }

    @Test
    fun authenticateUserCredentials(){
        val request:AuthenticateRequest = AuthenticateRequest.newBuilder().
        setUserName("rishabh sharma")
            .setPassword("password123")
            .build()
        val response= this.userGrpcService.authenticateUserCredentials(Mono.just(request))
        StepVerifier.create(response).assertNext{
            Assertions.assertNotNull(it.message)
            Assertions.assertEquals("",it.message)
        }
    }
}
