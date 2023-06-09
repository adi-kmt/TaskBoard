package com.adikmt.taskBoard.utils

import org.jooq.conf.Settings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class JooqSettings {

    @Bean
    fun jooqSettings(): Settings {
        return Settings()
            .withUpdateRecordTimestamp(true) // this is default, so it can be omitted
            .withExecuteWithOptimisticLocking(true)
            .withExecuteWithOptimisticLockingExcludeUnversioned(true)
    }
}