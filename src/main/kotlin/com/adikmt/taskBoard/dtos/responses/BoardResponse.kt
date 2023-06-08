package com.adikmt.taskBoard.dtos.responses


class BoardResponse(
    val boardId: Int,
    val title: String?,
    val isStarred: Boolean = false
)
