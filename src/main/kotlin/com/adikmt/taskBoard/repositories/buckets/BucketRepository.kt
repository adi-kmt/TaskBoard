package com.adikmt.taskBoard.repositories.buckets

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import jooq.generated.tables.records.BucketsRecord

interface BucketRepository {
    /**
     * APIs to implement
     * 1. Create bucket (Only can be done by admin)
     * 2. Get All buckets
     */
    fun createBucket(bucketRequest: BucketRequest, userId: Int): DbResponseWrapper<Int?>
    fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<List<BucketsRecord>?>
}