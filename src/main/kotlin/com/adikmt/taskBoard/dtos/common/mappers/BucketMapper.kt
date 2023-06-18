package com.adikmt.taskBoard.dtos.common.mappers

import com.adikmt.taskBoard.dtos.responses.BucketResponse
import jooq.generated.tables.records.BucketsRecord

fun BucketsRecord.toBucketResponse() = BucketResponse(
    bucketId = this.id,
    title = this.bucketTitle,
    boardId = this.boardId
)