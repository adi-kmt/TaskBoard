package com.adikmt.taskBoard.services.labels

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse

interface LabelService {
    fun createLabel(labelRequest: LabelRequest, userId: Int, boardId: Int): DbResponseWrapper<out Int?>

    fun getAllLabels(): DbResponseWrapper<out List<LabelResponse>?>
}
