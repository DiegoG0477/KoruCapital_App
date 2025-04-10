package com.koru.capital.auth.presentation.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.auth.presentation.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit, // Lambda for navigating after successful login (e.g., to Home)
    onNavigateToRegister: () -> Unit // Lambda for navigating to the registration flow
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle successful login navigation
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
            // ViewModel should potentially reset the flag after navigation is triggered
        }
    }

    // Handle navigation events via SharedFlow (alternative)
    /*
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            onLoginSuccess()
        }
    }
    */

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onLoginClick = viewModel::onLoginClick,
        onNavigateToRegister = onNavigateToRegister // Pass the lambda down
    )
}