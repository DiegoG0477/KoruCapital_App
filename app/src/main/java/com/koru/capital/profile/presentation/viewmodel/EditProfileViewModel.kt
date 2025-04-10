package com.koru.capital.profile.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.core.domain.usecase.UploadFileUseCase // If needed directly, but UpdateMyProfileUseCase handles it
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.domain.model.UserProfileUpdate
import com.koru.capital.profile.domain.usecase.GetMyProfileUseCase
import com.koru.capital.profile.domain.usecase.UpdateMyProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State for the Edit Profile Screen
data class EditProfileUiState(
    val isLoading: Boolean = true, // Loading initial profile data
    val isSaving: Boolean = false, // Saving updates
    val errorMessage: String? = null,
    val isSuccess: Boolean = false, // Update success flag

    // Editable fields
    val firstName: String = "",
    val lastName: String = "",
    val bio: String = "",
    val linkedInUrl: String = "",
    val instagramUrl: String = "", // Store full URL or handle? Let's use full URL for simplicity
    val currentProfileImageUrl: String? = null, // Existing image URL
    val newProfileImageUri: Uri? = null // New image selected by user
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val updateMyProfileUseCase: UpdateMyProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getMyProfileUseCase().fold(
                onSuccess = { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            firstName = profile.firstName ?: "",
                            lastName = profile.lastName ?: "",
                            bio = profile.bio ?: "",
                            linkedInUrl = profile.linkedInUrl ?: "",
                            instagramUrl = profile.instagramUrl ?: "",
                            currentProfileImageUrl = profile.profileImageUrl
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Error al cargar datos: ${exception.message}")
                    }
                }
            )
        }
    }

    // --- Input Change Handlers ---
    fun onFirstNameChanged(value: String) { _uiState.update { it.copy(firstName = value, errorMessage = null, isSuccess = false) } }
    fun onLastNameChanged(value: String) { _uiState.update { it.copy(lastName = value, errorMessage = null, isSuccess = false) } }
    fun onBioChanged(value: String) { _uiState.update { it.copy(bio = value, errorMessage = null, isSuccess = false) } }
    fun onLinkedInChanged(value: String) { _uiState.update { it.copy(linkedInUrl = value, errorMessage = null, isSuccess = false) } }
    fun onInstagramChanged(value: String) { _uiState.update { it.copy(instagramUrl = value, errorMessage = null, isSuccess = false) } }
    fun onProfileImageSelected(uri: Uri?) { _uiState.update { it.copy(newProfileImageUri = uri, errorMessage = null, isSuccess = false) } }


    // --- Submit Update ---
    fun submitProfileUpdate() {
        val state = _uiState.value

        // Basic Validation
        if (state.firstName.isBlank() || state.lastName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nombre y apellido son requeridos.") }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }

        // Create the update data object
        val updateData = UserProfileUpdate(
            firstName = state.firstName,
            lastName = state.lastName,
            bio = state.bio.takeIf { it.isNotBlank() }, // Send null if blank
            linkedInUrl = state.linkedInUrl.takeIf { it.isNotBlank() },
            instagramUrl = state.instagramUrl.takeIf { it.isNotBlank() },
            // Pass existing image URL initially, UseCase will override if new image is uploaded
            profileImageUrl = state.currentProfileImageUrl
        )

        viewModelScope.launch {
            // Call the use case, passing the update data and the new image URI
            val result = updateMyProfileUseCase(updateData, state.newProfileImageUri)

            result.fold(
                onSuccess = { updatedProfile ->
                    // Update successful
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSuccess = true, // Set success flag
                            errorMessage = null,
                            // Optionally update state with returned profile data
                            firstName = updatedProfile.firstName ?: "",
                            lastName = updatedProfile.lastName ?: "",
                            bio = updatedProfile.bio ?: "",
                            linkedInUrl = updatedProfile.linkedInUrl ?: "",
                            instagramUrl = updatedProfile.instagramUrl ?: "",
                            currentProfileImageUrl = updatedProfile.profileImageUrl, // Update displayed image URL
                            newProfileImageUri = null // Clear selected URI after successful upload/update
                        )
                    }
                    // Navigation back will be handled by LaunchedEffect in the Screen
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSuccess = false, // Ensure success is false
                            errorMessage = "Error al actualizar: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    // Called by Screen after navigation completes post-success
    fun acknowledgeSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}