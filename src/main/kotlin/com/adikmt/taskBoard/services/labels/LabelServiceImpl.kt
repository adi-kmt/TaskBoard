package com.adikmt.taskBoard.services.labels

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.labels.LabelRepository
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LabelServiceImpl
@Autowired constructor(
    private val labelRepository: LabelRepository,
    private val boardRepository: BoardRepository
) : LabelService {

    override fun createLabel(labelRequest: LabelRequest, userId: Int, boardId: Int): DbResponseWrapper<Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId = userId, boardId = boardId)
            userRole.data?.let { role ->
                if (role == UserRole.ADMIN) {
                    return labelRepository.createLabel(labelRequest)
                } else {
                    return DbResponseWrapper(exception = Exception("Only admins can create labels"))
                }
            } ?: return DbResponseWrapper(exception = userRole.exception)
        } catch (e: Exception) {
            return DbResponseWrapper(exception = e)
        }
    }

    override fun getAllLabels(): DbResponseWrapper<List<LabelResponse>?> {
        return try {
            labelRepository.getAllLabels()
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }
}