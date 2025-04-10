package com.koru.capital.auth.presentation.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.auth.presentation.login.viewmodel.LoginUiState
import com.koru.capital.core.ui.theme.KoruTheme

@Preview(showBackground = true, name = "Login - Empty")
@Composable
private fun LoginScreenEmptyPreview() {
    KoruTheme {
        LoginScreenContent(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - Filled")
@Composable
private fun LoginScreenFilledPreview() {
    KoruTheme {
        LoginScreenContent(
            uiState = LoginUiState(email = "test@example.com", password = "password"),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - Loading")
@Composable
private fun LoginScreenLoadingPreview() {
    KoruTheme {
        LoginScreenContent(
            uiState = LoginUiState(isLoading = true),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}

@Preview(showBackground = true, name = "Login - Error")
@Composable
private fun LoginScreenErrorPreview() {
    KoruTheme {
        LoginScreenContent(
            uiState = LoginUiState(errorMessage = "Usuario o contraseña incorrectos."),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}