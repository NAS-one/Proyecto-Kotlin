package com.osornofoodroutes.domain.usecase.foodplace

import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para agregar un local de comida.
 * Principio SRP: Solo se encarga de la lógica de inserción.
 */
class AddFoodPlaceUseCase(private val repository: FoodPlaceRepository) {

    sealed class Result {
        data class Success(val id: Long) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(foodPlace: FoodPlace): Result {
        // 1. Validación de Nombre
        when {
            foodPlace.name.isBlank() ->
                return Result.Error("El nombre del local es obligatorio")
            foodPlace.name.length > 50 ->
                return Result.Error("El nombre no puede tener más de 50 caracteres")
        }
        if (foodPlace.address.isBlank()) {
            return Result.Error("La dirección es obligatoria")
        }
        val id = repository.insertFoodPlace(foodPlace)
        return Result.Success(id)
    }
}
