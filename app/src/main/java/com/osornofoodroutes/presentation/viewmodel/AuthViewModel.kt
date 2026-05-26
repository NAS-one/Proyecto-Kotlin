package com.osornofoodroutes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.osornofoodroutes.domain.model.User
import com.osornofoodroutes.domain.usecase.auth.LoginUseCase
import com.osornofoodroutes.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI de autenticación.
 * Patrón Observer: La UI observa cambios en este estado.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val registrationSuccess: Boolean = false
)

/**
 * ViewModel de autenticación.
 * Patrón MVVM: Actúa como intermediario entre la Vista y el Modelo.
 * Principio SRP: Solo maneja lógica de autenticación.
 */
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = loginUseCase(email, password)) {
                is LoginUseCase.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        currentUser = result.user,
                        isLoggedIn = true,
                        errorMessage = null
                    )
                }
                is LoginUseCase.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = registerUseCase(name, email, password)) {
                is RegisterUseCase.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        registrationSuccess = true,
                        errorMessage = null
                    )
                }
                is RegisterUseCase.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearRegistrationSuccess() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false)
    }

    /**
     * Patrón Factory: Crea instancias del ViewModel con las dependencias necesarias.
     */
    class Factory(
        private val loginUseCase: LoginUseCase,
        private val registerUseCase: RegisterUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(loginUseCase, registerUseCase) as T
        }
    }
}
