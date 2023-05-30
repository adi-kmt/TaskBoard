package com.adikmt.taskBoard.dtos.requests

import com.adikmt.taskBoard.dtos.common.UserRole

class BoardRequest(
    val title: String,
    val isStarred: Boolean = false,
    val role: UserRole = UserRole.ADMIN
)