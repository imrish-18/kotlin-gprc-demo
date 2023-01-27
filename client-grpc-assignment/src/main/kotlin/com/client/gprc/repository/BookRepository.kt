package com.client.gprc.repository

import com.client.gprc.model.Book
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface  BookRepository:ReactiveMongoRepository<Book,Int> {
}