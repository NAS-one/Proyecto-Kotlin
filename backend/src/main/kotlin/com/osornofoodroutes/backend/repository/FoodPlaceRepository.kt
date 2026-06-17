package com.osornofoodroutes.backend.repository

import com.osornofoodroutes.backend.dto.FoodPlaceResponse
import com.osornofoodroutes.backend.tables.FoodPlacesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Repositorio de locales de comida.
 * Habla directamente con la base de datos y le hace consultas SQL.
 */
class FoodPlaceRepository {

    fun insert(
        name: String, description: String, category: String,
        address: String, latitude: Double, longitude: Double,
        rating: Float, imageUrl: String, phone: String, openingHours: String
    ): Int? = transaction {
        FoodPlacesTable.insert {
            it[FoodPlacesTable.name] = name
            it[FoodPlacesTable.description] = description
            it[FoodPlacesTable.category] = category
            it[FoodPlacesTable.address] = address
            it[FoodPlacesTable.latitude] = latitude
            it[FoodPlacesTable.longitude] = longitude
            it[FoodPlacesTable.rating] = rating
            it[FoodPlacesTable.imageUrl] = imageUrl
            it[FoodPlacesTable.phone] = phone
            it[FoodPlacesTable.openingHours] = openingHours
        }[FoodPlacesTable.id]
    }

    fun getAll(): List<FoodPlaceResponse> = transaction {
        FoodPlacesTable.selectAll().map { toResponse(it) }
    }

    fun getById(id: Int): FoodPlaceResponse? = transaction {
        FoodPlacesTable.select { FoodPlacesTable.id eq id }
            .map { toResponse(it) }
            .singleOrNull()
    }

    fun getByCategory(category: String): List<FoodPlaceResponse> = transaction {
        FoodPlacesTable.select { FoodPlacesTable.category eq category }
            .map { toResponse(it) }
    }

    fun update(
        id: Int, name: String, description: String, category: String,
        address: String, latitude: Double, longitude: Double,
        rating: Float, imageUrl: String, phone: String, openingHours: String
    ): Boolean = transaction {
        FoodPlacesTable.update({ FoodPlacesTable.id eq id }) {
            it[FoodPlacesTable.name] = name
            it[FoodPlacesTable.description] = description
            it[FoodPlacesTable.category] = category
            it[FoodPlacesTable.address] = address
            it[FoodPlacesTable.latitude] = latitude
            it[FoodPlacesTable.longitude] = longitude
            it[FoodPlacesTable.rating] = rating
            it[FoodPlacesTable.imageUrl] = imageUrl
            it[FoodPlacesTable.phone] = phone
            it[FoodPlacesTable.openingHours] = openingHours
        } > 0
    }

    fun deleteById(id: Int): Boolean = transaction {
        FoodPlacesTable.deleteWhere { FoodPlacesTable.id eq id } > 0
    }

    private fun toResponse(row: ResultRow) = FoodPlaceResponse(
        id = row[FoodPlacesTable.id],
        name = row[FoodPlacesTable.name],
        description = row[FoodPlacesTable.description],
        category = row[FoodPlacesTable.category],
        address = row[FoodPlacesTable.address],
        latitude = row[FoodPlacesTable.latitude],
        longitude = row[FoodPlacesTable.longitude],
        rating = row[FoodPlacesTable.rating],
        imageUrl = row[FoodPlacesTable.imageUrl],
        phone = row[FoodPlacesTable.phone],
        openingHours = row[FoodPlacesTable.openingHours]
    )
}

