package com.koru.capital.profile.presentation.ui.myAcc

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.core.ui.theme.KoruTheme
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.presentation.viewmodel.MyAccountUiState

val sampleUserProfile = UserProfile(
    userId = "user123",
    firstName = "Ana",
    lastName = "García",
    email = "ana.garcia@email.com",
    profileImageUrl = null, // Will use placeholder
    bio = "Emprendedora apasionada por la tecnología y el impacto social. Buscando socios para mi próximo proyecto.",
    linkedInUrl = "https://linkedin.com/in/anagarcia",
    instagramUrl = "https://instagram.com/anagarcia_dev",
    joinDate = "Enero 2024"
)

@Preview(showBackground = true, name = "My Account - Loading")
@Composable
private fun MyAccountContentLoadingPreview() {
    KoruTheme {
        MyAccountContent(
            uiState = MyAccountUiState(isLoading = true),
            onEditProfileClick = {}, onSettingsClick = {}, onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true, name = "My Account - Error")
@Composable
private fun MyAccountContentErrorPreview() {
    KoruTheme {
        MyAccountContent(
            uiState = MyAccountUiState(isLoading = false, errorMessage = "No se pudo cargar el perfil."),
            onEditProfileClick = {}, onSettingsClick = {}, onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true, name = "My Account - Success")
@Composable
private fun MyAccountContentSuccessPreview() {
    KoruTheme {
        MyAccountContent(
            uiState = MyAccountUiState(isLoading = false, userProfile = sampleUserProfile),
            onEditProfileClick = {}, onSettingsClick = {}, onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true, name = "My Account - Minimal Data")
@Composable
private fun MyAccountContentMinimalPreview() {
    val minimalProfile = UserProfile("user456", "Bob", "Smith", "bob@test.com", null, null, null, null, "Febrero 2024")
    KoruTheme {
        MyAccountContent(
            uiState = MyAccountUiState(isLoading = false, userProfile = minimalProfile),
            onEditProfileClick = {}, onSettingsClick = {}, onLogoutClick = {}
        )
    }
}