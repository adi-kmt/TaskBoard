package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.ResponseStatus
import com.adikmt.taskBoard.dtos.common.wrappers.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.wrappers.unwrap
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import com.adikmt.taskBoard.services.cards.CardService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/cards")
class CardController @Autowired constructor(private val cardService: CardService) {

    @PostMapping
    fun createCard(
        @Valid @RequestBody cardRequest: CardRequest
    ): ResponseEntity<ResponseWrapper<Int>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.details as UserDetails).username.toInt()
            cardService.createCard(cardRequest = cardRequest, userId = userId)
                .unwrap(successResponseStatus = ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{boardId}")
    fun getAllCards(
        @PathVariable boardId: Int,
        @RequestParam(required = false) assigneeUserId: Int? = null,
        @RequestParam(required = false) limit: Int = 20,
        @RequestParam seekAfter: LocalDateTime
    ): List<ResponseEntity<ResponseWrapper<CardResponse>>> {
        try {
            return if (assigneeUserId != null) {
                cardService.getAllCardsAssignedToUserById(
                    userId = assigneeUserId,
                    boardId = boardId,
                    limit = limit,
                    seekAfter = seekAfter
                ).map {
                    it.unwrap()
                }
            } else {
                cardService.getAllCards(boardId = boardId, limit = limit, seekAfter = seekAfter)
                    .map {
                        it.unwrap()
                    }
            }
        } catch (e: Exception) {
            return listOf(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @PutMapping("/update")
    fun updateCard(
        @Valid @RequestBody cardUpdateRequest: CardUpdateRequest
    ): ResponseEntity<ResponseWrapper<Boolean>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.details as UserDetails).username.toInt()
            cardService.updateCardDetails(
                cardRequest = cardUpdateRequest,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/updateBucket")
    fun updateCardBucket(
        @Valid @RequestBody cardUpdateBucketRequest: CardUpdateBucketRequest
    ): ResponseEntity<ResponseWrapper<Boolean>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.details as UserDetails).username.toInt()
            cardService.updateCardBucket(
                cardUpdateBucketRequest = cardUpdateBucketRequest,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PatchMapping("/updateUser")
    fun assignCardToAnotherUser(
        @Valid @RequestBody cardUpdateUserRequest: CardUpdateUserRequest
    ): ResponseEntity<ResponseWrapper<Boolean>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.details as UserDetails).username.toInt()
            cardService.assignCardToAnotherUser(
                cardUpdateUserRequest = cardUpdateUserRequest,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}