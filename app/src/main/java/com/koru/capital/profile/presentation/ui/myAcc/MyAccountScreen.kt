package com.koru.capital.profile.presentation.ui.myAcc

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.profile.presentation.viewmodel.MyAccountViewModel

@Composable
fun MyAccountScreen(
    viewModel: MyAccountViewModel = hiltViewModel(),
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyAccountContent(
        uiState = uiState,
        onEditProfileClick = onNavigateToEditProfile,
        onSettingsClick = onNavigateToSettings,
        onLogoutClick = {
            viewModel.onLogoutClick()
            onLogout()
        }
    )
}