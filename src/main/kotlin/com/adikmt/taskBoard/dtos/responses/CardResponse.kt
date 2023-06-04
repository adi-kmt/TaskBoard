package com.adikmt.taskBoard.dtos.responses

class CardResponse(
    var cardId: Int?,
    var title: String?,
    var description: String?,
    var startDate: String?,
    var endDate: String?,
    var isCardArchived: Boolean?,
    var bucketId: Int?
)
