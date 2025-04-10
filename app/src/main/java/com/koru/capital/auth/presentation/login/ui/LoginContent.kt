package com.koru.capital.auth.presentation.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.koru.capital.auth.presentation.login.ui.components.LoginBody
import com.koru.capital.auth.presentation.login.ui.components.LoginBottom
import com.koru.capital.auth.presentation.login.ui.components.LoginTop
import com.koru.capital.auth.presentation.login.viewmodel.LoginUiState

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        // Login screen typically doesn't have top/bottom bars managed by Scaffold itself
        // unless you want a consistent app bar style. The content itself has top/bottom sections.
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use padding from Scaffold if needed
        ) {
            LoginTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Adjust weight as needed
            )
            LoginBody(
                uiState = uiState,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f) // Adjust weight as needed
            )
            LoginBottom( // Pass the navigation lambda
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}