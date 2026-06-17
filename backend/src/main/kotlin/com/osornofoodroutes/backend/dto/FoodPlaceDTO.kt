package com.osornofoodroutes.backend.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para locales de comida.
 * Request: lo que envía el cliente para crear/actualizar.
 * Response: lo que devuelve el servidor.
 */

@Serializable
data class FoodPlaceRequest(
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

@Serializable
data class FoodPlaceResponse(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val imageUrl: String,
    val phone: String,
    val openingHours: String
)
