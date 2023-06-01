package com.adikmt.taskBoard.services.labels

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse

interface LabelService {
    fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<Int?>

    fun allLabels(): DbResponseWrapper<List<LabelResponse>?>
}
