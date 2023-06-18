package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


class CardUpdateRequest(
    @NotNull
    val id: Int,
    @NotNull
    @Size(max = 100, min = 5, message = "Enter a desc with character size between 5 and 100")
    val description: String,
    @NotNull
    val endDate: String,
    @NotNull
    val isCardArchived: Boolean = false,
    @NotNull
    val labelId: Int
)