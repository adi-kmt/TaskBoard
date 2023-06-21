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
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest
class BucketControllerTest {

    private val mockBucketService = mockk<BucketService>()

    private val bucketController = BucketController(mockBucketService)

    val bucketRequest = BucketRequest(title = "main bucket", boardId = 1)

    val bucketResponse = BucketResponse(bucketId = 1, title = "main bucket", boardId = 1)

    @Test
    @WithMockUser(username = "1")
    fun `create bucket successfully`() {
        every { mockBucketService.createBucket(bucketRequest = bucketRequest, userId = 1) }.returns(
            DbResponseWrapper.Success(data = 1)
        )
        val response = bucketController.createBucket(bucketRequest = bucketRequest)

        assert(response.statusCode == HttpStatus.CREATED)
        assert(response.body?.data == 1)
    }

    @Test
    @WithMockUser(username = "1")
    fun `create bucket with exception`() {
        every { mockBucketService.createBucket(bucketRequest = bucketRequest, userId = 1) }.returns(
            DbResponseWrapper.DBException(exception = Exception("Exception"))
        )
        val response = bucketController.createBucket(bucketRequest = bucketRequest)

        assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
        assert(response.body?.errorMessage == "Exception")
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get all buckets successfully`() {
        every { mockBucketService.getAllBucketsForBoardId(boardId = 1) }.returns(
            DbResponseWrapper.Success(data = listOf(bucketResponse))
        )
        val response = bucketController.getAllBuckets(boardId = 1)

        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.data == listOf(bucketResponse))
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get all bucket with exception`() {
        every { mockBucketService.getAllBucketsForBoardId(boardId = 1) }.returns(
            DbResponseWrapper.UserException(exception = Exception("Exception"))
        )
        val response = bucketController.getAllBuckets(boardId = 1)

        assert(response.statusCode == HttpStatus.BAD_REQUEST)
        assert(response.body?.errorMessage == "Exception")
    }
}