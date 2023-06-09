package com.adikmt.taskBoard.dtos.requests

import com.adikmt.taskBoard.dtos.common.UserRole
class AddUserToBoardRequest(
    val userId: Int,
    val boardId: Int,
    val role: UserRole = UserRole.USER
)