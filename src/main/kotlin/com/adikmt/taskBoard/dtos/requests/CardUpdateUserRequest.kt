package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull


class CardUpdateUserRequest(
    @NotNull
    val id: Int,
    @NotNull
    val newUserId: Int,
    @NotNull
    val boardId: Int
)