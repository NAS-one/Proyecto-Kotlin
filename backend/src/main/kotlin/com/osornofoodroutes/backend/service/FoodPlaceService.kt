package com.osornofoodroutes.backend.service

import com.osornofoodroutes.backend.dto.FoodPlaceRequest
import com.osornofoodroutes.backend.dto.FoodPlaceResponse
import com.osornofoodroutes.backend.repository.FoodPlaceRepository

/**
 * Servicio de locales de comida.
 * Contiene las reglas de negocio y validaciones.
 * Es el equivalente a los UseCases del frontend.
 */
class FoodPlaceService(
    private val repository: FoodPlaceRepository
) {

    /**
     * Crear un nuevo local.
     * Reglas: nombre y dirección obligatorios, rating entre 0 y 5.
     */
    fun create(request: FoodPlaceRequest): Result<Int> {
        // Validación: nombre obligatorio
        if (request.name.isBlank()) {
            return Result.failure(Exception("El nombre del local es obligatorio"))
        }

        // Validación: dirección obligatoria
        if (request.address.isBlank()) {
            return Result.failure(Exception("La dirección es obligatoria"))
        }

        // Validación: categoría obligatoria
        if (request.category.isBlank()) {
            return Result.failure(Exception("La categoría es obligatoria"))
        }

        // Validación: rating entre 0 y 5
        if (request.rating < 0f || request.rating > 5f) {
            return Result.failure(Exception("El rating debe estar entre 0 y 5"))
        }

        val id = repository.insert(
            name = request.name,
            description = request.description,
            category = request.category,
            address = request.address,
            latitude = request.latitude,
            longitude = request.longitude,
            rating = request.rating,
            imageUrl = request.imageUrl,
            phone = request.phone,
            openingHours = request.openingHours
        ) ?: return Result.failure(Exception("Error al crear el local"))

        return Result.success(id)
    }

    fun getAll(): List<FoodPlaceResponse> = repository.getAll()

    fun getById(id: Int): FoodPlaceResponse? = repository.getById(id)

    fun getByCategory(category: String): List<FoodPlaceResponse> = repository.getByCategory(category)

    /**
     * Actualizar un local existente.
     * Reglas: el local debe existir, mismas validaciones que crear.
     */
    fun update(id: Int, request: FoodPlaceRequest): Result<Boolean> {
        // Validación: el local debe existir
        if (repository.getById(id) == null) {
            return Result.failure(Exception("El local con id $id no existe"))
        }

        if (request.name.isBlank()) {
            return Result.failure(Exception("El nombre del local es obligatorio"))
        }

        val updated = repository.update(
            id = id,
            name = request.name,
            description = request.description,
            category = request.category,
            address = request.address,
            latitude = request.latitude,
            longitude = request.longitude,
            rating = request.rating,
            imageUrl = request.imageUrl,
            phone = request.phone,
            openingHours = request.openingHours
        )

        return Result.success(updated)
    }

    /**
     * Eliminar un local.
     * Regla: el local debe existir.
     */
    fun delete(id: Int): Result<Boolean> {
        if (repository.getById(id) == null) {
            return Result.failure(Exception("El local con id $id no existe"))
        }
        return Result.success(repository.deleteById(id))
    }
}
