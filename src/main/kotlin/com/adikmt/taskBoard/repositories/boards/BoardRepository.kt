package com.adikmt.taskBoard.repositories.boards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse

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
    fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardResponse?>
    fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardResponse>?>
    fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardResponse>?>
    fun getUserRoleForBoard(userId: Int, boardId: Int): DbResponseWrapper<UserRole?>
}
