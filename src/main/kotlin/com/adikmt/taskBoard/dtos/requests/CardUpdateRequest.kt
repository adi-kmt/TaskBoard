package com.adikmt.taskBoard.dtos.requests


class CardUpdateRequest(
    val id:Int,
    val description: String,
    val endDate: String,
    val isCardArchived: Boolean = false
)