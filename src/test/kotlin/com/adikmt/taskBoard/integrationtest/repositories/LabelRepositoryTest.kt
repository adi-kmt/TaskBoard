package com.adikmt.taskBoard.integrationtest.repositories

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.integrationtest.MockObjects
import com.adikmt.taskBoard.integrationtest.setupDSL
import com.adikmt.taskBoard.repositories.labels.LabelRepository
import com.adikmt.taskBoard.repositories.labels.LabelRepositoryImpl
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.test.context.ActiveProfiles

@JooqTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LabelRepositoryTest {
    lateinit var context: DSLContext

    lateinit var labelRepository: LabelRepository

    @BeforeAll
    fun setup() {
        context = setupDSL()
        labelRepository = LabelRepositoryImpl(context)
    }

    @Test
    fun `Create labels`() {
        val labelResponse1 = labelRepository.createLabel(MockObjects.Label.labelRequest1)
        val labelResponse2 = labelRepository.createLabel(MockObjects.Label.labelRequest2)

        assert(labelResponse1 == DbResponseWrapper.Success(1))
        assert(labelResponse2 == DbResponseWrapper.Success(2))
    }


    @Test
    fun `Get all labels`() {
        val labelResponseList = labelRepository.getAllLabels()

        val data = (labelResponseList as DbResponseWrapper.Success).data

        assert(data.size == 2)
        assert(data == MockObjects.Label.labelResponseList)
    }
}