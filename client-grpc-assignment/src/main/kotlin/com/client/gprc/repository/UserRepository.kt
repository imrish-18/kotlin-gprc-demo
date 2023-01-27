package com.client.gprc.repository
import com.client.gprc.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository :ReactiveMongoRepository<User,Int> {
    fun findByUserNameAndPassword(userName:String,password:String):Mono<User>
}