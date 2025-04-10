package com.koru.capital.auth.presentation.register.ui.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
// No necesitas hiltViewModel aquí si lo recibes como parámetro
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.auth.presentation.register.viewmodel.RegisterNavigationEvent
import com.koru.capital.auth.presentation.register.viewmodel.RegisterViewModel
// No necesitas hiltViewModelScoped aquí
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterEmailPasswordScreen(
    // Volver a añadir el ViewModel como parámetro
    viewModel: RegisterViewModel,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Ya no obtienes el viewModel aquí, lo recibes como parámetro
    // val viewModel: RegisterViewModel = hiltViewModel() // Eliminar esta línea

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle navigation events (SOLO los que el ViewModel debe controlar)
    LaunchedEffect(Unit) { // Use Unit to launch only once
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is RegisterNavigationEvent.NavigateToPersonalInfo -> {
                    // El ViewModel indica que se debe ir al siguiente paso
                    onNavigateToPersonalInfo()
                }
                // Asegurarse que el caso NavigateToLogin NO esté aquí
                is RegisterNavigationEvent.NavigateToLogin -> {
                    // Este evento solo se emite al COMPLETAR el registro (en PersonalInfoScreen)
                    // No debería hacer nada aquí.
                }
            }
        }
    }

    RegisterEmailPasswordContent(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onContinueClick = viewModel::proceedToPersonalInfo, // VM decide si emitir evento para ir a PersonalInfo
        onLoginClick = onNavigateToLogin // Usar directamente la lambda de NavigationWrapper para el clic
    )
}