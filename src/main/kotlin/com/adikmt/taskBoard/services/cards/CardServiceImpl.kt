package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.cards.CardRepository
import jooq.generated.tables.records.CardsRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.server.coRouter

class CardServiceImpl @Autowired constructor(
    private val cardRepository: CardRepository,
    private val boardRepository: BoardRepository
) : CardService {
    override fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId, cardRequest.boardId)
            userRole.data?.let { role ->
                if (role == UserRole.ADMIN) {
                    return cardRepository.createCard(cardRequest = cardRequest, userId = userId)
                } else {
                    return DbResponseWrapper(exception = Exception("Non admins can't add cards to board'"))
                }
            } ?: return DbResponseWrapper(exception = userRole.exception)
        } catch (e: Exception) {
            return DbResponseWrapper(exception = e)
        }
    }

    override fun getAllCards(boardId: Int): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            cardRepository.getAllCards(boardId)
        } catch (e: Exception) {
            flow {
                DbResponseWrapper<CardResponse>(exception = e)
            }
        }
    }

    override fun getAllCardsAssignedToUserById(userId: Int, boardId: Int): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            cardRepository.getAllCardsAssignedToUserById(userId = userId, boardId = boardId)
        } catch (e: Exception) {
            flow {
                DbResponseWrapper<CardResponse>(exception = e)
            }
        }
    }

    override fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Int?> {
        return try {
            cardRepository.updateCardDetails(cardRequest)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Int?> {
        return try {
            cardRepository.updateCardBucket(cardUpdateBucketRequest)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Int?> {
        return try {
            cardRepository.assignCardToAnotherUser(cardUpdateUserRequest)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }
}