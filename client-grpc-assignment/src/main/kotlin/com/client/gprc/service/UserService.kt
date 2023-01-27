package com.client.gprc.service
import com.client.gprc.model.User
import com.client.gprc.model.UserDto
import com.client.gprc.repository.UserRepository
import org.springframework.stereotype.Service
import jakarta.validation.Valid
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


// TODO: Auto-generated Javadoc
/**
 * The Class UserService.
 */
@Service
@Validated
@Transactional
class UserService {

    companion object {
        val logger = LogManager.getLogger(UserService::class.java)!!
    }

    /** The user repository. */
    @Autowired
    private lateinit var userRepository: UserRepository


    /**
     * Creates the user.
     *
     * @param userDto the user
     * @return the mono
     */
    fun createUser(@Valid userDto: UserDto) :Mono<User>{

        logger.info("[userService] checking user details with email id ${userDto.email}")

        return   userRepository.findByUserNameAndPassword(userDto.firstName + userDto.lastName, userDto.password)
             .doOnNext{logger.info("[userService] we have user details  already with email id ${userDto.email}")}
                .switchIfEmpty { val user = User(
                    userDto.userId, userDto.firstName, userDto.lastName, userDto.firstName + userDto.lastName,
                    userDto.password, userDto.contact, userDto.email
                )
                    logger.info("[userService] saving user details with email id ${user.email}")
                    return@switchIfEmpty userRepository.save(user)
                        .doOnSuccess { logger.info("[userService] saving user details into database..${user.email}") }
                        .onErrorReturn(user)
                }
        }
        /**
         * Gets the user by id.
         *
         * @param userId the id
         * @return the user by id
         */
        fun getUserById(userId: Int): Mono<User> {
            logger.info("[USER SERVICE] validated user credentials $userId")
            return userRepository.findById(userId)
                .doOnNext { logger.info("user is exist with user id..{$userId}") }
        }

        /**
         * Authenticate user with username.
         *
         * @param userName the username
         * @param password the password
         * @return the mono
         */
        fun authenticateUserWithUserName(userName: String, password: String): Mono<String>
        {
            val user: Mono<User> = userRepository.findByUserNameAndPassword(userName, password)
                .switchIfEmpty { Mono.empty() }
            logger.info("[USER SERVICE] validated user credentials $userName")
            return user.hasElement()
                .map { isNotEmpty ->
                    if (isNotEmpty) return@map "user Logged In successfully" else {
                        return@map "invalid userName and password..Please try again.."
                    }
                }
        }

}



