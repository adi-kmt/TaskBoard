package com.adikmt.taskBoard.integrationtest

import com.adikmt.taskBoard.dtos.requests.*
import com.adikmt.taskBoard.dtos.responses.*
import java.time.Duration
import java.time.Instant
import java.util.*

object MockObjects {

    object Board {
        val boardRequest1 = BoardRequest(
            title = "jnksfdkjn",
            isStarred = false
        )
        val boardRequest2 = BoardRequest(
            title = "jnksfdkjkjnfdknn",
            isStarred = false
        )

        val boardResponseList = listOf(
            BoardResponse(
                boardId = 1,
                title = "jnksfdkjn",
                isStarred = false
            ),
            BoardResponse(
                boardId = 2,
                title = "jnksfdkjkjnfdknn",
                isStarred = false
            )
        )
    }

    object Bucket {
        val bucketRequest1 = BucketRequest(
            title = "sdkjnsdknsd",
            boardId = 1
        )
        val bucketRequest2 = BucketRequest(
            title = "kjndfkjnfkjn",
            boardId = 1
        )

        val bucketResponseList = listOf(
            BucketResponse(
                bucketId = 1,
                title = "sdkjnsdknsd",
                boardId = 1
            ),
            BucketResponse(
                bucketId = 2,
                title = "kjndfkjnfkjn",
                boardId = 1
            )
        )
    }

    object Card {
        val cardRequest1 = CardRequest(
            title = "kjnsdfkjnsf",
            description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsv",
            startDate = Date.from(Instant.now()).toString(),
            endDate = Date.from(Instant.now() + Duration.ofDays(3L)).toString(),
            isCardArchived = false,
            boardId = 1,
            bucketId = 1
        )
        val cardRequest2 = CardRequest(
            title = "kjnomsfoijkjnsd",
            description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsvmnmn mn n mn mnmnm",
            startDate = Date.from(Instant.now() + Duration.ofDays(3L)).toString(),
            endDate = Date.from(Instant.now() + Duration.ofDays(7L)).toString(),
            isCardArchived = false,
            boardId = 1,
            bucketId = 2
        )

        val cardResponseList = listOf(
            CardResponse(
                cardId = 1,
                title = "kjnsdfkjnsf",
                description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsv",
                startDate = Date.from(Instant.now()).toString(),
                endDate = Date.from(Instant.now() + Duration.ofDays(3L)).toString(),
                isCardArchived = false,
                bucketId = 1
            ),
            CardResponse(
                cardId = 2,
                title = "kjnomsfoijkjnsd",
                description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsvmnmn mn n mn mnmnm",
                startDate = Date.from(Instant.now() + Duration.ofDays(3L)).toString(),
                endDate = Date.from(Instant.now() + Duration.ofDays(7L)).toString(),
                isCardArchived = false,
                bucketId = 2
            )
        )

        val cardUpdateUserRequest = CardUpdateUserRequest(
            id = 1,
            newUserId = 1
        )

        val cardUpdateBucketRequest = CardUpdateBucketRequest(
            id = 2,
            bucketId = 1
        )

        val cardUpdateRequest = CardUpdateRequest(
            id = 2,
            description = "mmkdmoinsdokmkmokmsd kjdnskjnsdkjn",
            endDate = Date.from(Instant.now() + Duration.ofDays(6L)).toString(),
            isCardArchived = true
        )
    }

    object Label {
        val labelRequest1 = LabelRequest(
            name = "jhbkjbihk",
            colour = "jhbvjh khjkjh"
        )
        val labelRequest2 = LabelRequest(
            name = "oklkmmkvg",
            colour = "mnkjnjgvcfh"
        )

        val labelResponseList = listOf(
            LabelResponse(
                labelId = 1,
                name = "jhbkjbihk",
                colour = "jhbvjh khjkjh"
            ),
            LabelResponse(
                labelId = 1,
                name = "oklkmmkvg",
                colour = "mnkjnjgvcfh"
            )
        )
    }

    object User {
        val userRequest1 = UserRequest(
            userName = "jndfskjn",
            password = "kjnasdkjnads"
        )
        val userRequest2 = UserRequest(
            userName = "kmsdoakkjn",
            password = "ndsakjndakjnad"
        )

        val userResponseList = listOf(
            UserResponse(
                userId = 1,
                userName = "jndfskjn",
                userPassword = "kjnasdkjnads",
                jwtToken = "kjndsfkjnsfd.kjndfskjnds.kjndfskjnsfd"
            ),
            UserResponse(
                userId = 2,
                userName = "kmsdoakkjn",
                userPassword = "ndsakjndakjnad",
                jwtToken = "kjndsfkjnsfd.kjndfskjnds.kjndfskjnsfd"
            )
        )
    }

}