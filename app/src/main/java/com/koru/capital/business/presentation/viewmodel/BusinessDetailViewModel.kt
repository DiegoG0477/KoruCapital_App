package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.usecase.GetBusinessDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusinessDetailUiState(
    val isLoading: Boolean = true,
    val business: Business? = null,
    val errorMessage: String? = null,
    val showAssociationDialog: Boolean = false,
    val isSaved: Boolean = false,
    val isLiked: Boolean = false
)

@HiltViewModel
class BusinessDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBusinessDetailsUseCase: GetBusinessDetailsUseCase
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
            val result = getBusinessDetailsUseCase(businessId)
            result.fold(
                onSuccess = { businessData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            business = businessData
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
        _uiState.update { it.copy(showAssociationDialog = true) }
    }

    fun dismissAssociationDialog() {
        _uiState.update { it.copy(showAssociationDialog = false) }
    }

    fun toggleSave() {  }
    fun toggleLike() {  }

}