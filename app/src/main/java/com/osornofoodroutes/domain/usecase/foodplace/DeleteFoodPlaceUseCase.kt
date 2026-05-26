package com.osornofoodroutes.domain.usecase.foodplace

import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para eliminar un local de comida.
 */
class DeleteFoodPlaceUseCase(private val repository: FoodPlaceRepository) {
    suspend operator fun invoke(foodPlace: FoodPlace) {
        repository.deleteFoodPlace(foodPlace)
    }
}
