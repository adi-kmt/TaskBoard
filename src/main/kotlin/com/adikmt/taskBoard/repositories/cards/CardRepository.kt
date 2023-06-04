package com.adikmt.taskBoard.repositories.cards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    /**
     * APIs to implement
     * C
     * 1. Create Card (Only to be done by admin)
     *
     * R
     * 1. Get All cards (Paginated, sorted by created date) - got by board id
     * 2. Get All cards for a user (Paginated, sorted by created date) - got by board id
     *
     * U
     * 1. Update Card Details (can be done by assignee or admin)
     * 2. Change Card's bucket (can be done by assignee or admin)
     * 3. Assign to someone else (Only to be done by admin)
     *
     * Cannot delete
     */
    fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<out Int>
    fun getAllCards(boardId: Int): Flow<DbResponseWrapper<CardResponse>>
    fun getAllCardsAssignedToUserById(userId: Int, boardId: Int): Flow<DbResponseWrapper<CardResponse>>
    fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<out Int>
    fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<out Int>
    fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<out Int>
}
