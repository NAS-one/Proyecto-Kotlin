package com.osornofoodroutes.data.local.dao

import androidx.room.*
import com.osornofoodroutes.data.local.entity.RouteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos sobre rutas.
 */
@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: RouteEntity): Long

    @Update
    suspend fun update(route: RouteEntity)

    @Delete
    suspend fun delete(route: RouteEntity)

    @Query("SELECT * FROM routes ORDER BY name ASC")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes WHERE userId = :userId ORDER BY name ASC")
    fun getRoutesByUserId(userId: Long): Flow<List<RouteEntity>>

    @Query("SELECT * FROM routes WHERE id = :id LIMIT 1")
    suspend fun getRouteById(id: Long): RouteEntity?
}
