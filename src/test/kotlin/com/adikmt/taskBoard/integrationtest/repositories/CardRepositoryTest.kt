package com.adikmt.taskBoard.integrationtest.repositories

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.integrationtest.MockObjects
import com.adikmt.taskBoard.integrationtest.setupDSL
import com.adikmt.taskBoard.repositories.cards.CardRepository
import com.adikmt.taskBoard.repositories.cards.CardRepositoryImpl
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@JooqTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardRepositoryTest {

    lateinit var context: DSLContext

    lateinit var cardRepository: CardRepository

    @BeforeAll
    fun setup() {
        context = setupDSL()
        cardRepository = CardRepositoryImpl(context)
    }

    @Test
    fun `Create card`() {
        val cardResponse1 = cardRepository.createCard(MockObjects.Card.cardRequest1, userId = 1)
        val cardResponse2 = cardRepository.createCard(MockObjects.Card.cardRequest2, userId = 2)

        assert(cardResponse1 == DbResponseWrapper.Success(1))
        assert(cardResponse2 == DbResponseWrapper.Success(2))
    }

    @Test
    fun `Create card for invalid bucket and board`() {
        assertThrows<Exception>(message = "cursor returns no rows") {
            val cardResponse = cardRepository.createCard(
                MockObjects.Card.cardRequest3, userId = 2
            )

            throw (cardResponse as DbResponseWrapper.ServerException).exception
        }
    }

    @Test
    fun `Update valid card`() {
        val cardResponse = cardRepository.updateCardDetails(MockObjects.Card.cardUpdateRequest)

        assert(cardResponse == DbResponseWrapper.Success(true))
    }

    @Test
    fun `Update invalid card`() {
        assertThrows<Exception>(message = "No card found to update") {
            val cardResponse = cardRepository.updateCardDetails(MockObjects.Card.cardUpdateRequestInvalid)

            throw (cardResponse as DbResponseWrapper.UserException).exception
        }
    }

    @Test
    fun `Update valid card bucket`() {
        val cardResponse = cardRepository.updateCardBucket(MockObjects.Card.cardUpdateBucketRequest)

        assert(cardResponse == DbResponseWrapper.Success(true))
    }

    @Test
    fun `Update invalid card bucket`() {
        assertThrows<Exception>(message = "No card found to update") {
            val cardResponse = cardRepository.updateCardBucket(MockObjects.Card.cardUpdateBucketRequestInvalid)

            throw (cardResponse as DbResponseWrapper.UserException).exception
        }
    }

    @Test
    fun `Update valid card assignee`() {
        val cardResponse = cardRepository.assignCardToAnotherUser(MockObjects.Card.cardUpdateUserRequest)

        assert(cardResponse == DbResponseWrapper.Success(true))
    }

    @Test
    fun `Update invalid card assignee`() {
        assertThrows<Exception>(message = "No card found to update") {
            val cardResponse = cardRepository.assignCardToAnotherUser(MockObjects.Card.cardUpdateUserRequestInvalid)

            throw (cardResponse as DbResponseWrapper.UserException).exception
        }
    }

    @Test
    fun `get all cards valid`() {
        val cardResponse = cardRepository.getAllCards(boardId = 1, limit = 2, seekAfter = LocalDateTime.now())
        cardResponse.forEachIndexed { index, card ->
            when (index) {
                0 -> assert(card == DbResponseWrapper.Success(MockObjects.Card.cardResponseList[1]))
                1 -> assert(card == DbResponseWrapper.Success(MockObjects.Card.cardResponseList.first()))
            }
        }
    }

    @Test
    fun `get all cards for invalid board`() {
        val cardResponse = cardRepository.getAllCards(boardId = 3, limit = 10, seekAfter = LocalDateTime.MIN)
        assert(cardResponse.isEmpty())
    }

    @Test
    fun `get all cards for board and user assigned`() {
        val cardResponse = cardRepository.getAllCardsAssignedToUserById(
            userId = 1,
            boardId = 1,
            limit = 2,
            seekAfter = LocalDateTime.now()
        )
        cardResponse.forEachIndexed { index, card ->
            when (index) {
                0 -> assert(card == DbResponseWrapper.Success(MockObjects.Card.cardResponseList[1]))
                1 -> assert(card == DbResponseWrapper.Success(MockObjects.Card.cardResponseList.first()))
            }
        }
    }

    @Test
    fun `get all cards by user for invalid data`() {
        val cardResponse = cardRepository.getAllCardsAssignedToUserById(
            userId = 3,
            boardId = 2,
            limit = 2,
            seekAfter = LocalDateTime.now()
        )
        assert(cardResponse.isEmpty())
    }
}