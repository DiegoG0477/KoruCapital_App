package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.usecase.DeleteBusinessUseCase
import com.koru.capital.business.domain.usecase.GetMyBusinessesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BusinessListItemUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: String?,
    val location: String?,
    val isOwned: Boolean
)

data class MyBusinessesUiState(
    val isLoading: Boolean = false,
    val businesses: List<BusinessListItemUiModel> = emptyList(),
    val errorMessage: String? = null,
    val selectedFilter: BusinessFilter = BusinessFilter.OWNED,
    val showDeleteConfirmation: Boolean = false,
    val businessToDelete: BusinessListItemUiModel? = null
)

enum class BusinessFilter(val title: String) {
    OWNED("Mis Negocios"),
    SAVED("Guardados"),
    PARTNERED("Asociaciones")
}

sealed class MyBusinessesNavigationEvent {
    data class NavigateToEditBusiness(val businessId: String) : MyBusinessesNavigationEvent()
    object NavigateToAddBusiness : MyBusinessesNavigationEvent()
}


@HiltViewModel
class MyBusinessesViewModel @Inject constructor(
    private val getMyBusinessesUseCase: GetMyBusinessesUseCase,
    private val deleteBusinessUseCase: DeleteBusinessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyBusinessesUiState())
    val uiState: StateFlow<MyBusinessesUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MyBusinessesNavigationEvent>()
    val navigationEvent: SharedFlow<MyBusinessesNavigationEvent> = _navigationEvent.asSharedFlow()


    init {
        loadBusinesses()
    }

    fun loadBusinesses(filter: BusinessFilter = _uiState.value.selectedFilter) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, businesses = emptyList()) }

            getMyBusinessesUseCase(filter).fold(
                onSuccess = { businessList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            businesses = businessList,
                            selectedFilter = filter
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al cargar negocios: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    fun onFilterSelected(filter: BusinessFilter) {
        if (_uiState.value.selectedFilter != filter) {
            loadBusinesses(filter)
        }
    }

    fun onAddBusinessClick() {
        viewModelScope.launch { _navigationEvent.emit(MyBusinessesNavigationEvent.NavigateToAddBusiness) }
    }

    fun onEditBusinessClick(businessId: String) {
        viewModelScope.launch { _navigationEvent.emit(MyBusinessesNavigationEvent.NavigateToEditBusiness(businessId)) }
    }

    fun onDeleteBusinessRequest(business: BusinessListItemUiModel) {
        _uiState.update { it.copy(showDeleteConfirmation = true, businessToDelete = business) }
    }

    fun onConfirmDelete() {
        val businessToDelete = _uiState.value.businessToDelete ?: return dismissDeleteConfirmation()

        _uiState.update { it.copy(isLoading = true, showDeleteConfirmation = false) }
        viewModelScope.launch {
            deleteBusinessUseCase(businessToDelete.id).fold(
                onSuccess = {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            businesses = currentState.businesses.filterNot { it.id == businessToDelete.id },
                            businessToDelete = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al eliminar: ${exception.message}",
                            businessToDelete = null
                        )
                    }
                }
            )
        }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false, businessToDelete = null) }
    }

}