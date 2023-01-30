package com.client.gprc.service


import com.client.gprc.model.User
import com.client.gprc.model.UserDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@WebFluxTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceIntegrationTest {

    @Autowired
    private  lateinit var webTestClient: WebTestClient

    /** The userDto. */
    private lateinit var userDto: UserDto

    /** The user. */
    private lateinit var user: User


    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = UserDto(
            userId = 123,
            firstName = "rishabh",
            lastName = "sharma",
            contact = "8177312312",
            password = "password123",
            email = "rishabh@gmail.com"
        )

        user = User(
            userDto.userId,
            userDto.firstName,
            userDto.lastName,
            userDto.firstName + userDto.lastName,
            userDto.password,
            userDto.contact,
            userDto.email
        )
    }
    @Test
     fun testCreateUser(){
         webTestClient.post()
             .uri("/api/users/add",userDto)
             .contentType(MediaType.APPLICATION_JSON)
             .accept(MediaType.APPLICATION_JSON)
             .body(Mono.just(userDto),UserDto::class.java)
             .exchange()
             .expectStatus().is2xxSuccessful.expectBody(User::class.java)
             .value {
                 Assertions.assertEquals(it.userId, 123)
             }
    }

    @Test
    fun getUserByIdTest(){
        webTestClient.get()
            .uri("/getUser/{userId}", user.userId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(User::class.java)
            .value{
                Assertions.assertEquals(it.userId,123)
            }
    }
}