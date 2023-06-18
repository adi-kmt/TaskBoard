package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import com.adikmt.taskBoard.services.cards.CardService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import reactor.test.StepVerifier

@SpringBootTest
class CardControllerTest {

    private val mockCardService = mockk<CardService>()

    private val cardController = CardController(mockCardService)

    val cardRequest = CardRequest(
        title = "card title",
        description = "card description",
        startDate = "start date",
        endDate = "end date",
        boardId = 1,
        bucketId = 1,
        labelId = 1
    )

    val cardResponse = CardResponse(
        cardId = 1,
        title = "card title",
        description = "card description",
        startDate = "start date",
        endDate = "end date",
        bucketId = 1,
        labelId = 1,
        isCardArchived = false
    )


    @Test
    fun `Create card successfully`() {
        every { mockCardService.createCard(cardRequest = cardRequest, userId = 1) }.returns(
            DbResponseWrapper.Success(data = 1)
        )
        val response = cardController.createCard(cardRequest = cardRequest, userId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.CREATED)
            assert(responseEntity.body?.data == 1)
        }.verifyComplete()
    }

    @Test
    fun `Create card with exception`() {
        every { mockCardService.createCard(cardRequest = cardRequest, userId = 1) }.returns(
            DbResponseWrapper.DBException(exception = Exception("Exception"))
        )
        val response = cardController.createCard(cardRequest = cardRequest, userId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }

    @Test
    fun `Get all cards for a board successfully`() {
        every { mockCardService.getAllCards(boardId = 1) }.returns(
            flow {
                DbResponseWrapper.Success(data = listOf(cardResponse))
            }
        )
        val response = cardController.getAllCards(boardId = 1)

        runBlocking {
            response.collectLatest { responseEntity ->
                assert(responseEntity.statusCode == HttpStatus.OK)
                assert(responseEntity.body?.data == cardResponse)
            }
        }
    }
}