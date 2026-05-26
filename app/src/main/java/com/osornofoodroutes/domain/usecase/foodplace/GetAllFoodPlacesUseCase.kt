package com.osornofoodroutes.domain.usecase.foodplace

import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todos los locales de comida.
 * Principio SRP: Solo recupera la lista completa.
 */
class GetAllFoodPlacesUseCase(private val repository: FoodPlaceRepository) {
    operator fun invoke(): Flow<List<FoodPlace>> = repository.getAllFoodPlaces()
}
