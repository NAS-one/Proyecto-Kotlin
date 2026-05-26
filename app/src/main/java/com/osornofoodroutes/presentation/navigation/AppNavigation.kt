package com.osornofoodroutes.presentation.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.presentation.ui.auth.LoginScreen
import com.osornofoodroutes.presentation.ui.auth.RegisterScreen
import com.osornofoodroutes.presentation.ui.foodplace.FoodPlaceFormScreen
import com.osornofoodroutes.presentation.ui.foodplace.FoodPlaceListScreen
import com.osornofoodroutes.presentation.ui.home.HomeScreen
import com.osornofoodroutes.presentation.ui.map.MapScreen
import com.osornofoodroutes.presentation.ui.route.CreateRouteScreen
import com.osornofoodroutes.presentation.ui.route.RouteListScreen
import com.osornofoodroutes.presentation.viewmodel.AuthViewModel
import com.osornofoodroutes.presentation.viewmodel.FoodPlaceViewModel
import com.osornofoodroutes.presentation.viewmodel.RouteViewModel

/**
 * Navegación principal de la aplicación.
 * Gestiona todas las rutas y pasa los ViewModels a cada pantalla.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    foodPlaceViewModel: FoodPlaceViewModel,
    routeViewModel: RouteViewModel
) {
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val foodPlaceState by foodPlaceViewModel.uiState.collectAsStateWithLifecycle()
    val routeState by routeViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // === LOGIN ===
        composable(Screen.Login.route) {
            // Si ya está logueado, redirigir al Home
            LaunchedEffect(authState.isLoggedIn) {
                if (authState.isLoggedIn) {
                    authState.currentUser?.let { user ->
                        routeViewModel.loadRoutes(user.id)
                    }
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                uiState = authState,
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onClearError = { authViewModel.clearError() }
            )
        }

        // === REGISTER ===
        composable(Screen.Register.route) {
            LaunchedEffect(authState.registrationSuccess) {
                if (authState.registrationSuccess) {
                    authViewModel.clearRegistrationSuccess()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true } // Limpia la pila y va al login
                    }
                }
            }

            RegisterScreen(
                uiState = authState,
                onRegister = { name, email, password ->
                    authViewModel.register(name, email, password)
                },
                onNavigateToLogin = { navController.popBackStack() },
                onClearError = { authViewModel.clearError() }
            )
        }

        // === HOME ===
        composable(Screen.Home.route) {
            val user = authState.currentUser
            if (user != null) {
                HomeScreen(
                    user = user,
                    foodPlaces = foodPlaceState.foodPlaces,
                    routeCount = routeState.routes.size,
                    onNavigateToPlaces = { navController.navigate(Screen.FoodPlaceList.route) },
                    onNavigateToRoutes = { navController.navigate(Screen.RouteList.route) },
                    onNavigateToMap = { navController.navigate(Screen.Map.createRoute()) },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        // === FOOD PLACE LIST ===
        composable(Screen.FoodPlaceList.route) {
            FoodPlaceListScreen(
                uiState = foodPlaceState,
                filteredPlaces = foodPlaceViewModel.getFilteredFoodPlaces(),
                categories = foodPlaceViewModel.getCategories(),
                onCategorySelected = { foodPlaceViewModel.filterByCategory(it) },
                onAddPlace = { navController.navigate(Screen.FoodPlaceForm.createRoute()) },
                onEditPlace = { place ->
                    navController.navigate(Screen.FoodPlaceForm.createRoute(place.id))
                },
                onDeletePlace = { foodPlaceViewModel.deleteFoodPlace(it) },
                onViewOnMap = { place ->
                    navController.navigate(Screen.Map.createRoute(focusPlaceId = place.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // === FOOD PLACE FORM (Create/Edit) ===
        composable(
            route = Screen.FoodPlaceForm.route,
            arguments = listOf(navArgument("placeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getLong("placeId") ?: -1L
            var existingPlace by remember { mutableStateOf<FoodPlace?>(null) }

            // Buscar el local existente si es edición
            LaunchedEffect(placeId) {
                if (placeId > 0) {
                    existingPlace = foodPlaceState.foodPlaces.find { it.id == placeId }
                }
            }

            FoodPlaceFormScreen(
                existingPlace = if (placeId > 0) existingPlace else null,
                onSave = { place ->
                    if (placeId > 0) {
                        foodPlaceViewModel.updateFoodPlace(place)
                    } else {
                        foodPlaceViewModel.addFoodPlace(place)
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // === ROUTE LIST ===
        composable(Screen.RouteList.route) {
            RouteListScreen(
                uiState = routeState,
                allFoodPlaces = foodPlaceState.foodPlaces,
                onCreateRoute = { navController.navigate(Screen.CreateRoute.route) },
                onDeleteRoute = { routeViewModel.deleteRoute(it) },
                onViewRouteOnMap = { route ->
                    navController.navigate(Screen.Map.createRoute(routeId = route.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // === CREATE ROUTE ===
        composable(Screen.CreateRoute.route) {
            val user = authState.currentUser
            if (user != null) {
                CreateRouteScreen(
                    allFoodPlaces = foodPlaceState.foodPlaces,
                    userId = user.id,
                    onSave = { route ->
                        routeViewModel.createRoute(route)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // === MAP ===
        composable(
            route = Screen.Map.route,
            arguments = listOf(
                navArgument("focusPlaceId") { type = NavType.LongType; defaultValue = -1L },
                navArgument("routeId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) { backStackEntry ->
            val focusPlaceId = backStackEntry.arguments?.getLong("focusPlaceId") ?: -1L
            val routeId = backStackEntry.arguments?.getLong("routeId") ?: -1L

            val focusPlace = if (focusPlaceId > 0) {
                foodPlaceState.foodPlaces.find { it.id == focusPlaceId }
            } else null

            val routePlaces = if (routeId > 0) {
                val route = routeState.routes.find { it.id == routeId }
                route?.let { r ->
                    foodPlaceState.foodPlaces.filter { it.id in r.foodPlaceIds }
                }
            } else null

            MapScreen(
                foodPlaces = foodPlaceState.foodPlaces,
                initialFocusPlace = focusPlace,
                routePlaces = routePlaces,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
