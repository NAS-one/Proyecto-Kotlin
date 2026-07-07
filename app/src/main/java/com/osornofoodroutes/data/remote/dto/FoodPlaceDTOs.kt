package com.osornofoodroutes.data.remote.dto

import com.osornofoodroutes.domain.model.FoodPlace

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

data class FoodPlaceResponse(
    val id: Long,
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
) {
    fun toDomain(): FoodPlace {
        return FoodPlace(
            id = id,
            name = name,
            description = description,
            category = category,
            address = address,
            latitude = latitude,
            longitude = longitude,
            rating = rating,
            imageUrl = imageUrl,
            phone = phone,
            openingHours = openingHours
        )
    }
}
