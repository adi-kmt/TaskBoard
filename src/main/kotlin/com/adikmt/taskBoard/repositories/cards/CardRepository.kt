package com.adikmt.taskBoard.repositories.cards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse

interface CardRepository {
    /**
     * APIs to implement
     * C
     * 1. Create Card (Only to be done by admin) - Check if bucket exists
     *
     * R
     * 1. Get All cards (Paginated, sorted by created date) - got by board id
     * 2. Get All cards for a user (Paginated, sorted by created date) - got by board id
     *
     * U
     * 1. Update Card Details - check if bucket exists
     * 2. Change Card's bucket - check if bucket exists
     * 3. Assign to someone else (Only to be done by admin), check if user exists
     *
     * Cannot delete, but archived
     */
    fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int>
    fun getAllCards(boardId: Int): List<DbResponseWrapper<CardResponse>>
    fun getAllCardsAssignedToUserById(userId: Int, boardId: Int): List<DbResponseWrapper<CardResponse>>
    fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Boolean>
    fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Boolean>
    fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Boolean>
}
