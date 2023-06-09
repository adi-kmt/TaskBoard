package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.cards.CardRepository
import com.adikmt.taskBoard.utils.SSEmitterBus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CardServiceImpl @Autowired constructor(
    private val cardRepository: CardRepository,
    private val boardRepository: BoardRepository
) : CardService {

    private val sseEmitter = SSEmitterBus

    override fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId, cardRequest.boardId)

            return when (userRole) {
                is DbResponseWrapper.Success -> {
                    if (userRole.data?.equals(UserRole.ADMIN) == true) {
                        cardRepository.createCard(cardRequest = cardRequest, userId = userId)
                    } else {
                        DbResponseWrapper.UserException(exception = Exception("Non admins can't add cards to board'"))
                    }
                }

                else -> DbResponseWrapper.UserException(exception = Exception("Something went wrong"))
            }
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(exception = e)
        }
    }

    override fun getAllCards(boardId: Int): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            cardRepository.getAllCards(boardId)
        } catch (e: Exception) {
            flow {
                DbResponseWrapper.ServerException(exception = e)
            }
        }
    }

    override fun getAllCardsAssignedToUserById(userId: Int, boardId: Int): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            cardRepository.getAllCardsAssignedToUserById(userId = userId, boardId = boardId)
        } catch (e: Exception) {
            flow {
                DbResponseWrapper.ServerException(exception = e)
            }
        }
    }

    override fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Int?> {
        return try {
            cardRepository.updateCardDetails(cardRequest)
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(exception = e)
        }
    }

    override fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Int?> {
        return try {
            val response = cardRepository.updateCardBucket(cardUpdateBucketRequest)
            sseEmitter.emit(cardUpdateBucketRequest = cardUpdateBucketRequest)
            response
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(exception = e)
        }
    }

    override fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Int?> {
        return try {
            val response = cardRepository.assignCardToAnotherUser(cardUpdateUserRequest)
            sseEmitter.emit(cardUpdateUserRequest = cardUpdateUserRequest)
            response
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(exception = e)
        }
    }
}