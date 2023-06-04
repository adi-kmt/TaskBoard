package com.adikmt.taskBoard.dtos.common.mappers

import com.adikmt.taskBoard.dtos.responses.CardResponse
import jooq.generated.tables.records.CardsRecord

fun CardsRecord.toCardResponse() = CardResponse(
    cardId = this.id,
    title = this.cardTitle,
    description = this.cardDesc,
    startDate = this.cardStartDate.toString(),
    endDate = this.cardEndDate.toString(),
    isCardArchived = this.isCardArchived,
    bucketId = this.bucketId
)