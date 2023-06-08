package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import com.adikmt.taskBoard.services.buckets.BucketService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import reactor.test.StepVerifier

@SpringBootTest
class BucketControllerTest {

    private val mockBucketService = mockk<BucketService>()

    private val bucketController = BucketController(mockBucketService)

    val bucketRequest = BucketRequest(title = "main bucket", boardId = 1)

    val bucketResponse = BucketResponse(bucketId = 1, title = "main bucket", boardId = 1)

    @Test
    fun `create bucket successfully`() {
        every { mockBucketService.createBucket(bucketRequest = bucketRequest, userId = 1) }.returns(
            DbResponseWrapper.Success(data = 1)
        )
        val response = bucketController.createBucket(bucketRequest = bucketRequest, userId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.CREATED)
            assert(responseEntity.body?.data == 1)
        }.verifyComplete()
    }

    @Test
    fun `create bucket with exception`() {
        every { mockBucketService.createBucket(bucketRequest = bucketRequest, userId = 1) }.returns(
            DbResponseWrapper.DBException(exception = Exception("Exception"))
        )
        val response = bucketController.createBucket(bucketRequest = bucketRequest, userId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }

    @Test
    fun `Get all buckets successfully`() {
        every { mockBucketService.getAllBucketsForBoardId(boardId = 1) }.returns(
            DbResponseWrapper.Success(data = listOf(bucketResponse))
        )
        val response = bucketController.getAllBuckets(boardId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.OK)
            assert(responseEntity.body?.data == listOf(bucketResponse))
        }.verifyComplete()
    }

    @Test
    fun `Get all bucket with exception`() {
        every { mockBucketService.getAllBucketsForBoardId(boardId = 1) }.returns(
            DbResponseWrapper.UserException(exception = Exception("Exception"))
        )
        val response = bucketController.getAllBuckets(boardId = 1)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.BAD_REQUEST)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }
}