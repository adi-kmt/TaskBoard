package com.adikmt.taskBoard.dtos.common.mappers

import com.adikmt.taskBoard.dtos.responses.BoardResponse
import jooq.generated.tables.records.BoardsRecord

fun BoardsRecord.toBoardResponse() = BoardResponse(
    boardId = this.id,
    title = this.boardTitle,
    isStarred = this.isBoardStarred
)