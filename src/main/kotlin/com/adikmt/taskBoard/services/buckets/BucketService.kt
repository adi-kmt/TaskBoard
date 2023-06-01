package com.adikmt.taskBoard.services.buckets

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse

interface BucketService {

    fun createBucket(bucketRequest: BucketRequest, userId: Int): DbResponseWrapper<Int?>

    fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<List<BucketResponse>?>
}