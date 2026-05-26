package com.osornofoodroutes.domain.usecase.foodplace

import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para actualizar un local de comida.
 */
class UpdateFoodPlaceUseCase(private val repository: FoodPlaceRepository) {
    suspend operator fun invoke(foodPlace: FoodPlace) {
        repository.updateFoodPlace(foodPlace)
    }
}
