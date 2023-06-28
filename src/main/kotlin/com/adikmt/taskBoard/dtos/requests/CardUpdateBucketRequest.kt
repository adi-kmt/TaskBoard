package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull


class CardUpdateBucketRequest(
    @NotNull
    val id: Int,
    @NotNull
    val newBucketId: Int,
    @NotNull
    val boardId: Int
)