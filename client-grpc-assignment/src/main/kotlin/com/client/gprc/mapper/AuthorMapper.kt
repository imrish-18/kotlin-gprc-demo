package com.client.gprc.mapper

import com.client.gprc.model.Author
import com.client.gprc.model.User
import com.globallogic.grpc.*

class AuthorMapper {
    companion object {
        fun buildCreateAuthorResponse(createdAuthor: Author): CreateAuthorResponse =
            CreateAuthorResponse.newBuilder()
                .setId(createdAuthor.authorId ?: 0)
                .setAge(createdAuthor.age)
                .setAuthorName(createdAuthor.authorName)
                .setNumberOfBooksWritten(createdAuthor.numberOfBooksWritten)
                .build();

      fun buildGetAuthorByIdResponse(author:Author): AuthorResponse =
              AuthorResponse.newBuilder().setAuthor(
                  author_proto.Author.newBuilder()
                      .setAge(author.age)
                      .setAuthorName(author.authorName)
                      .setNumberOfBooksWritten(author.numberOfBooksWritten)
                      .setId(author.authorId)
                      .addAllBooks(author.book
                          .map { book ->
                              book_proto.Book.newBuilder()
                                  .setId(book.bookId ?: 0)
                                  .setBookTitle(book.bookTitle)
                                  .setPrice(book.price)
                                  .setRatings(book.ratings)
                                  .setPublishingYear(book.publishingYear)
                                  .build()
                          }
                      ).build()

              ).build()

        fun buildAllAuthorsResponse(allAuthors: List<Author?>): AuthorsResponse? {
            val newBuilder = AuthorsResponse.newBuilder()
            allAuthors
                .forEach {
                    newBuilder.addAuthor(
                        author_proto.Author.newBuilder()
                            .setId(it?.authorId ?: 0)
                            .setAuthorName(it?.authorName ?: "")
                            .setAge(it?.age ?: 0)
                            .setNumberOfBooksWritten(it?.numberOfBooksWritten ?: 0)
                            .addAllBooks(it?.book?.map { book ->
                                book_proto.Book.newBuilder()
                                    .setId(book?.bookId ?: 0)
                                    .setBookTitle(book.bookTitle)
                                    .setPrice(book.price)
                                    .setRatings(book.ratings)
                                    .setPublishingYear(book.publishingYear)
                                    .build()
                            })
                            .build()
                    )
                };
            return newBuilder.build()
        }
    }
    }