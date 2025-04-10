package com.koru.capital.profile.presentation.ui

import android.net.Uri // Import Uri
import androidx.activity.compose.rememberLauncherForActivityResult // Import launcher
import androidx.activity.result.contract.ActivityResultContracts // Import contract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.profile.presentation.ui.edit.EditProfileContent
import com.koru.capital.profile.presentation.viewmodel.EditProfileViewModel

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit, // Navigate back after save or cancel
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Launcher for picking an image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            viewModel.onProfileImageSelected(uri) // Pass the selected URI to the ViewModel
        }
    )

    // Handle successful save: Navigate back
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBackClick()
            viewModel.acknowledgeSuccess()
        }
    }

    EditProfileContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFirstNameChange = viewModel::onFirstNameChanged,
        onLastNameChange = viewModel::onLastNameChanged,
        onBioChange = viewModel::onBioChanged,
        onLinkedInChange = viewModel::onLinkedInChanged,
        onInstagramChange = viewModel::onInstagramChanged,
        // Pass a lambda that triggers the launcher
        onImagePickerClick = {
            imagePickerLauncher.launch("image/*") // Launch the image picker
        },
        // Pass the actual image selection handler (might not be needed directly by Content if picker logic is here)
        onImageSelected = viewModel::onProfileImageSelected, // Pass VM function
        onSaveChanges = viewModel::submitProfileUpdate
    )
}