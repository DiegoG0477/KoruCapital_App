package com.koru.capital.auth.presentation.register.ui.basic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.auth.presentation.register.viewmodel.RegisterNavigationEvent
import com.koru.capital.auth.presentation.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterEmailPasswordScreen(
    viewModel: RegisterViewModel,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is RegisterNavigationEvent.NavigateToPersonalInfo -> {
                    onNavigateToPersonalInfo()
                }
                is RegisterNavigationEvent.NavigateToLogin -> {
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
        onContinueClick = viewModel::proceedToPersonalInfo,
        onLoginClick = onNavigateToLogin
    )
}