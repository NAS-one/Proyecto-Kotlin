package com.osornofoodroutes.backend.repository

import com.osornofoodroutes.backend.dto.RouteResponse
import com.osornofoodroutes.backend.tables.RoutesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Repositorio de rutas gastronómicas.
 * Habla directamente con la base de datos y le hace consultas SQL.
 */
class RouteRepository {

    fun insert(
        name: String, description: String, userId: Int,
        foodPlaceIds: List<Int>, estimatedTime: String
    ): Int? = transaction {
        val statement = RoutesTable.insert {
            it[RoutesTable.name] = name
            it[RoutesTable.description] = description
            it[RoutesTable.userId] = userId
            it[RoutesTable.foodPlaceIds] = foodPlaceIds.joinToString(",")
            it[RoutesTable.estimatedTime] = estimatedTime
        }
        try {
            statement[RoutesTable.id]
        } catch (e: Exception) {
            RoutesTable.select { RoutesTable.name eq name }.limit(1).firstOrNull()?.get(RoutesTable.id)
        }
    }

    fun getByUserId(userId: Int): List<RouteResponse> = transaction {
        RoutesTable.select { RoutesTable.userId eq userId }
            .map { toResponse(it) }
    }

    fun getById(id: Int): RouteResponse? = transaction {
        RoutesTable.select { RoutesTable.id eq id }
            .map { toResponse(it) }
            .singleOrNull()
    }

    fun getAll(): List<RouteResponse> = transaction {
        RoutesTable.selectAll().map { toResponse(it) }
    }

    fun deleteById(id: Int): Boolean = transaction {
        RoutesTable.deleteWhere { RoutesTable.id eq id } > 0
    }

    private fun toResponse(row: ResultRow): RouteResponse {
        val idsString = row[RoutesTable.foodPlaceIds]
        val idsList = if (idsString.isBlank()) emptyList()
                      else idsString.split(",").mapNotNull { it.trim().toIntOrNull() }

        return RouteResponse(
            id = row[RoutesTable.id],
            name = row[RoutesTable.name],
            description = row[RoutesTable.description],
            userId = row[RoutesTable.userId],
            foodPlaceIds = idsList,
            estimatedTime = row[RoutesTable.estimatedTime]
        )
    }
}

