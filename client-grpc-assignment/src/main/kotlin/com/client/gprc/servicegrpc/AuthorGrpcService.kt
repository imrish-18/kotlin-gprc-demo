package com.client.gprc.servicegrpc

import com.client.gprc.mapper.AuthorMapper
import com.client.gprc.model.Author
import com.client.gprc.service.AuthorService
import com.client.gprc.validations.ValidatingAuthor
import com.globallogic.grpc.*
import com.google.protobuf.Empty
import org.apache.logging.log4j.LogManager
import org.lognet.springboot.grpc.GRpcService
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@GRpcService
class AuthorGrpcService :ReactorAuthorServiceGrpc.AuthorServiceImplBase() {

    @Autowired
    private lateinit var authorService: AuthorService

    companion object {
        val logger = LogManager.getLogger(UserGrpcService::class.java)!!
    }
    @Autowired
    private lateinit var validatingAuthor: ValidatingAuthor

    override fun createAuthor(request: Mono<CreateAuthorRequest>): Mono<CreateAuthorResponse?>? {
        return validatingAuthor.validateAuthorreq(request)
            .flatMap {
              request.flatMap { res -> authorService.createAuthor(buildAuthor(res))
                        .map { author ->
                            AuthorMapper.buildCreateAuthorResponse(author)
                        }
                }.doOnSuccess { result-> UserGrpcService.logger.info("created user  with userId..{${result.id}}",result) }
            }

    }
    override fun getAuthorById(request: Mono<GetAuthorByIdRequest?>?): Mono<AuthorResponse?>?
    {
        return request!!.flatMap { res->authorService.getAuthorById(res!!.id) }
            .switchIfEmpty { Mono.empty() }
            .map { author->AuthorMapper.buildGetAuthorByIdResponse(author ?: emptyAuthor()) }
            .doOnSuccess{response-> logger.info("author by id: {}",response.author)}

    }
    override fun getAllAuthors(request: Mono<Empty?>?): Mono<AuthorsResponse?>?
    {
          return authorService.getAllAuthors().collectList()
            .map { authors->AuthorMapper.buildAllAuthorsResponse(authors) }
            .doOnSuccess { response-> logger.info("all authors are..{}",response!!.authorList) }
    }

    private fun buildAuthor(authorRequest: CreateAuthorRequest)=
        Author(
            authorId = Random().nextInt(),
            authorName = authorRequest.authorName,
            numberOfBooksWritten = authorRequest.numberOfBooksWritten,
            age = authorRequest.age,
            book = ArrayList()
        )

    fun emptyAuthor(): Author =
        Author(
            authorId = Random().nextInt(),
            authorName = "",
            numberOfBooksWritten = 0,
            age = 0,
            book = ArrayList()
        )
    }
