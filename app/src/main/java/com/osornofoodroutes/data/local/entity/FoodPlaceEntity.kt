package com.osornofoodroutes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para la tabla de locales de comida.
 */
@Entity(tableName = "food_places")
data class FoodPlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val phone: String = "",
    val openingHours: String = ""
)
