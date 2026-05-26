package com.osornofoodroutes.domain.usecase.route

import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.repository.RouteRepository

/**
 * Caso de uso para crear una nueva ruta.
 */
class CreateRouteUseCase(private val repository: RouteRepository) {

    sealed class Result {
        data class Success(val id: Long) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(route: Route): Result {
        if (route.name.isBlank()) {
            return Result.Error("El nombre de la ruta es obligatorio")
        }
        if (route.foodPlaceIds.isEmpty()) {
            return Result.Error("Debes agregar al menos un local a la ruta")
        }
        val id = repository.insertRoute(route)
        return Result.Success(id)
    }
}
