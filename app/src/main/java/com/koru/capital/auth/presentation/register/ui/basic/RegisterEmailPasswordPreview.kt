package com.koru.capital.auth.presentation.register.ui.basic

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.auth.presentation.register.viewmodel.RegisterUiState
import com.koru.capital.core.ui.theme.KoruTheme

@Preview(showBackground = true, name = "Email/Pass - Empty")
@Composable
private fun RegisterEmailPasswordContentEmptyPreview() {
    KoruTheme {
        RegisterEmailPasswordContent(
            uiState = RegisterUiState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onContinueClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Email/Pass - Filled")
@Composable
private fun RegisterEmailPasswordContentFilledPreview() {
    KoruTheme {
        RegisterEmailPasswordContent(
            uiState = RegisterUiState(
                email = "test@example.com",
                password = "password123",
                confirmPassword = "password123"
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onContinueClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Email/Pass - Error")
@Composable
private fun RegisterEmailPasswordContentErrorPreview() {
    KoruTheme {
        RegisterEmailPasswordContent(
            uiState = RegisterUiState(
                email = "test@",
                password = "short",
                confirmPassword = "notsame",
                isEmailValid = false, emailErrorMessage = "Formato inválido",
                isPasswordValid = false, passwordErrorMessage = "Mínimo 8 caracteres",
                isConfirmPasswordValid = false, confirmPasswordErrorMessage = "Las contraseñas no coinciden",
                registrationError = "Por favor corrige los errores"
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onContinueClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Email/Pass - Loading")
@Composable
private fun RegisterEmailPasswordContentLoadingPreview() {
    KoruTheme {
        RegisterEmailPasswordContent(
            uiState = RegisterUiState(isLoading = true),
            onEmailChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onContinueClick = {},
            onLoginClick = {}
        )
    }
}