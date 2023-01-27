package com.client.gprc.router

import com.client.gprc.handler.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.*

// TODO: Auto-generated
/**
 * The Class UserRouter.
 */
@Configuration
class UserRouter {

    /**
     * Creates the user.
     *
     * @param userHandler the user handler
     * @return the router function
     */
    @Bean
    fun apiCreateUser(userHandler: UserHandler)= router {
        "/api/users".nest {
            accept(APPLICATION_JSON).nest{
                contentType(APPLICATION_JSON)
                    .nest {
                        POST("/add",userHandler::createUser)
                    }
            }
        }
    }
    /**
     * Gets the user by id.
     *
     * @param userHandler the user handler
     * @return the user by id
     * Authenticate user.
     *
     * @param userHandler the user handler
     * @return the router function
     */
    @Bean
    fun getUserByIdApiRouter(userHandler: UserHandler)= router {
        "/v1/user".nest {
            accept(APPLICATION_JSON).nest{
                contentType(APPLICATION_JSON)
                    .nest {
                        GET("/getUser/{userId}",userHandler::getUserById)
                        GET("/authenticateUserWithUserName/{userName}/{password}",userHandler::authenticateUserWithUserName)
                    }
            }
        }
    }

    /**
     * Creates the user.
     *
     * @param userHandler the user handler
     * @return the router function
     */
    @Bean
    fun routerAddUser(userHandler: com.client.gprc.handler.UserHandler):RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.POST("/create/user")
            .and(RequestPredicates.contentType(APPLICATION_JSON)),
            userHandler::createUser)
    }
}
