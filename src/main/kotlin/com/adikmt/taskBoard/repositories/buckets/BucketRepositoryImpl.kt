package com.adikmt.taskBoard.repositories.buckets

import com.adikmt.taskBoard.dtos.common.mappers.toBucketResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import jooq.generated.tables.Buckets.Companion.BUCKETS
import jooq.generated.tables.records.BucketsRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BucketRepositoryImpl @Autowired constructor(private val context: DSLContext) : BucketRepository {
    override fun createBucket(bucketRequest: BucketRequest, userId: Int): DbResponseWrapper<Int> {
        return try {
            val bucketId = context.insertInto<BucketsRecord>(BUCKETS)
                .set(BUCKETS.BUCKET_TITLE, bucketRequest.title)
                .set(BUCKETS.BOARD_ID, bucketRequest.boardId)
                .onDuplicateKeyIgnore()
                .returningResult<Int>(BUCKETS.ID)
                .execute()

            DbResponseWrapper.Success(
                data = bucketId
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<List<BucketResponse>> {
        return try {
            val bucketsList = context
                .select(BUCKETS.ID, BUCKETS.BOARD_ID, BUCKETS.BUCKET_TITLE)
                .where(BUCKETS.BOARD_ID.eq(boardId))
                .fetchStreamInto(BucketsRecord::class.java)
                .toList()
                .map {
                    it.toBucketResponse()
                }

            DbResponseWrapper.Success(
                data = bucketsList
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }
}
