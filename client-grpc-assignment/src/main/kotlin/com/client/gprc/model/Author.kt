package com.client.gprc.model

import jakarta.validation.constraints.Size
import org.springframework.data.annotation.Id
import org.springframework.lang.NonNull

data class Author(
    @Id var authorId: Int,
    @get:NonNull
    val authorName: String,
    @get:Size(min = 18, max = 80, message = "age must be between 18 and 80")
    val age: Int,
    var numberOfBooksWritten: Int,
    val book: List<Book>,

    ){
    fun Author(authorId: Int) {
        this.authorId = authorId
    }
}