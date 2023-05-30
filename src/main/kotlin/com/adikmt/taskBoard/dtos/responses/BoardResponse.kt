package com.adikmt.taskBoard.dtos.responses


class BoardResponse(
    val boardId: Int = 0,
    val title: String? = null,
    val isStarred: Boolean = false
)
