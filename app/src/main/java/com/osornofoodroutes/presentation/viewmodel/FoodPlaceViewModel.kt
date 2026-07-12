package com.osornofoodroutes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.osornofoodroutes.domain.model.FoodPlace
import com.osornofoodroutes.domain.usecase.foodplace.AddFoodPlaceUseCase
import com.osornofoodroutes.domain.usecase.foodplace.DeleteFoodPlaceUseCase
import com.osornofoodroutes.domain.usecase.foodplace.GetAllFoodPlacesUseCase
import com.osornofoodroutes.domain.usecase.foodplace.UpdateFoodPlaceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI de locales de comida.
 */
data class FoodPlaceUiState(
    val foodPlaces: List<FoodPlace> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedCategory: String = "Todos"
)

/**
 * ViewModel para gestionar los locales de comida.
 * Implementa CRUD completo.
 */
class FoodPlaceViewModel(
    private val getAllFoodPlacesUseCase: GetAllFoodPlacesUseCase,
    private val addFoodPlaceUseCase: AddFoodPlaceUseCase,
    private val updateFoodPlaceUseCase: UpdateFoodPlaceUseCase,
    private val deleteFoodPlaceUseCase: DeleteFoodPlaceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodPlaceUiState())
    val uiState: StateFlow<FoodPlaceUiState> = _uiState.asStateFlow()

    init {
        loadFoodPlaces()
    }

    private fun loadFoodPlaces() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getAllFoodPlacesUseCase().collect { places ->
                _uiState.value = _uiState.value.copy(
                    foodPlaces = places,
                    isLoading = false
                )
            }
        }
    }

    fun addFoodPlace(foodPlace: FoodPlace) {
        viewModelScope.launch {
            when (val result = addFoodPlaceUseCase(foodPlace)) {
                is AddFoodPlaceUseCase.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Local agregado correctamente"
                    )
                    loadFoodPlaces()
                }
                is AddFoodPlaceUseCase.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun updateFoodPlace(foodPlace: FoodPlace) {
        viewModelScope.launch {
            updateFoodPlaceUseCase(foodPlace)
            _uiState.value = _uiState.value.copy(
                successMessage = "Local actualizado correctamente"
            )
            loadFoodPlaces()
        }
    }

    fun deleteFoodPlace(foodPlace: FoodPlace) {
        viewModelScope.launch {
            deleteFoodPlaceUseCase(foodPlace)
            _uiState.value = _uiState.value.copy(
                successMessage = "Local eliminado"
            )
            loadFoodPlaces()
        }
    }

    fun filterByCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Obtiene las categorías únicas de los locales.
     */
    fun getCategories(): List<String> {
        return listOf("Todos") + _uiState.value.foodPlaces
            .map { it.category }
            .distinct()
            .sorted()
    }

    /**
     * Filtra los locales por categoría seleccionada.
     */
    fun getFilteredFoodPlaces(): List<FoodPlace> {
        return if (_uiState.value.selectedCategory == "Todos") {
            _uiState.value.foodPlaces
        } else {
            _uiState.value.foodPlaces.filter {
                it.category == _uiState.value.selectedCategory
            }
        }
    }

    class Factory(
        private val getAllFoodPlacesUseCase: GetAllFoodPlacesUseCase,
        private val addFoodPlaceUseCase: AddFoodPlaceUseCase,
        private val updateFoodPlaceUseCase: UpdateFoodPlaceUseCase,
        private val deleteFoodPlaceUseCase: DeleteFoodPlaceUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FoodPlaceViewModel(
                getAllFoodPlacesUseCase,
                addFoodPlaceUseCase,
                updateFoodPlaceUseCase,
                deleteFoodPlaceUseCase
            ) as T
        }
    }
}
