package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class BucketRequest(
    @NotNull
    @Size(max = 20, min = 5, message = "Enter a bucket name with size between 5 and 20 characters")
    var title: String,
    @NotNull
    var boardId: Int
)