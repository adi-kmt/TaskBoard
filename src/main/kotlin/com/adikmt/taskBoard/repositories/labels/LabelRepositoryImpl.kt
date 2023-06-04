package com.adikmt.taskBoard.repositories.labels

import com.adikmt.taskBoard.dtos.requests.LabelRequest
import jooq.generated.tables.Labels.Companion.LABELS
import jooq.generated.tables.records.LabelsRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LabelRepositoryImpl(@Autowired private val context: DSLContext) : LabelRepository {

    override fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<Int?> {
        return try {
            val labelId: Int? = context?.insertInto<LabelsRecord>(LABELS)
                ?.set(LABELS.LABEL_NAME, labelRequest.name)
                ?.set(LABELS.LABEL_COLOUR, labelRequest.colour)
                ?.onDuplicateKeyIgnore()
                ?.returningResult<Int>(LABELS.ID)
                ?.execute()
            DbResponseWrapper<Int?>(
                data = labelId
            )
        } catch (e: Exception) {
            DbResponseWrapper(
                exception = e
            )
        }
    }

    override fun allLabels(): DbResponseWrapper<List<LabelsRecord>?> =
        try {
            val labelList: List<LabelsRecord>? = context
                .select(LABELS.ID, LABELS.LABEL_NAME, LABELS.LABEL_COLOUR)
                ?.fetchStreamInto(LabelsRecord::class.java)
                ?.toList()
            DbResponseWrapper<List<LabelsRecord>?>(
                data = labelList
            )
        } catch (e: Exception) {
            DbResponseWrapper(
                exception = e
            )
        }
}
