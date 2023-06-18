package com.adikmt.taskBoard.integrationtest.repositories

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.integrationtest.MockObjects
import com.adikmt.taskBoard.integrationtest.setupDSL
import com.adikmt.taskBoard.repositories.buckets.BucketRepository
import com.adikmt.taskBoard.repositories.buckets.BucketRepositoryImpl
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
class BucketRepositoryTest {

    lateinit var context: DSLContext
    lateinit var bucketRepository: BucketRepository

    @BeforeAll
    fun setup() {
        context = setupDSL()
        bucketRepository = BucketRepositoryImpl(context)
    }

    @Test
    fun `Create buckets in board`() {
        val bucketResponse1 = bucketRepository.createBucket(MockObjects.Bucket.bucketRequest1)
        val bucketResponse2 = bucketRepository.createBucket(MockObjects.Bucket.bucketRequest2)

        assert(bucketResponse1 == DbResponseWrapper.Success(1))
        assert(bucketResponse2 == DbResponseWrapper.Success(2))
    }

    @Test
    fun `Create buckets in invalid board`() {
        assertThrows<Exception>(message = "cursor returns no rows") {
            val bucketResponse = bucketRepository.createBucket(MockObjects.Bucket.bucketRequest1.apply {
                this.boardId = 3
            })
            throw (bucketResponse as DbResponseWrapper.ServerException).exception
        }
    }

    @Test
    fun `Get All buckets in filled board`() {
        val bucketResponseList = bucketRepository.getAllBucketsForBoardId(boardId = 1)

        val data = (bucketResponseList as DbResponseWrapper.Success).data

        assert(data.size == 2)
        assert(data == MockObjects.Bucket.bucketResponseList)
    }

    @Test
    fun `Get All buckets in empty board`() {
        val bucketResponseList = bucketRepository.getAllBucketsForBoardId(boardId = 2)

        val data = (bucketResponseList as DbResponseWrapper.Success).data

        assert(data.isEmpty())
    }

    @Test
    fun `Get All buckets in invalid board`() {

        assertThrows<Exception>(message = "cursor returns no rows") {
            val bucketResponseList = bucketRepository.getAllBucketsForBoardId(boardId = 3)

            throw (bucketResponseList as DbResponseWrapper.ServerException).exception
        }
    }
}