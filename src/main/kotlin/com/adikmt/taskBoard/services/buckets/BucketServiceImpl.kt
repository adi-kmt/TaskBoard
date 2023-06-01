package com.adikmt.taskBoard.services.buckets

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.buckets.BucketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BucketServiceImpl
    @Autowired constructor(
        private val bucketRepository: BucketRepository,
        private val boardRepository: BoardRepository
    ) : BucketService {
    override fun createBucket(bucketRequest: BucketRequest, userId: Int, boardId: Int): DbResponseWrapper<Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId = userId, boardId = boardId)
            userRole.data?.let { role ->
                if (role == UserRole.ADMIN) {
                    return bucketRepository.createBucket(bucketRequest)
                } else {
                    return DbResponseWrapper(exception = Exception("Only admins can create Buckets"))
                }
            } ?: return DbResponseWrapper(exception = userRole.exception)
        } catch (e: Exception) {
            return DbResponseWrapper(exception = e)
        }
    }

    override fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<List<BucketResponse>?> {
        return try {
            bucketRepository.getAllBucketsForBoardId(boardId)
        } catch (e:Exception) {
            DbResponseWrapper(exception = e)
        }
    }
}