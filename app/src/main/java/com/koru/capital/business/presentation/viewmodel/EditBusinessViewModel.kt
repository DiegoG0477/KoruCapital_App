package com.koru.capital.business.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.usecase.GetBusinessDetailsUseCase
import com.koru.capital.business.domain.usecase.UpdateBusinessUseCase
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.usecase.GetCategoriesUseCase
import com.koru.capital.core.domain.usecase.GetMunicipalitiesUseCase
import com.koru.capital.core.domain.usecase.GetStatesUseCase
// No se necesita UploadFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay // Para simular resolveStateId
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- Data Class (Con indicadores de carga específicos) ---
data class EditBusinessUiState(
    val businessId: String = "",
    val businessName: String = "",
    val description: String = "",
    val investment: String = "",
    val profitPercentage: String = "",
    val selectedCategoryId: String = "",
    val selectedStateId: String = "",
    val selectedMunicipalityId: String = "",
    val businessModel: String = "",
    val monthlyIncome: String = "",
    val selectedImageUri: Uri? = null, // Nueva imagen seleccionada
    val existingImageUrl: String? = null, // URL actual
    val isLoading: Boolean = true, // Carga inicial de detalles
    val isSaving: Boolean = false, // Guardando cambios
    val isLoadingStates: Boolean = false, // Carga específica de estados
    val isLoadingCategories: Boolean = false, // Carga específica de categorías
    val isLoadingMunicipalities: Boolean = false, // Carga específica de municipios
    val isSuccess: Boolean = false, // Éxito al guardar
    val errorMessage: String? = null,
    val states: List<State> = emptyList(),
    val municipalities: List<Municipality> = emptyList(),
    val categories: List<Category> = emptyList()
)

