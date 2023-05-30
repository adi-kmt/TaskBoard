package com.adikmt.taskBoard.dtos.responses

class CardResponse(
    var cardId: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var isCardArchived: Boolean = false,
    var bucketId: Int = 0
)
