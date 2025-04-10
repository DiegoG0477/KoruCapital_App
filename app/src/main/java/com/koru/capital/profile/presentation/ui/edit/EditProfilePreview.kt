package com.koru.capital.profile.presentation.ui.edit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.core.ui.theme.KoruTheme
import com.koru.capital.profile.presentation.viewmodel.EditProfileUiState

@Preview(showBackground = true, name = "Edit Profile - Loading")
@Composable
private fun EditProfileContentLoadingPreview() {
    KoruTheme {
        EditProfileContent(
            uiState = EditProfileUiState(isLoading = true),
            onBackClick = {}, onFirstNameChange = {}, onLastNameChange = {}, onBioChange = {},
            onLinkedInChange = {}, onInstagramChange = {}, onImageSelected = {}, onSaveChanges = {},
            onImagePickerClick = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Edit Profile - Loaded")
@Composable
private fun EditProfileContentLoadedPreview() {
    KoruTheme {
        EditProfileContent(
            uiState = EditProfileUiState(
                isLoading = false,
                firstName = "Carlos",
                lastName = "Ruiz",
                bio = "Desarrollador y entusiasta de las finanzas.",
                linkedInUrl = "https://linkedin.com/in/carlos",
                instagramUrl = "https://instagram.com/carlos_ru",
                currentProfileImageUrl = null // Uses placeholder
            ),
            onBackClick = {}, onFirstNameChange = {}, onLastNameChange = {}, onBioChange = {},
            onLinkedInChange = {}, onInstagramChange = {}, onImageSelected = {}, onSaveChanges = {},
            onImagePickerClick = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Edit Profile - Saving")
@Composable
private fun EditProfileContentSavingPreview() {
    KoruTheme {
        EditProfileContent(
            uiState = EditProfileUiState(
                isLoading = false,
                isSaving = true, // Show saving indicator
                firstName = "Carlos", lastName = "Ruiz" // Other fields...
            ),
            onBackClick = {}, onFirstNameChange = {}, onLastNameChange = {}, onBioChange = {},
            onLinkedInChange = {}, onInstagramChange = {}, onImageSelected = {}, onSaveChanges = {},
            onImagePickerClick = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Edit Profile - Error")
@Composable
private fun EditProfileContentErrorPreview() {
    KoruTheme {
        EditProfileContent(
            uiState = EditProfileUiState(
                isLoading = false,
                errorMessage = "El nombre no puede estar vacío.",
                firstName = "", // Example error state
                lastName = "Ruiz"
            ),
            onBackClick = {}, onFirstNameChange = {}, onLastNameChange = {}, onBioChange = {},
            onLinkedInChange = {}, onInstagramChange = {}, onImageSelected = {}, onSaveChanges = {},
            onImagePickerClick = TODO(),
            modifier = TODO()
        )
    }
}