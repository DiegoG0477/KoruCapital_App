package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.usecase.DeleteBusinessUseCase // <-- Importar real
import com.koru.capital.business.domain.usecase.GetMyBusinessesUseCase // <-- Importar real
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- Data Classes, Enum, Sealed Class (sin cambios) ---
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
// --- Fin Data Classes ---


@HiltViewModel
class MyBusinessesViewModel @Inject constructor(
    private val getMyBusinessesUseCase: GetMyBusinessesUseCase, // <-- Inyectar real
    private val deleteBusinessUseCase: DeleteBusinessUseCase  // <-- Inyectar real
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyBusinessesUiState())
    val uiState: StateFlow<MyBusinessesUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<MyBusinessesNavigationEvent>()
    val navigationEvent: SharedFlow<MyBusinessesNavigationEvent> = _navigationEvent.asSharedFlow()


    init {
        loadBusinesses() // Carga inicial con filtro OWNED
    }

    // Cargar negocios usando el UseCase real
    fun loadBusinesses(filter: BusinessFilter = _uiState.value.selectedFilter) {
        viewModelScope.launch {
            // Actualizar estado: Iniciar carga, limpiar errores y lista actual
            _uiState.update { it.copy(isLoading = true, errorMessage = null, businesses = emptyList()) }

            // Llamar al UseCase
            getMyBusinessesUseCase(filter).fold(
                onSuccess = { businessList ->
                    // Éxito: Actualizar estado con la nueva lista y parar carga
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            businesses = businessList, // La lista ya viene como UIModel del Repo/API
                            selectedFilter = filter // Asegurar que el filtro esté actualizado
                        )
                    }
                },
                onFailure = { exception ->
                    // Fallo: Actualizar estado con mensaje de error y parar carga
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
            // No es necesario actualizar el filtro aquí, loadBusinesses lo hará
            loadBusinesses(filter) // Llamar a cargar con el nuevo filtro
        }
    }

    // --- Navegación (sin cambios) ---
    fun onAddBusinessClick() {
        viewModelScope.launch { _navigationEvent.emit(MyBusinessesNavigationEvent.NavigateToAddBusiness) }
    }

    fun onEditBusinessClick(businessId: String) {
        viewModelScope.launch { _navigationEvent.emit(MyBusinessesNavigationEvent.NavigateToEditBusiness(businessId)) }
    }

    // --- Delete Logic (Usar UseCase real) ---
    fun onDeleteBusinessRequest(business: BusinessListItemUiModel) {
        _uiState.update { it.copy(showDeleteConfirmation = true, businessToDelete = business) }
    }

    fun onConfirmDelete() {
        val businessToDelete = _uiState.value.businessToDelete ?: return dismissDeleteConfirmation()

        _uiState.update { it.copy(isLoading = true, showDeleteConfirmation = false) } // Indicar carga, ocultar diálogo
        viewModelScope.launch {
            deleteBusinessUseCase(businessToDelete.id).fold(
                onSuccess = {
                    // Éxito: Quitar de la lista local y parar carga
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            businesses = currentState.businesses.filterNot { it.id == businessToDelete.id },
                            businessToDelete = null // Limpiar
                        )
                    }
                    // Opcional: Emitir evento para Snackbar/Toast de éxito
                },
                onFailure = { exception ->
                    // Fallo: Parar carga, mostrar error, limpiar businessToDelete
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

    // Eliminar helpers de simulación si existían
}