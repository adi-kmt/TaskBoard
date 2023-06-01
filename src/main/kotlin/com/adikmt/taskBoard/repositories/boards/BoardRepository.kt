package com.adikmt.taskBoard.repositories.boards

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import jooq.generated.tables.records.BoardsRecord

interface BoardRepository {
    /**
     * APIs to implement (Only as applicable to particular user)
     * 1. Create Board
     * 2. Get Board by ID
     * 3. Search by board by name
     * 4. Get All Boards
     * 5. Get user role for particular board
     */
    fun createBoard(boardRequest: BoardRequest, userId: Int): DbResponseWrapper<Int>
    fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardsRecord?>
    fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardsRecord>?>
    fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardsRecord>?>
    fun getUserRoleForBoard(userId: Int, boardId: Int): DbResponseWrapper<UserRole?>
}
