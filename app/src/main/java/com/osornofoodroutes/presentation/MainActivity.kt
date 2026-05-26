package com.osornofoodroutes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.osornofoodroutes.OsornoFoodRoutesApp
import com.osornofoodroutes.data.repository.FoodPlaceRepositoryImpl
import com.osornofoodroutes.data.repository.RouteRepositoryImpl
import com.osornofoodroutes.data.repository.UserRepositoryImpl
import com.osornofoodroutes.domain.usecase.auth.LoginUseCase
import com.osornofoodroutes.domain.usecase.auth.RegisterUseCase
import com.osornofoodroutes.domain.usecase.foodplace.AddFoodPlaceUseCase
import com.osornofoodroutes.domain.usecase.foodplace.DeleteFoodPlaceUseCase
import com.osornofoodroutes.domain.usecase.foodplace.GetAllFoodPlacesUseCase
import com.osornofoodroutes.domain.usecase.foodplace.UpdateFoodPlaceUseCase
import com.osornofoodroutes.domain.usecase.route.CreateRouteUseCase
import com.osornofoodroutes.domain.usecase.route.DeleteRouteUseCase
import com.osornofoodroutes.domain.usecase.route.GetUserRoutesUseCase
import com.osornofoodroutes.presentation.navigation.AppNavigation
import com.osornofoodroutes.presentation.theme.OsornoFoodRoutesTheme
import com.osornofoodroutes.presentation.viewmodel.AuthViewModel
import com.osornofoodroutes.presentation.viewmodel.FoodPlaceViewModel
import com.osornofoodroutes.presentation.viewmodel.RouteViewModel

/**
 * Actividad principal de la aplicación.
 *
 * Aquí se configuran manualmente las dependencias (sin Dagger/Hilt para simplicidad).
 * En una versión más avanzada se podría usar inyección de dependencias.
 *
 * Patrón Factory: Se usan ViewModelProvider.Factory para crear los ViewModels.
 * Principio DIP: Los repositorios se inyectan como interfaces en los UseCases.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // === CONFIGURACIÓN DE DEPENDENCIAS (Manual DI) ===
        val app = application as OsornoFoodRoutesApp
        val database = app.database

        // Repositorios (Implementaciones concretas)
        val userRepository = UserRepositoryImpl(database.userDao())
        val foodPlaceRepository = FoodPlaceRepositoryImpl(database.foodPlaceDao())
        val routeRepository = RouteRepositoryImpl(database.routeDao())

        // Use Cases - Auth
        val loginUseCase = LoginUseCase(userRepository)
        val registerUseCase = RegisterUseCase(userRepository)

        // Use Cases - FoodPlace
        val getAllFoodPlacesUseCase = GetAllFoodPlacesUseCase(foodPlaceRepository)
        val addFoodPlaceUseCase = AddFoodPlaceUseCase(foodPlaceRepository)
        val updateFoodPlaceUseCase = UpdateFoodPlaceUseCase(foodPlaceRepository)
        val deleteFoodPlaceUseCase = DeleteFoodPlaceUseCase(foodPlaceRepository)

        // Use Cases - Route
        val getUserRoutesUseCase = GetUserRoutesUseCase(routeRepository)
        val createRouteUseCase = CreateRouteUseCase(routeRepository)
        val deleteRouteUseCase = DeleteRouteUseCase(routeRepository)

        // ViewModels (Patrón Factory)
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModel.Factory(loginUseCase, registerUseCase)
        )[AuthViewModel::class.java]

        val foodPlaceViewModel = ViewModelProvider(
            this,
            FoodPlaceViewModel.Factory(
                getAllFoodPlacesUseCase,
                addFoodPlaceUseCase,
                updateFoodPlaceUseCase,
                deleteFoodPlaceUseCase
            )
        )[FoodPlaceViewModel::class.java]

        val routeViewModel = ViewModelProvider(
            this,
            RouteViewModel.Factory(
                getUserRoutesUseCase,
                createRouteUseCase,
                deleteRouteUseCase
            )
        )[RouteViewModel::class.java]

        // === UI CON COMPOSE ===
        setContent {
            OsornoFoodRoutesTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    authViewModel = authViewModel,
                    foodPlaceViewModel = foodPlaceViewModel,
                    routeViewModel = routeViewModel
                )
            }
        }
    }
}
