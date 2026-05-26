package com.osornofoodroutes.domain.usecase.route

import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todas las rutas de un usuario.
 */
class GetUserRoutesUseCase(private val repository: RouteRepository) {
    operator fun invoke(userId: Long): Flow<List<Route>> = repository.getRoutesByUserId(userId)
}
