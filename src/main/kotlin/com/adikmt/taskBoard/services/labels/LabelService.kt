package com.adikmt.taskBoard.services.labels

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse

interface LabelService {
    fun createLabel(labelRequest: LabelRequest, userId:Int, boardId:Int): DbResponseWrapper<Int?>

    fun getAllLabels(): DbResponseWrapper<List<LabelResponse>?>
}
