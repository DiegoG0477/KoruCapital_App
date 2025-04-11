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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val selectedImageUri: Uri? = null,
    val existingImageUrl: String? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isLoadingStates: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingMunicipalities: Boolean = false,
    val isSuccess: Boolean = false,
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
) : ViewModel() {

    private val businessId: String = checkNotNull(savedStateHandle["businessId"]) { "Business ID missing" }

    private val _uiState = MutableStateFlow(EditBusinessUiState(businessId = businessId))
    val uiState: StateFlow<EditBusinessUiState> = _uiState.asStateFlow()

    private val businessCountryId = "MX"

    init {
        loadInitialDataForEdit()
    }

    private fun loadInitialDataForEdit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isLoadingStates = true, isLoadingCategories = true, errorMessage = null) }

            val categoriesJob = launch { loadCategories() }
            val statesJob = launch { loadStates(businessCountryId) }

            categoriesJob.join()
            statesJob.join()

            if (uiState.value.errorMessage == null) {
                loadBusinessDetailsAndMunicipalities(uiState.value.states)
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadBusinessDetailsAndMunicipalities(availableStates: List<State>) {
        getBusinessDetailsUseCase(businessId).fold(
            onSuccess = { business ->
                val resolvedStateId = resolveStateIdFromMunicipality(business.municipalityId, availableStates) ?: ""

                _uiState.update {
                    it.copy(
                        businessName = business.name,
                        description = business.description,
                        investment = business.investment.toString(),
                        profitPercentage = business.profitPercentage.toString(),
                        selectedCategoryId = business.categoryId.toString(),
                        selectedStateId = resolvedStateId,
                        selectedMunicipalityId = business.municipalityId.toString(),
                        businessModel = business.businessModel,
                        monthlyIncome = business.monthlyIncome.toString(),
                        existingImageUrl = business.imageUrl,
                        errorMessage = null
                    )
                }
                if (resolvedStateId.isNotEmpty()) {
                    loadMunicipalities(resolvedStateId, true)
                } else {
                    _uiState.update { it.copy(isLoading = false, isLoadingMunicipalities = false) }
                }
            },
            onFailure = { exception ->
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error cargando detalles: ${exception.message}") }
            }
        )
    }

    private suspend fun resolveStateIdFromMunicipality(municipalityIdInt: String, states: List<State>): String? {
        println("WARN: resolveStateIdFromMunicipality needs a proper implementation!")
        delay(10)
        val municipalityIdStr = municipalityIdInt.toString()
        for (state in states) {
            val municipalitiesResult = getMunicipalitiesUseCase(state.id)
            if (municipalitiesResult.isSuccess && municipalitiesResult.getOrNull()?.any { it.id == municipalityIdStr } == true) {
                return state.id
            }
        }
        return states.firstOrNull()?.id
    }

    private suspend fun loadStates(countryId: String) {
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
        getCategoriesUseCase().fold(
            onSuccess = { categories ->
                _uiState.update { it.copy(categories = categories, isLoadingCategories = false) }
            },
            onFailure = { error ->
                _uiState.update { it.copy(isLoadingCategories = false, errorMessage = (it.errorMessage ?: "") + "\nError Categorías: ${error.message}", categories = emptyList()) }
            }
        )
    }

    private fun loadMunicipalities(stateId: String, isInitialLoad: Boolean = false) {
        if (stateId.isBlank()) {
            _uiState.update { it.copy(municipalities = emptyList(), selectedMunicipalityId = "", isLoadingMunicipalities = false, isLoading = if(isInitialLoad) false else it.isLoading ) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMunicipalities = true, errorMessage = null, municipalities = emptyList()) }
            getMunicipalitiesUseCase(stateId).fold(
                onSuccess = { municipalities ->
                    val finalLoadingState = if (isInitialLoad) false else _uiState.value.isLoading
                    val currentMuniId = _uiState.value.selectedMunicipalityId
                    val shouldKeepSelection = municipalities.any { it.id == currentMuniId }
                    _uiState.update {
                        it.copy(
                            municipalities = municipalities,
                            isLoadingMunicipalities = false,
                            isLoading = finalLoadingState,
                            selectedMunicipalityId = if (!shouldKeepSelection && !isInitialLoad) "" else currentMuniId
                        )
                    }
                },
                onFailure = { error ->
                    val finalLoadingState = if (isInitialLoad) false else _uiState.value.isLoading
                    _uiState.update { it.copy(isLoadingMunicipalities = false, isLoading = finalLoadingState, errorMessage = "Error cargando municipios: ${error.message}", municipalities = emptyList()) }
                }
            )
        }
    }

    fun onBusinessNameChanged(name: String) { _uiState.update { it.copy(businessName = name, errorMessage = null, isSuccess = false) } }
    fun onDescriptionChanged(description: String) { _uiState.update { it.copy(description = description, errorMessage = null, isSuccess = false) } }
    fun onInvestmentChanged(investment: String) {  }
    fun onProfitChanged(profit: String) {  }
    fun onCategorySelected(categoryId: String) { _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null, isSuccess = false) } }
    fun onBusinessModelChanged(model: String) { _uiState.update { it.copy(businessModel = model, errorMessage = null, isSuccess = false) } }
    fun onMonthlyIncomeChanged(income: String) {  }

    fun onStateSelected(stateId: String) {
        if (stateId != _uiState.value.selectedStateId) {
            _uiState.update {
                it.copy(
                    selectedStateId = stateId,
                    selectedMunicipalityId = "",
                    municipalities = emptyList(),
                    isLoadingMunicipalities = stateId.isNotBlank(),
                    errorMessage = null,
                    isSuccess = false
                )
            }
            loadMunicipalities(stateId, false)
        }
    }
    fun onMunicipalitySelected(municipalityId: String) { _uiState.update { it.copy(selectedMunicipalityId = municipalityId, errorMessage = null, isSuccess = false) } }
    fun onImageSelected(uri: Uri?) { _uiState.update { it.copy(selectedImageUri = uri, errorMessage = null, isSuccess = false) } }


    fun submitUpdate() {
        val state = _uiState.value
        if (!isValid(state)) {
            _uiState.update { it.copy(isSaving = false) }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null, isSuccess = false) }

        viewModelScope.launch {
            val businessUpdateData = Business(
                id = state.businessId,
                name = state.businessName,
                description = state.description,
                investment = state.investment.toDoubleOrNull() ?: 0.0,
                profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
                categoryId = state.selectedCategoryId.toIntOrNull() ?: 0,
                municipalityId = state.selectedMunicipalityId,
                businessModel = state.businessModel,
                monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0,
                imageUrl = state.existingImageUrl,
            )

            updateBusinessUseCase(
                businessId = state.businessId,
                businessData = businessUpdateData,
                newImageUri = state.selectedImageUri
            ).fold(
                onSuccess = { updatedBusiness ->
                    _uiState.update {
                        it.copy(
                            isSaving = false, isSuccess = true, errorMessage = null,
                            businessName = updatedBusiness.name,
                            description = updatedBusiness.description,
                            investment = updatedBusiness.investment.toString(),
                            profitPercentage = updatedBusiness.profitPercentage.toString(),
                            selectedCategoryId = updatedBusiness.categoryId.toString(),
                            selectedMunicipalityId = updatedBusiness.municipalityId.toString(),
                            businessModel = updatedBusiness.businessModel,
                            monthlyIncome = updatedBusiness.monthlyIncome.toString(),
                            existingImageUrl = updatedBusiness.imageUrl,
                            selectedImageUri = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isSaving = false, isSuccess = false, errorMessage = "Error al actualizar: ${exception.message}") }
                }
            )
        }
    }

    private fun isValid(state: EditBusinessUiState): Boolean {
        var message: String? = null
        when {
            state.businessName.isBlank() -> message = "El nombre del negocio es requerido"
            state.description.isBlank() -> message = "La descripción es requerida"
            state.investment.isBlank() || state.investment.toDoubleOrNull() == null || state.investment.toDouble() <= 0 -> message = "La inversión debe ser un número positivo"
            state.profitPercentage.isBlank() || state.profitPercentage.toDoubleOrNull() == null || state.profitPercentage.toDouble() <= 0 -> message = "La ganancia debe ser un número positivo"
            state.selectedCategoryId.isBlank() || state.selectedCategoryId.toIntOrNull() == null -> message = "Selecciona una categoría válida"
            state.selectedStateId.isBlank() -> message = "Selecciona un estado"
            state.selectedMunicipalityId.isBlank() -> message = "Selecciona un municipio válido"
            state.businessModel.isBlank() -> message = "El modelo de negocio es requerido"
            state.monthlyIncome.isBlank() || state.monthlyIncome.toDoubleOrNull() == null || state.monthlyIncome.toDouble() <= 0 -> message = "El ingreso mensual debe ser positivo"
        }
        _uiState.update { it.copy(errorMessage = message) }
        return message == null
    }

    fun resetSuccessState() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}