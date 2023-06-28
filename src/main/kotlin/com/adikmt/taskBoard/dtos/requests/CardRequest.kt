package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class CardRequest(
    @NotNull
    @Size(max = 30, min = 5, message = "Enter a card name with character size between 5 and 30")
    val title: String,
    @NotNull
    @Size(max = 100, min = 5, message = "Enter a desc with character size between 5 and 30")
    val description: String,
    @NotNull
    val startDate: String,
    @NotNull
    val endDate: String,
    @NotNull
    val isCardArchived: Boolean = false,
    @NotNull
    val boardId: Int,
    @NotNull
    val labelId: Int,
    @NotNull
    val bucketId: Int
)