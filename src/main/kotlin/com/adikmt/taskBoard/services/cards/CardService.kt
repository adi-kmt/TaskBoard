package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import kotlinx.coroutines.flow.Flow

interface CardService {

    fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int?>

    fun getAllCards(boardId: Int): Flow<DbResponseWrapper<CardResponse>>

    fun getAllCardsAssignedToUserById(userId: Int, boardId: Int): Flow<DbResponseWrapper<CardResponse>>

    fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Int?>

    fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Int?>

    fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Int?>
}