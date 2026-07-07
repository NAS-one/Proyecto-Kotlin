package com.osornofoodroutes.data.repository

import com.osornofoodroutes.data.remote.FoodPlaceApi
import com.osornofoodroutes.data.remote.dto.FoodPlaceRequest
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de locales de comida.
 * Patrón Repository: Abstrae el acceso a la base de datos Room.
 */
class FoodPlaceRepositoryImpl(
    private val foodPlaceApi: FoodPlaceApi
) : FoodPlaceRepository {

    override fun getAllFoodPlaces(): Flow<List<FoodPlace>> {
        return kotlinx.coroutines.flow.flow {
            try {
                val places = foodPlaceApi.getAllFoodPlaces().map { it.toDomain() }
                emit(places)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override suspend fun getFoodPlaceById(id: Long): FoodPlace? {
        return try {
            foodPlaceApi.getFoodPlaceById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getFoodPlacesByCategory(category: String): List<FoodPlace> {
        return try {
            foodPlaceApi.getFoodPlacesByCategory(category).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun insertFoodPlace(foodPlace: FoodPlace): Long {
        return try {
            val response = foodPlaceApi.createFoodPlace(
                FoodPlaceRequest(
                    name = foodPlace.name,
                    description = foodPlace.description,
                    category = foodPlace.category,
                    address = foodPlace.address,
                    latitude = foodPlace.latitude,
                    longitude = foodPlace.longitude,
                    rating = foodPlace.rating,
                    imageUrl = foodPlace.imageUrl,
                    phone = foodPlace.phone,
                    openingHours = foodPlace.openingHours
                )
            )
            if (response.success) 1L else -1L
        } catch (e: Exception) {
            -1L
        }
    }

    override suspend fun updateFoodPlace(foodPlace: FoodPlace) {
        try {
            foodPlaceApi.updateFoodPlace(
                foodPlace.id,
                FoodPlaceRequest(
                    name = foodPlace.name,
                    description = foodPlace.description,
                    category = foodPlace.category,
                    address = foodPlace.address,
                    latitude = foodPlace.latitude,
                    longitude = foodPlace.longitude,
                    rating = foodPlace.rating,
                    imageUrl = foodPlace.imageUrl,
                    phone = foodPlace.phone,
                    openingHours = foodPlace.openingHours
                )
            )
        } catch (e: Exception) {
            // Ignorar por ahora
        }
    }

    override suspend fun deleteFoodPlace(foodPlace: FoodPlace) {
        try {
            foodPlaceApi.deleteFoodPlace(foodPlace.id)
        } catch (e: Exception) {
            // Ignorar por ahora
        }
    }

    override suspend fun searchFoodPlaces(query: String): List<FoodPlace> {
        // En un caso real, el backend tendría un endpoint de búsqueda.
        // Aquí filtramos localmente para simplificar la demo.
        return try {
            val all = foodPlaceApi.getAllFoodPlaces().map { it.toDomain() }
            all.filter { it.name.contains(query, ignoreCase = true) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
