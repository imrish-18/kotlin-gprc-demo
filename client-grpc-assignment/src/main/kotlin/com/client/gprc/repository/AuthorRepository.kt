package com.client.gprc.repository

import com.client.gprc.model.Author
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository:ReactiveMongoRepository<Author,Int> {
}