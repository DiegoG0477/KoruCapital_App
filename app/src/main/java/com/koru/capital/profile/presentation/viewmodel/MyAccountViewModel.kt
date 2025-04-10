package com.koru.capital.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.usecase.LogoutUseCase // <-- Importar
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.usecase.GetMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyAccountUiState(
    val isLoading: Boolean = true,
    val userProfile: UserProfile? = null,
    val errorMessage: String? = null,
    val isLoggingOut: Boolean = false // <-- Añadir estado para el proceso de logout
)

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val logoutUseCase: LogoutUseCase // <-- Inyectar LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyAccountUiState())
    val uiState: StateFlow<MyAccountUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        // ... (sin cambios) ...
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = getMyProfileUseCase()
            result.fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(isLoading = false, userProfile = profile)
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Error al cargar perfil: ${exception.message}")
                    }
                }
            )
        }
    }

    // --- LÓGICA LOGOUT ---
    fun onLogoutClick() {
        if (_uiState.value.isLoggingOut) return // Prevenir clicks múltiples

        _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }
        viewModelScope.launch {
            logoutUseCase().fold(
                onSuccess = {
                    // El token se limpió. La navegación se dispara desde la Screen.
                    // No es necesario hacer más aquí, pero podrías limpiar estado específico del VM si lo hubiera.
                    _uiState.update { it.copy(isLoggingOut = false) } // Resetear estado de carga
                },
                onFailure = { exception ->
                    // Mostrar error si falla la limpieza del token (raro)
                    _uiState.update {
                        it.copy(
                            isLoggingOut = false,
                            errorMessage = "Error al cerrar sesión: ${exception.message}"
                        )
                    }
                }
            )
        }
    }
    // onEditProfileClick y onSettingsClick (placeholders)
    fun onEditProfileClick() { /* Trigger navigation event */ }
    fun onSettingsClick() { /* Trigger navigation event */ }
}