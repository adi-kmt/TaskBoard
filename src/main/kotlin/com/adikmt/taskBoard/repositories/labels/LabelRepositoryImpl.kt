package com.adikmt.taskBoard.repositories.labels

import com.adikmt.taskBoard.dtos.common.mappers.toLabelResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse
import jooq.generated.tables.Labels.Companion.LABELS
import jooq.generated.tables.records.LabelsRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LabelRepositoryImpl @Autowired constructor(private val context: DSLContext) : LabelRepository {

    override fun createLabel(labelRequest: LabelRequest): DbResponseWrapper<Int?> {
        return try {
            val labelId = context.insertInto<LabelsRecord>(LABELS)
                .set(LABELS.LABEL_NAME, labelRequest.name)
                .set(LABELS.LABEL_COLOUR, labelRequest.colour)
                .onDuplicateKeyIgnore()
                .returning()
                .fetchSingle().id

            DbResponseWrapper.Success(
                data = labelId
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getAllLabels(): DbResponseWrapper<List<LabelResponse>> =
        try {
            val labelList = context
                .select(LABELS.ID, LABELS.LABEL_NAME, LABELS.LABEL_COLOUR)
                .from(LABELS)
                .fetchStreamInto(LabelsRecord::class.java)
                .toList()
                .map {
                    it.toLabelResponse()
                }

            DbResponseWrapper.Success(
                data = labelList
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
}
