package com.adikmt.taskBoard.repositories.labels

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse

interface LabelRepository {
    fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<Int>

    fun getAllLabels(): DbResponseWrapper<List<LabelResponse>>
}
