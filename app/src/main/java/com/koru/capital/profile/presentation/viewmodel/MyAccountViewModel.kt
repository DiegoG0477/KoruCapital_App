package com.koru.capital.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.usecase.LogoutUseCase
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
    val isLoggingOut: Boolean = false
)

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyAccountUiState())
    val uiState: StateFlow<MyAccountUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
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

    fun onLogoutClick() {
        if (_uiState.value.isLoggingOut) return

        _uiState.update { it.copy(isLoggingOut = true, errorMessage = null) }
        viewModelScope.launch {
            logoutUseCase().fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoggingOut = false) }
                },
                onFailure = { exception ->
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
    fun onEditProfileClick() {  }
    fun onSettingsClick() {  }
}