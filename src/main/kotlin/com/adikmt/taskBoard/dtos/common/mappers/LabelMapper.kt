package com.adikmt.taskBoard.dtos.common.mappers

import com.adikmt.taskBoard.dtos.responses.LabelResponse
import jooq.generated.tables.records.LabelsRecord

fun LabelsRecord.toLabelResponse() = LabelResponse(
    labelId = this.id,
    name = this.labelName,
    colour = this.labelColour
)