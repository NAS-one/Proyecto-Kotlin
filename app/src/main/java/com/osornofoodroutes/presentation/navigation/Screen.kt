package com.osornofoodroutes.presentation.navigation

/**
 * Rutas de navegación de la aplicación.
 * Centraliza todas las rutas en un solo lugar.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object FoodPlaceList : Screen("food_places")
    data object FoodPlaceForm : Screen("food_place_form/{placeId}") {
        fun createRoute(placeId: Long = -1L) = "food_place_form/$placeId"
    }
    data object RouteList : Screen("routes")
    data object CreateRoute : Screen("create_route")
    data object Map : Screen("map/{focusPlaceId}/{routeId}") {
        fun createRoute(focusPlaceId: Long = -1L, routeId: Long = -1L) = "map/$focusPlaceId/$routeId"
    }
}
