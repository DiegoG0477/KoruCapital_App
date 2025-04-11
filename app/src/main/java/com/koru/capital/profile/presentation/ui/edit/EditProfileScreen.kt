package com.koru.capital.profile.presentation.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.profile.presentation.ui.edit.EditProfileContent
import com.koru.capital.profile.presentation.viewmodel.EditProfileViewModel

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            viewModel.onProfileImageSelected(uri)
        }
    )

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
        onImagePickerClick = {
            imagePickerLauncher.launch("image/*")
        },
        onImageSelected = viewModel::onProfileImageSelected,
        onSaveChanges = viewModel::submitProfileUpdate
    )
}