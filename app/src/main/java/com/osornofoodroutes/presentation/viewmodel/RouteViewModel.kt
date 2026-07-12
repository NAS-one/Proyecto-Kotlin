package com.osornofoodroutes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.osornofoodroutes.domain.model.Route
import com.osornofoodroutes.domain.usecase.route.CreateRouteUseCase
import com.osornofoodroutes.domain.usecase.route.DeleteRouteUseCase
import com.osornofoodroutes.domain.usecase.route.GetUserRoutesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI de rutas.
 */
data class RouteUiState(
    val routes: List<Route> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

/**
 * ViewModel para gestionar las rutas de comida.
 */
class RouteViewModel(
    private val getUserRoutesUseCase: GetUserRoutesUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    fun loadRoutes(userId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getUserRoutesUseCase(userId).collect { routes ->
                _uiState.value = _uiState.value.copy(
                    routes = routes,
                    isLoading = false
                )
            }
        }
    }

    fun createRoute(route: Route) {
        viewModelScope.launch {
            when (val result = createRouteUseCase(route)) {
                is CreateRouteUseCase.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Ruta creada correctamente"
                    )
                    loadRoutes(route.userId)
                }
                is CreateRouteUseCase.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun deleteRoute(route: Route) {
        viewModelScope.launch {
            deleteRouteUseCase(route)
            _uiState.value = _uiState.value.copy(
                successMessage = "Ruta eliminada"
            )
            loadRoutes(route.userId)
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    class Factory(
        private val getUserRoutesUseCase: GetUserRoutesUseCase,
        private val createRouteUseCase: CreateRouteUseCase,
        private val deleteRouteUseCase: DeleteRouteUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RouteViewModel(
                getUserRoutesUseCase,
                createRouteUseCase,
                deleteRouteUseCase
            ) as T
        }
    }
}
