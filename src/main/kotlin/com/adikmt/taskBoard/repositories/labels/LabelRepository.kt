package com.adikmt.taskBoard.repositories.labels

import com.adikmt.taskBoard.dtos.requests.LabelRequest
import jooq.generated.tables.records.LabelsRecord

interface LabelRepository {
    /**
     * APIs to implement
     * 1. Create Labels (Only to be done by admin)
     * 2. Get All labels
     */
    fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<Int?>

    fun allLabels(): DbResponseWrapper<List<LabelsRecord>?>
}
