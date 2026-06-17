package com.osornofoodroutes.backend.service

import com.osornofoodroutes.backend.dto.RouteRequest
import com.osornofoodroutes.backend.dto.RouteResponse
import com.osornofoodroutes.backend.repository.FoodPlaceRepository
import com.osornofoodroutes.backend.repository.RouteRepository

/**
 * Servicio de rutas gastronómicas.
 * Contiene las reglas de negocio y validaciones.
 * Es el equivalente a los UseCases del frontend.
 */
class RouteService(
    private val routeRepository: RouteRepository,
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Crear una nueva ruta.
     * Reglas: nombre obligatorio, debe tener al menos 2 locales,
     * y todos los locales deben existir en la base de datos.
     */
    fun create(userId: Int, request: RouteRequest): Result<Int> {
        // Validación: nombre obligatorio
        if (request.name.isBlank()) {
            return Result.failure(Exception("El nombre de la ruta es obligatorio"))
        }

        // Validación: al menos 2 locales en la ruta
        if (request.foodPlaceIds.size < 2) {
            return Result.failure(Exception("La ruta debe tener al menos 2 locales"))
        }

        // Validación: todos los locales deben existir
        for (placeId in request.foodPlaceIds) {
            if (foodPlaceRepository.getById(placeId) == null) {
                return Result.failure(Exception("El local con id $placeId no existe"))
            }
        }

        val id = routeRepository.insert(
            name = request.name,
            description = request.description,
            userId = userId,
            foodPlaceIds = request.foodPlaceIds,
            estimatedTime = request.estimatedTime
        ) ?: return Result.failure(Exception("Error al crear la ruta"))

        return Result.success(id)
    }

    fun getByUserId(userId: Int): List<RouteResponse> = routeRepository.getByUserId(userId)

    fun getAll(): List<RouteResponse> = routeRepository.getAll()

    /**
     * Eliminar una ruta.
     * Regla: la ruta debe existir y pertenecer al usuario.
     */
    fun delete(id: Int, userId: Int): Result<Boolean> {
        val route = routeRepository.getById(id)
            ?: return Result.failure(Exception("La ruta con id $id no existe"))

        // Validación: solo el dueño puede eliminar su ruta
        if (route.userId != userId) {
            return Result.failure(Exception("No tienes permiso para eliminar esta ruta"))
        }

        return Result.success(routeRepository.deleteById(id))
    }
}
