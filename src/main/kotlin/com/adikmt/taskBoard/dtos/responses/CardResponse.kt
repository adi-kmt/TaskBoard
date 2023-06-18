package com.adikmt.taskBoard.dtos.responses

data class CardResponse(
    val cardId: Int?,
    val title: String?,
    val description: String?,
    val startDate: String?,
    val endDate: String?,
    val isCardArchived: Boolean?,
    val bucketId: Int?,
    val labelId: Int?
)
