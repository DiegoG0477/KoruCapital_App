package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.model.Business // Import domain model
import com.koru.capital.business.domain.usecase.GetBusinessDetailsUseCase // Import UseCase
// Import save/like use cases if needed on this screen too
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State for the Detail Screen
data class BusinessDetailUiState(
    val isLoading: Boolean = true,
    val business: Business? = null, // Holds the loaded business details (Domain Model)
    val errorMessage: String? = null,
    val showAssociationDialog: Boolean = false, // State for the association modal
    // Add states for like/save specific to this screen if actions are available here
    val isSaved: Boolean = false,
    val isLiked: Boolean = false
)

@HiltViewModel
class BusinessDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBusinessDetailsUseCase: GetBusinessDetailsUseCase
    // Inject toggleSave/Like UseCases if needed
) : ViewModel() {

    private val businessId: String = checkNotNull(savedStateHandle["businessId"])

    private val _uiState = MutableStateFlow(BusinessDetailUiState())
    val uiState: StateFlow<BusinessDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            // Simulate delay for demo
            // delay(1000)
            val result = getBusinessDetailsUseCase(businessId)
            result.fold(
                onSuccess = { businessData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            business = businessData
                            // TODO: Initialize isSaved/isLiked based on loaded data if available
                            // isSaved = businessData.isSavedByUser ?: false
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al cargar detalles: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    fun onAssociateClick() {
        // Logic before showing dialog (e.g., check eligibility?)
        _uiState.update { it.copy(showAssociationDialog = true) }
    }

    fun dismissAssociationDialog() {
        _uiState.update { it.copy(showAssociationDialog = false) }
    }

    // Add functions for save/like toggles if these actions are on the detail screen
    fun toggleSave() { /* ... call use case, update state ... */ }
    fun toggleLike() { /* ... call use case, update state ... */ }

}