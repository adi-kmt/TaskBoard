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

    /**
     * 1 Create card
     * 2 Create card in board without bucket
     * 3 Get all cards valid/invalid
     * 4 Get all cards by user valid/invalid
     * 5. Update card
     * 6. Update card bucket
     * 7. Update card assignee
     */

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


}