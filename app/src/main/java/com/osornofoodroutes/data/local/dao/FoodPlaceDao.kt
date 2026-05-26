package com.osornofoodroutes.data.local.dao

import androidx.room.*
import com.osornofoodroutes.data.local.entity.FoodPlaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos sobre locales de comida.
 */
@Dao
interface FoodPlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodPlace: FoodPlaceEntity): Long

    @Update
    suspend fun update(foodPlace: FoodPlaceEntity)

    @Delete
    suspend fun delete(foodPlace: FoodPlaceEntity)

    @Query("SELECT * FROM food_places ORDER BY name ASC")
    fun getAllFoodPlaces(): Flow<List<FoodPlaceEntity>>

    @Query("SELECT * FROM food_places WHERE id = :id LIMIT 1")
    suspend fun getFoodPlaceById(id: Long): FoodPlaceEntity?

    @Query("SELECT * FROM food_places WHERE category = :category ORDER BY name ASC")
    suspend fun getFoodPlacesByCategory(category: String): List<FoodPlaceEntity>

    @Query("SELECT * FROM food_places WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchFoodPlaces(query: String): List<FoodPlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foodPlaces: List<FoodPlaceEntity>)
}
