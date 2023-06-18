package com.adikmt.taskBoard.integrationtest

import com.adikmt.taskBoard.dtos.requests.*
import com.adikmt.taskBoard.dtos.responses.*
import java.time.LocalDateTime

object MockObjects {

    object Board {
        val boardRequest1 = BoardRequest(
            title = "jnksfdkjn"
        )
        val boardRequest2 = BoardRequest(
            title = "jnksfdkjkjnfdknn"
        )

        val boardResponseList = listOf(
            BoardResponse(
                boardId = 1,
                title = "jnksfdkjn"
            ),
            BoardResponse(
                boardId = 2,
                title = "jnksfdkjkjnfdknn"
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
            startDate = LocalDateTime.now().toString(),
            endDate = LocalDateTime.of(2023, 8, 20, 15, 40).toString(),
            isCardArchived = false,
            boardId = 1,
            bucketId = 1,
            labelId = 1
        )
        val cardRequest2 = CardRequest(
            title = "kjnomsfoijkjnsd",
            description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsvmnmn mn n mn mnmnm",
            startDate = LocalDateTime.of(2023, 8, 20, 15, 40).toString(),
            endDate = LocalDateTime.of(2023, 9, 20, 15, 40).toString(),
            isCardArchived = false,
            boardId = 1,
            bucketId = 2,
            labelId = 2
        )

        val cardRequest3 = CardRequest(
            title = "kjnomsfoijkjnsd",
            description = "dsojnfdkjnskjnfsdkjnsdfkjnsfkjnweoijewonjfdskjnvsjnsvmnmn mn n mn mnmnm",
            startDate = LocalDateTime.of(2023, 8, 20, 15, 40).toString(),
            endDate = LocalDateTime.of(2023, 9, 20, 15, 40).toString(),
            isCardArchived = false,
            boardId = 3,
            bucketId = 4,
            labelId = 2
        )

        val cardResponseList = listOf(
            CardResponse(
                cardId = 1,
                title = "kjnsdfkjnsf",
                description = "mmkdmoinsdokmkmokmsd kjdnskjnsdkjn vskjnkv",
                startDate = LocalDateTime.of(2023, 6, 17, 22, 39, 33).toString(),
                endDate = LocalDateTime.of(2023, 11, 25, 18, 40).toString(),
                isCardArchived = false,
                bucketId = 1,
                labelId = 1
            ),
            CardResponse(
                cardId = 2,
                title = "kjnomsfoijkjnsd",
                description = "mmkdmoinsdokmkmokmsd kjdnskjnsdkjn",
                startDate = LocalDateTime.of(2023, 8, 20, 15, 40).toString(),
                endDate = LocalDateTime.of(2023, 6, 25, 18, 40).toString(),
                isCardArchived = false,
                bucketId = 1,
                labelId = 1
            )
        )

        val cardUpdateUserRequest = CardUpdateUserRequest(
            id = 2,
            newUserId = 1
        )

        val cardUpdateUserRequestInvalid = CardUpdateUserRequest(
            id = 3,
            newUserId = 1
        )

        val cardUpdateBucketRequest = CardUpdateBucketRequest(
            id = 2,
            bucketId = 1
        )

        val cardUpdateBucketRequestInvalid = CardUpdateBucketRequest(
            id = 3,
            bucketId = 1
        )

        val cardUpdateRequest = CardUpdateRequest(
            id = 1,
            description = "mmkdmoinsdokmkmokmsd kjdnskjnsdkjn vskjnkv",
            endDate = LocalDateTime.of(2023, 11, 25, 18, 40).toString(),
            isCardArchived = false,
            labelId = 1
        )
        val cardUpdateRequestInvalid = CardUpdateRequest(
            id = 4,
            description = "mmkdmoinsdokmkmokmsd kjdnskjnsdkjn vskjnkv",
            endDate = LocalDateTime.of(2023, 11, 25, 18, 40).toString(),
            isCardArchived = false,
            labelId = 1
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
                labelId = 2,
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
                userPassword = "kjnasdkjnads"
            ),
            UserResponse(
                userId = 2,
                userName = "kmsdoakkjn",
                userPassword = "ndsakjndakjnad"
            )
        )
    }

}