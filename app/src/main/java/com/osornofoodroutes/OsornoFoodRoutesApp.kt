package com.osornofoodroutes

import android.app.Application
import com.osornofoodroutes.data.local.AppDatabase
import com.osornofoodroutes.data.local.FoodPlaceSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Clase Application principal.
 * Inicializa la base de datos y precarga datos si es la primera ejecución.
 */
class OsornoFoodRoutesApp : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        seedDatabaseIfNeeded()
    }

    /**
     * Precarga los locales de comida de Osorno si la base de datos está vacía.
     */
    private fun seedDatabaseIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            val foodPlaceDao = database.foodPlaceDao()
            // Verificar con un collect simple del Flow no es ideal,
            // así que usamos una query directa
            val existingPlaces = foodPlaceDao.searchFoodPlaces("")
            if (existingPlaces.isEmpty()) {
                foodPlaceDao.insertAll(FoodPlaceSeeder.getInitialFoodPlaces())
            }
        }
    }
}
