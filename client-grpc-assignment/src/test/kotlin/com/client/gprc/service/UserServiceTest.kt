package com.client.gprc.service

import com.client.gprc.model.User
import com.client.gprc.model.UserDto
import com.client.gprc.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    /** The user service. */
    @InjectMocks
    private lateinit var userService: UserService

    /** The user repository. */
    @Mock
    private lateinit var userRepository: UserRepository

    /** The userDto. */
    private lateinit var userDto: UserDto

    /** The user. */
    private lateinit var user: User

    /**
     * setUp.
     */
    @BeforeAll
    fun setUp() {
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

    /**
     * Test create user.
     */
    @Test
    fun testCreateUser() {


        Mockito.`when`(userRepository.save(user)).thenReturn(Mono.just(user))

        Mockito.`when`(userRepository.findByUserNameAndPassword(user.userName, user.password))
            .thenReturn(Mono.just(user))

        StepVerifier.create(userService.createUser(userDto))
            .assertNext { users: User ->
                Assertions.assertNotNull(users)
                Assertions.assertEquals(users.password, "password123")
                Assertions.assertEquals(users, user)
            }.verifyComplete()
    }

        /**
     * Authenticate user with userName test.
     */
    @Test
    fun authenticateUserWithUserNameInvalidTest() {
        Mockito.`when`(userRepository.findByUserNameAndPassword(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.empty())
        StepVerifier.create(userService.authenticateUserWithUserName(Mockito.anyString(), Mockito.anyString()))
            .assertNext { user: String? ->
                Assertions.assertEquals(user, "invalid userName and password..Please try again..")
                Assertions.assertNotNull(user)

      }
}
    /**
     * Authenticate user with userName test.
     */
    @Test
    fun authenticateUserWithUserNameTest() {
        Mockito.`when`(userRepository.findByUserNameAndPassword(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Mono.just(user))
        StepVerifier.create(userService.authenticateUserWithUserName(Mockito.anyString(), Mockito.anyString()))
            .assertNext { user: String? ->
                Assertions.assertEquals(user, "user Logged In successfully")
                Assertions.assertNotNull(user)
            }
    }
        /**
     * Gets the user by id test.
     *
     * @return the user by id test
     */
    @Test
    fun getUserByIdTest() {
        Mockito.`when`(userRepository.findById(Mockito.anyInt())).thenReturn(Mono.just(user))
        StepVerifier.create(userService.getUserById(Mockito.anyInt()))
            .assertNext { user: User? ->
                Assertions.assertNotNull(user)
            }.verifyComplete()
    }
}