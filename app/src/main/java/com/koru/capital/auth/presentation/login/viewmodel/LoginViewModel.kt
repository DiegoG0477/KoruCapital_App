package com.koru.capital.auth.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.model.LoginCredentials
import com.koru.capital.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        val credentials = LoginCredentials(currentState.email, currentState.password)

        if (credentials.email.isBlank() || credentials.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Correo y contraseña son requeridos.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, isLoginSuccess = false) }

        viewModelScope.launch {
            loginUseCase(credentials).fold(
                onSuccess = { authToken ->
                    _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }

                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error de inicio de sesión desconocido"
                        )
                    }
                }
            )
        }
    }
}