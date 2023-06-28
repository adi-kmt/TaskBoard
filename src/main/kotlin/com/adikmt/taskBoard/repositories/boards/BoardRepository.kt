package com.adikmt.taskBoard.repositories.boards

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse

interface BoardRepository {
    fun createBoard(boardRequest: BoardRequest, userId: Int): DbResponseWrapper<Int>
    fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardResponse>
    fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardResponse>>
    fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardResponse>>
    fun getUserRoleForBoard(userId: Int, boardId: Int): DbResponseWrapper<UserRole>
}