@HiltViewModel
class EditBusinessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBusinessDetailsUseCase: GetBusinessDetailsUseCase,
    private val updateBusinessUseCase: UpdateBusinessUseCase,
    private val getStatesUseCase: GetStatesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMunicipalitiesUseCase: GetMunicipalitiesUseCase
    // Inyectar GetCurrentUserIdUseCase si fuera necesario para alguna lógica
) : ViewModel() {

    private val businessId: String = checkNotNull(savedStateHandle["businessId"]) { "Business ID missing in arguments" }

    private val _uiState = MutableStateFlow(EditBusinessUiState(businessId = businessId))
    val uiState: StateFlow<EditBusinessUiState> = _uiState.asStateFlow()

    // TODO: Determinar el countryId relevante (ej: de preferencias o fijo)
    private val defaultCountryId = "MX"

    init {
        loadInitialDataForEdit()
    }

    // --- Data Loading ---
    private fun loadInitialDataForEdit() {
        viewModelScope.launch {
            // Iniciar todas las cargas relevantes
            _uiState.update { it.copy(
                isLoading = true, // Carga general
                isLoadingStates = true,
                isLoadingCategories = true,
                errorMessage = null)
            }

            // Lanzar cargas en paralelo
            val categoriesJob = launch { loadCategories() }
            val statesJob = launch { loadStates(defaultCountryId) }

            // Esperar a que terminen las cargas de listas
            categoriesJob.join()
            statesJob.join()

            // Cargar detalles del negocio solo si las listas se cargaron bien (o manejar error)
            if (uiState.value.errorMessage == null) {
                // Usamos los estados ya cargados en uiState para resolver el stateId
                loadBusinessDetailsAndMunicipalities(uiState.value.states)
            } else {
                // Si falló la carga de listas, terminar carga general con error
                _uiState.update { it.copy(isLoading = false)}
            }
        }
    }

    private suspend fun loadBusinessDetailsAndMunicipalities(availableStates: List<State>) {
        // isLoading ya está en true
        getBusinessDetailsUseCase(businessId).fold(
            onSuccess = { business ->
                // Intentar resolver State ID (IMPLEMENTACIÓN PENDIENTE)
                val resolvedStateId = resolveStateIdFromMunicipality(business.municipalityId, availableStates) ?: ""

                _uiState.update {
                    it.copy(
                        // isLoading se pondrá en false *después* de cargar municipios (si aplica)
                        businessName = business.name,
                        description = business.description,
                        investment = business.investment.toString(),
                        profitPercentage = business.profitPercentage.toString(),
                        selectedCategoryId = business.categoryId.toString(),
                        selectedStateId = resolvedStateId,
                        selectedMunicipalityId = business.municipalityId.toString(), // Convertir Int a String
                        businessModel = business.businessModel,
                        monthlyIncome = business.monthlyIncome.toString(),
                        existingImageUrl = business.imageUrl,
                        errorMessage = null // Limpiar errores previos si la carga fue exitosa
                    )
                }
                // Cargar municipios para el estado resuelto
                if (resolvedStateId.isNotEmpty()) {
                    loadMunicipalities(resolvedStateId, true) // Indicar que es carga inicial
                } else {
                    // Si no se resolvió estado, terminar carga general aquí
                    _uiState.update { it.copy(isLoading = false, isLoadingMunicipalities = false) }
                }
            },
            onFailure = { exception ->
                _uiState.update { it.copy(isLoading = false, isLoadingStates = false, isLoadingCategories = false, isLoadingMunicipalities = false, errorMessage = "Error cargando detalles: ${exception.message}") }
            }
        )
    }

    // Lógica placeholder/ineficiente para resolver stateId. ¡MEJORAR!
    private suspend fun resolveStateIdFromMunicipality(municipalityIdInt: Int, states: List<State>): String? {
        println("WARN: resolveStateIdFromMunicipality needs a proper implementation! Using inefficient placeholder.")
        delay(10)
        val municipalityIdStr = municipalityIdInt.toString()
        // Busca en todos los estados cargados (ineficiente si son muchos)
        for (state in states) {
            // Llama al use case para obtener municipios de CADA estado hasta encontrar el correcto
            val municipalitiesResult = getMunicipalitiesUseCase(state.id)
            if (municipalitiesResult.isSuccess) {
                if (municipalitiesResult.getOrThrow().any { it.id == municipalityIdStr }) {
                    return state.id // Encontrado!
                }
            } else {
                // Manejar error si falla la carga de municipios durante la búsqueda
                println("Error fetching municipalities for state ${state.id} while resolving state ID.")
            }
        }
        // Si no se encontró en ningún estado cargado
        return null
    }

    private suspend fun loadStates(countryId: String) {
        // isLoadingStates ya está en true
        getStatesUseCase(countryId).fold(
            onSuccess = { states ->
                _uiState.update { it.copy(states = states, isLoadingStates = false) }
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoadingStates = false, errorMessage = (it.errorMessage ?: "") + "\nError Estados: ${error.message}", states = emptyList()) }
            }
        )
    }

    private suspend fun loadCategories() {
        // isLoadingCategories ya está en true
        getCategoriesUseCase().fold(
            onSuccess = { categories ->
                _uiState.update { it.copy(categories = categories, isLoadingCategories = false) }
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoadingCategories = false, errorMessage = (it.errorMessage ?: "") + "\nError Categorías: ${error.message}", categories = emptyList()) }
            }
        )
    }

    // Modificado para indicar si es parte de la carga inicial general
    private fun loadMunicipalities(stateId: String, isInitialLoad: Boolean = false) {
        if (stateId.isBlank()) {
            _uiState.update { it.copy(municipalities = emptyList(), selectedMunicipalityId = "", isLoadingMunicipalities = false, isLoading = if(isInitialLoad) false else it.isLoading ) } // Finalizar carga si es inicial
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMunicipalities = true, errorMessage = null, municipalities = emptyList()) }
            getMunicipalitiesUseCase(stateId).fold(
                onSuccess = { municipalities ->
                    // Si es parte de la carga inicial, también finalizar isLoading general
                    val newLoadingState = if (isInitialLoad) false else _uiState.value.isLoading
                    _uiState.update { it.copy(municipalities = municipalities, isLoadingMunicipalities = false, isLoading = newLoadingState) }
                    // Si es carga inicial y el ID guardado está en la nueva lista, mantenerlo seleccionado
                    if (!isInitialLoad && !_uiState.value.municipalities.any{ it.id == _uiState.value.selectedMunicipalityId}){
                        _uiState.update { it.copy(selectedMunicipalityId = "") } // Deseleccionar si no está en la nueva lista
                    }
                },
                onFailure = { error ->
                    val newLoadingState = if (isInitialLoad) false else _uiState.value.isLoading
                    _uiState.update { it.copy(isLoadingMunicipalities = false, isLoading = newLoadingState, errorMessage = "Error cargando municipios: ${error.message}", municipalities = emptyList()) }
                }
            )
        }
    }

    // --- Event Handlers ---
    fun onBusinessNameChanged(name: String) { _uiState.update { it.copy(businessName = name, errorMessage = null, isSuccess = false) } }
    fun onDescriptionChanged(description: String) { _uiState.update { it.copy(description = description, errorMessage = null, isSuccess = false) } }
    fun onInvestmentChanged(investment: String) {
        val filtered = investment.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(investment = filtered, errorMessage = null, isSuccess = false) } }
    }
    fun onProfitChanged(profit: String) {
        val filtered = profit.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(profitPercentage = filtered, errorMessage = null, isSuccess = false) } }
    }
    fun onCategorySelected(categoryId: String) { _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null, isSuccess = false) } }
    fun onBusinessModelChanged(model: String) { _uiState.update { it.copy(businessModel = model, errorMessage = null, isSuccess = false) } }
    fun onMonthlyIncomeChanged(income: String) {
        val filtered = income.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(monthlyIncome = filtered, errorMessage = null, isSuccess = false) } }
    }
    fun onStateSelected(stateId: String) {
        if (stateId != _uiState.value.selectedStateId) {
            _uiState.update {
                it.copy(
                    selectedStateId = stateId,
                    selectedMunicipalityId = "", // Resetear municipio al cambiar estado
                    municipalities = emptyList(),
                    isLoadingMunicipalities = stateId.isNotBlank(),
                    errorMessage = null,
                    isSuccess = false
                )
            }
            loadMunicipalities(stateId, false) // Cargar municipios (no es carga inicial)
        }
    }
    fun onMunicipalitySelected(municipalityId: String) { _uiState.update { it.copy(selectedMunicipalityId = municipalityId, errorMessage = null, isSuccess = false) } }
    fun onImageSelected(uri: Uri?) { _uiState.update { it.copy(selectedImageUri = uri, errorMessage = null, isSuccess = false) } }


    // --- Form Submission (Update) ---
    fun submitUpdate() {
        val state = _uiState.value
        if (!isValid(state)) {
            // isValid ya actualiza el errorMessage
            _uiState.update { it.copy(isSaving = false) }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }

        viewModelScope.launch {
            // Crear objeto Business con los datos del estado actual
            val businessUpdateData = Business(
                id = state.businessId,
                name = state.businessName,
                description = state.description,
                investment = state.investment.toDoubleOrNull() ?: 0.0,
                profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
                categoryId = state.selectedCategoryId.toIntOrNull() ?: 0,
                municipalityId = state.selectedMunicipalityId.toIntOrNull() ?: 0,
                businessModel = state.businessModel,
                monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0,
                imageUrl = state.existingImageUrl, // Pasar URL existente
            )

            // Llamar al UseCase de actualización
            val result = updateBusinessUseCase(
                businessId = state.businessId,
                businessData = businessUpdateData,
                newImageUri = state.selectedImageUri // Pasar la nueva URI
            )

            result.fold(
                onSuccess = { updatedBusiness ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSuccess = true,
                            errorMessage = null,
                            // Actualizar campos con datos devueltos (opcional pero bueno)
                            businessName = updatedBusiness.name,
                            description = updatedBusiness.description,
                            investment = updatedBusiness.investment.toString(),
                            profitPercentage = updatedBusiness.profitPercentage.toString(),
                            selectedCategoryId = updatedBusiness.categoryId.toString(),
                            selectedMunicipalityId = updatedBusiness.municipalityId.toString(),
                            // Quizás actualizar stateId si se recalcula
                            businessModel = updatedBusiness.businessModel,
                            monthlyIncome = updatedBusiness.monthlyIncome.toString(),
                            existingImageUrl = updatedBusiness.imageUrl,
                            selectedImageUri = null // Limpiar URI seleccionada
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSuccess = false,
                            errorMessage = "Error al actualizar: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    // --- Validation Helper (Completo) ---
    private fun isValid(state: EditBusinessUiState): Boolean {
        var message: String? = null
        when {
            state.businessName.isBlank() -> message = "El nombre del negocio es requerido"
            state.description.isBlank() -> message = "La descripción es requerida"
            state.investment.isBlank() || state.investment.toDoubleOrNull() == null || state.investment.toDouble() <= 0 -> message = "La inversión debe ser un número positivo"
            state.profitPercentage.isBlank() || state.profitPercentage.toDoubleOrNull() == null || state.profitPercentage.toDouble() <= 0 -> message = "La ganancia debe ser un número positivo"
            state.selectedCategoryId.isBlank() || state.selectedCategoryId.toIntOrNull() == null -> message = "Selecciona una categoría válida"
            state.selectedStateId.isBlank() -> message = "Selecciona un estado" // Aunque se resuelva, debe haber uno seleccionado
            state.selectedMunicipalityId.isBlank() || state.selectedMunicipalityId.toIntOrNull() == null -> message = "Selecciona un municipio válido"
            state.businessModel.isBlank() -> message = "El modelo de negocio es requerido"
            state.monthlyIncome.isBlank() || state.monthlyIncome.toDoubleOrNull() == null || state.monthlyIncome.toDouble() <= 0 -> message = "El ingreso mensual debe ser positivo"
            // La imagen ya no es obligatoria aquí, se maneja existente vs nueva
        }
        _uiState.update { it.copy(errorMessage = message) }
        return message == null
    }

    // --- Reset State ---
    fun resetSuccessState() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}