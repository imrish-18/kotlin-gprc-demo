package com.client.gprc.service

import com.client.gprc.model.Author
import com.client.gprc.repository.AuthorRepository
import jakarta.validation.Valid
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AuthorService {
    companion object {
        val logger = LogManager.getLogger(UserService::class.java)!!
    }

    /** The author repository.  */
    @Autowired
    private lateinit var  authorRepository: AuthorRepository

    /**
     * Creates the author.
     *
     * @param author the author
     * @return the mono
     */
    fun createAuthor(author: @Valid Author?): Mono<Author> {
        logger.info("[AUTHOR] savings author details {} " + author!!.authorName)
        return authorRepository.save(author)
    }

    /**
     * Gets the all authors.
     *
     * @return the all authors
     */
    fun getAllAuthors(): Flux<Author> {
        logger.info("[AUTHOR] getting all author details ")
        return authorRepository.findAll()
    }

    /**
     * Gets the author by id.
     *
     * @param authorId the id
     * @return the author by id
     */
    fun getAuthorById(authorId: Int): Mono<Author> {
        return authorRepository.findById(authorId)
            .doOnNext { UserService.logger.info("author is exist with user id..{$authorId}") }
    }
}