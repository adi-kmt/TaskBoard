package com.adikmt.taskBoard.repositories.buckets

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import jooq.generated.tables.records.BucketsRecord

interface BucketRepository {
    /**
     * APIs to implement (Only can be done by admin)
     * 1. Create bucket
     * 2. Get All buckets
     */
    fun createBucket(bucketRequest: BucketRequest, userId: Int): DbResponseWrapper<Int?>
    fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<List<BucketsRecord>?>
}
