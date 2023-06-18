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
        username = "root"
        password = "Android=fun1"
        driverClassName = "com.mysql.cj.jdbc.Driver"
    }

    val connection = DataSourceConnectionProvider(dataSource)

    return DSL.using(connection, SQLDialect.MYSQL)
}