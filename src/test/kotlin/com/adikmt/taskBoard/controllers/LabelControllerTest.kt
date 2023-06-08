package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse
import com.adikmt.taskBoard.services.labels.LabelService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import reactor.test.StepVerifier

@SpringBootTest
class LabelControllerTest {

    private val labelService = mockk<LabelService>()

    private val labelController = LabelController(labelService)

    private val labelRequest = LabelRequest(name = "labelName", colour = "jnfdjn")

    private val labelResponse = LabelResponse(labelId = 1, name = "labelName", colour = "jnfdjn")

    @Test
    fun `create label successfully`() {
        every { labelService.createLabel(labelRequest = labelRequest, userId = 1, boardId = 1) }
            .returns(
                DbResponseWrapper.Success(data = 1)
            )

        val response = labelController.addLabel(labelRequest = labelRequest, userId = 1, boardId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.CREATED)
            assert(responseEntity.body?.data == 1)
        }.verifyComplete()
    }

    @Test
    fun `create label with exception`() {
        every { labelService.createLabel(labelRequest = labelRequest, userId = 1, boardId = 1) }
            .returns(
                DbResponseWrapper.DBException(exception = Exception("Exception"))
            )

        val response = labelController.addLabel(labelRequest = labelRequest, userId = 1, boardId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }

    @Test
    fun `Get all label successfully`() {
        every { labelService.getAllLabels() }
            .returns(
                DbResponseWrapper.Success(data = listOf(labelResponse))
            )

        val response = labelController.getAllLabels()

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.OK)
            assert(responseEntity.body?.data == listOf(labelResponse))
        }.verifyComplete()
    }

    @Test
    fun `Get all label with Exception`() {
        every { labelService.getAllLabels() }
            .returns(
                DbResponseWrapper.DBException(exception = Exception("Exception"))
            )

        val response = labelController.getAllLabels()

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }
}