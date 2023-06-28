package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import java.time.LocalDateTime

interface CardService {
    val emitter: SSEmitterBus

    fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int>

    fun getAllCards(boardId: Int, limit: Int, seekAfter: LocalDateTime): List<DbResponseWrapper<CardResponse>>

    fun getAllCardsAssignedToUserById(
        userId: Int,
        boardId: Int,
        limit: Int,
        seekAfter: LocalDateTime
    ): List<DbResponseWrapper<CardResponse>>

    fun updateCardDetails(cardRequest: CardUpdateRequest, userId: Int): DbResponseWrapper<Boolean>

    fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest, userId: Int): DbResponseWrapper<Boolean>

    fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest, userId: Int): DbResponseWrapper<Boolean>
}