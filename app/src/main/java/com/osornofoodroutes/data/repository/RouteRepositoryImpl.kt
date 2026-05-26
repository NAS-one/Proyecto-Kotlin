package com.osornofoodroutes.data.repository

import com.osornofoodroutes.data.local.dao.RouteDao
import com.osornofoodroutes.data.local.toDomain
import com.osornofoodroutes.data.local.toEntity
import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de rutas.
 * Patrón Repository: Abstrae el acceso a la base de datos Room.
 */
class RouteRepositoryImpl(
    private val routeDao: RouteDao
) : RouteRepository {

    override fun getAllRoutes(): Flow<List<Route>> {
        return routeDao.getAllRoutes().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getRoutesByUserId(userId: Long): Flow<List<Route>> {
        return routeDao.getRoutesByUserId(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getRouteById(id: Long): Route? {
        return routeDao.getRouteById(id)?.toDomain()
    }

    override suspend fun insertRoute(route: Route): Long {
        return routeDao.insert(route.toEntity())
    }

    override suspend fun updateRoute(route: Route) {
        routeDao.update(route.toEntity())
    }

    override suspend fun deleteRoute(route: Route) {
        routeDao.delete(route.toEntity())
    }
}
