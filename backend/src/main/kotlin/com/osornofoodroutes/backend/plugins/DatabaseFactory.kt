package com.osornofoodroutes.backend.plugins

import com.osornofoodroutes.backend.tables.FoodPlacesTable
import com.osornofoodroutes.backend.tables.RoutesTable
import com.osornofoodroutes.backend.tables.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Conexión a la base de datos Neon (PostgreSQL).
 * Usa HikariCP para manejar el pool de conexiones.
 * Crea las tablas automáticamente si no existen.
 */
object DatabaseFactory {

    fun init(config: ApplicationConfig) {
        val url = config.property("database.url").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()
        val driver = config.property("database.driver").getString()

        // Configurar pool de conexiones con HikariCP
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = driver
            username = user
            this.password = password
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            // Configuración para Neon (serverless)
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        // Crear las tablas si no existen
        transaction {
            SchemaUtils.create(UsersTable, FoodPlacesTable, RoutesTable)
        }

        println("✅ Base de datos conectada exitosamente a Neon PostgreSQL")
        println("✅ Tablas creadas: users, food_places, routes")
    }
}
