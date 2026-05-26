package com.osornofoodroutes.domain.usecase.route

import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.repository.RouteRepository

/**
 * Caso de uso para eliminar una ruta.
 */
class DeleteRouteUseCase(private val repository: RouteRepository) {
    suspend operator fun invoke(route: Route) {
        repository.deleteRoute(route)
    }
}
