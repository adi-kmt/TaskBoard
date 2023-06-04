package com.adikmt.taskBoard.repositories.labels

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse

interface LabelRepository {
    /**
     * APIs to implement
     * 1. Create Labels (Only to be done by admin)
     * 2. Get All labels
     */
    fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<out Int>

    fun allLabels(): DbResponseWrapper<out List<LabelResponse>>
}
