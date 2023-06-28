package com.adikmt.taskBoard.integrationtest

import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider

fun setupDSL(): DSLContext {
    val dataSource = HikariDataSource()

    dataSource.apply {
        jdbcUrl = "jdbc:mysql://localhost:3306/testDB"
        username = "PUT_YOUR_USERNAME"
        password = "PUT_YOUR_PASSWORD"
        driverClassName = "com.mysql.cj.jdbc.Driver"
    }

    val connection = DataSourceConnectionProvider(dataSource)

    return DSL.using(connection, SQLDialect.MYSQL)
}