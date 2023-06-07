package com.adikmt.taskBoard.services.boards

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.AddUserToBoardRequest
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse

interface BoardService {

    fun createBoard(boardRequest: BoardRequest, userId: Int): DbResponseWrapper<Int>

    fun addUserToBoard(userId: Int, addUserToBoardRequest: AddUserToBoardRequest): DbResponseWrapper<Boolean>

    fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardResponse?>

    fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardResponse>?>

    fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardResponse>?>
}