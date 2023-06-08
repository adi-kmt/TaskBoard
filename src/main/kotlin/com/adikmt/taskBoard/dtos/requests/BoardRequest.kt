package com.adikmt.taskBoard.dtos.requests

import com.adikmt.taskBoard.dtos.common.UserRole
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class BoardRequest(
    @NotNull
    @Size(max = 30, min = 5, message = "Enter a board name with character size between 5 and 30")
    val title: String,
    @NotNull
    val isStarred: Boolean = false,
    @NotNull
    val role: UserRole = UserRole.ADMIN
)