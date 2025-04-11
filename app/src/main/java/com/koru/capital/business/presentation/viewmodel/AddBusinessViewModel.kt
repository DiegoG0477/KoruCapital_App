package com.koru.capital.business.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.usecase.GetCurrentUserIdUseCase
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.usecase.AddBusinessUseCase
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.usecase.GetCategoriesUseCase
import com.koru.capital.core.domain.usecase.GetCountriesUseCase
import com.koru.capital.core.domain.usecase.GetMunicipalitiesUseCase
import com.koru.capital.core.domain.usecase.GetStatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddBusinessUiState(
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
    val isLoading: Boolean = false,
    val isLoadingCountries: Boolean = false,
    val isLoadingStates: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingMunicipalities: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val countries: List<Country> = emptyList(),
    val states: List<State> = emptyList(),
    val municipalities: List<Municipality> = emptyList(),
    val categories: List<Category> = emptyList()
)

@HiltViewModel
class AddBusinessViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getStatesUseCase: GetStatesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMunicipalitiesUseCase: GetMunicipalitiesUseCase,
    private val addBusinessUseCase: AddBusinessUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBusinessUiState())
    val uiState: StateFlow<AddBusinessUiState> = _uiState.asStateFlow()

    private val defaultCountryId = "MX"

    init {
        loadInitialRequiredData()
    }

    private fun loadInitialRequiredData() {
        loadCategories()
        loadStates(defaultCountryId)
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCountries = true, errorMessage = null) }
            getCountriesUseCase().fold(
                onSuccess = { countries -> _uiState.update { it.copy(countries = countries, isLoadingCountries = false) } },
                onFailure = { error -> _uiState.update { it.copy(isLoadingCountries = false, errorMessage = "Error países: ${error.message}", countries = emptyList()) } }
            )
        }
    }

    private fun loadStates(countryId: String) {
        if (countryId.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStates = true, errorMessage = null, states = emptyList(), selectedStateId = "", municipalities = emptyList(), selectedMunicipalityId = "") }
            getStatesUseCase(countryId).fold(
                onSuccess = { states -> _uiState.update { it.copy(states = states, isLoadingStates = false) } },
                onFailure = { error -> _uiState.update { it.copy(isLoadingStates = false, errorMessage = "Error estados: ${error.message}", states = emptyList()) } }
            )
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCategories = true, errorMessage = null) }
            getCategoriesUseCase().fold(
                onSuccess = { categories -> _uiState.update { it.copy(categories = categories, isLoadingCategories = false) } },
                onFailure = { error -> _uiState.update { it.copy(isLoadingCategories = false, errorMessage = "Error categorías: ${error.message}", categories = emptyList()) } }
            )
        }
    }

    private fun loadMunicipalities(stateId: String) {
        if (stateId.isBlank()) {
            _uiState.update { it.copy(municipalities = emptyList(), selectedMunicipalityId = "") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMunicipalities = true, errorMessage = null, municipalities = emptyList(), selectedMunicipalityId = "") }
            getMunicipalitiesUseCase(stateId).fold(
                onSuccess = { municipalities -> _uiState.update { it.copy(municipalities = municipalities, isLoadingMunicipalities = false) } },
                onFailure = { error -> _uiState.update { it.copy(isLoadingMunicipalities = false, errorMessage = "Error municipios: ${error.message}", municipalities = emptyList()) } }
            )
        }
    }

    fun onBusinessNameChanged(name: String) { _uiState.update { it.copy(businessName = name, errorMessage = null) } }
    fun onDescriptionChanged(description: String) { _uiState.update { it.copy(description = description, errorMessage = null) } }
    fun onInvestmentChanged(investment: String) {
        val filtered = investment.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(investment = filtered, errorMessage = null) } }
    }
    fun onProfitChanged(profit: String) {
        val filtered = profit.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(profitPercentage = filtered, errorMessage = null) } }
    }
    fun onCategorySelected(categoryId: String) { _uiState.update { it.copy(selectedCategoryId = categoryId, errorMessage = null) } }
    fun onBusinessModelChanged(model: String) { _uiState.update { it.copy(businessModel = model, errorMessage = null) } }
    fun onMonthlyIncomeChanged(income: String) {
        val filtered = income.filter { it.isDigit() || it == '.' }
        if (filtered.count { it == '.' } <= 1) { _uiState.update { it.copy(monthlyIncome = filtered, errorMessage = null) } }
    }

    fun onStateSelected(stateId: String) {
        if (stateId != _uiState.value.selectedStateId) {
            _uiState.update {
                it.copy(
                    selectedStateId = stateId,
                    selectedMunicipalityId = "",
                    municipalities = emptyList(),
                    isLoadingMunicipalities = stateId.isNotBlank(),
                    errorMessage = null
                )
            }
            loadMunicipalities(stateId)
        }
    }
    fun onMunicipalitySelected(municipalityId: String) {
        _uiState.update { it.copy(selectedMunicipalityId = municipalityId, errorMessage = null) }
    }
    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri, errorMessage = null) }
    }

    fun submitBusiness() {
        val state = _uiState.value
        if (!isValid(state)) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val ownerId = getCurrentUserIdUseCase()
            if (ownerId == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error: Sesión inválida.") }
                return@launch
            }

            val businessData = Business(
                id = null,
                name = state.businessName,
                description = state.description,
                investment = state.investment.toDoubleOrNull() ?: 0.0,
                profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
                categoryId = state.selectedCategoryId.toIntOrNull() ?: 0,
                municipalityId = state.selectedMunicipalityId,
                businessModel = state.businessModel,
                monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0,
                imageUrl = null
            )

            addBusinessUseCase(ownerId, businessData, state.selectedImageUri).fold(
                onSuccess = { _ ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error al crear negocio: ${exception.message}") }
                }
            )
        }
    }

    private fun isValid(state: AddBusinessUiState): Boolean {
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

    fun resetFormState() {
        _uiState.update { currentState ->
            AddBusinessUiState(
                countries = currentState.countries,
                states = currentState.states,
                categories = currentState.categories,
                municipalities = emptyList()
            )
        }
    }
}