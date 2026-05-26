package com.osornofoodroutes.domain.repository

import com.osornofoodroutes.domain.model.FoodPlace
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del Repositorio de Locales de Comida.
 * Principio DIP: Abstracción para acceso a datos de locales.
 * Principio ISP: Solo métodos de CRUD para FoodPlace.
 */
interface FoodPlaceRepository {
    fun getAllFoodPlaces(): Flow<List<FoodPlace>>
    suspend fun getFoodPlaceById(id: Long): FoodPlace?
    suspend fun getFoodPlacesByCategory(category: String): List<FoodPlace>
    suspend fun insertFoodPlace(foodPlace: FoodPlace): Long
    suspend fun updateFoodPlace(foodPlace: FoodPlace)
    suspend fun deleteFoodPlace(foodPlace: FoodPlace)
    suspend fun searchFoodPlaces(query: String): List<FoodPlace>
}
