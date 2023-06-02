package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.ResponseStatus
import com.adikmt.taskBoard.dtos.common.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.unwrap
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import com.adikmt.taskBoard.services.cards.CardService
import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/cards")
class CardController @Autowired constructor(private val cardService: CardService) {

    @PostMapping
    fun createCard(
        @Valid @RequestBody cardRequest: CardRequest,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                cardService.createCard(cardRequest = cardRequest, userId = userId)
                    .unwrap(responseStatus = ResponseStatus.CREATED)
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @GetMapping("/{boardId}")
    fun getAllCards(
        @PathVariable boardId: Int,
        @RequestParam(required = false) userId: Int?
    ): Flow<ResponseEntity<ResponseWrapper<CardResponse>>> {
        try {
            val headers = HttpHeaders().also {
                it.add(HttpHeaders.CONTENT_TYPE, APPLICATION_NDJSON_VALUE)
            }
            return if (userId != null) {
                cardService.getAllCardsAssignedToUserById(userId = userId, boardId = boardId).map {
                    it.unwrap(header = headers)
                }
            } else {
                cardService.getAllCards(boardId = boardId).map {
                    it.unwrap(header = headers)
                }
            }
        } catch (e: Exception) {
            return Flux.just<ResponseEntity<ResponseWrapper<CardResponse>>>(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            ).asFlow()
        }
    }

    @PutMapping("/update")
    fun updateCard(
        @Valid @RequestBody cardUpdateRequest: CardUpdateRequest,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                cardService.updateCardDetails(
                    cardRequest = cardUpdateRequest, userId = userId
                ).unwrap()
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @PatchMapping("/updateBucket")
    fun updateCardBucket(
        @Valid @RequestBody cardUpdateBucketRequest: CardUpdateBucketRequest,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                cardService.updateCardBucket(
                    cardUpdateBucketRequest = cardUpdateBucketRequest, userId = userId
                ).unwrap()
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @PatchMapping("/updateUser")
    fun assignCardToAnotherUser(
        @Valid @RequestBody cardUpdateUserRequest: CardUpdateUserRequest,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                cardService.assignCardToAnotherUser(
                    cardUpdateUserRequest = cardUpdateUserRequest, userId = userId
                ).unwrap()
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }
}