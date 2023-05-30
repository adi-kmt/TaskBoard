package com.adikmt.taskBoard.dtos.requests


class CardRequest(
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val isCardArchived: Boolean = false,
    val bucketId: Int
)