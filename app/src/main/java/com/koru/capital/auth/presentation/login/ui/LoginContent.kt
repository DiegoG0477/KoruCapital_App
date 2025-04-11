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
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoginTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            LoginBody(
                uiState = uiState,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
            LoginBottom(
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}