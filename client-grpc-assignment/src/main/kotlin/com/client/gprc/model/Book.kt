package com.client.gprc.model
import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.Id
import org.springframework.lang.NonNull

data class Book(@Id val bookId:Int,
                @get:NotBlank
                val bookTitle:String,
                @get:NonNull
                val authorId:Int,
                @get:NonNull
                val publishingYear:Int,
                val ratings:Double,
                val price:Double,
                @get:NonNull
                var author: Author

)