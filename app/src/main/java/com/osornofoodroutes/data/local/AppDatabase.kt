package com.osornofoodroutes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.osornofoodroutes.data.local.dao.FoodPlaceDao
import com.osornofoodroutes.data.local.dao.RouteDao
import com.osornofoodroutes.data.local.dao.UserDao
import com.osornofoodroutes.data.local.entity.FoodPlaceEntity
import com.osornofoodroutes.data.local.entity.RouteEntity
import com.osornofoodroutes.data.local.entity.UserEntity

/**
 * Base de datos Room de la aplicación.
 * Patrón Singleton: Una sola instancia en toda la app.
 */
@Database(
    entities = [UserEntity::class, FoodPlaceEntity::class, RouteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun foodPlaceDao(): FoodPlaceDao
    abstract fun routeDao(): RouteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos (Singleton).
         * Thread-safe gracias a synchronized.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "osorno_food_routes_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
