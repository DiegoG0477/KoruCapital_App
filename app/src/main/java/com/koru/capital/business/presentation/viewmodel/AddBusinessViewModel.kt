package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.Business
import com.koru.capital.business.domain.usecase.AddBusinessUseCase
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.usecase.GetCategoriesUseCase
import com.koru.capital.core.domain.usecase.GetMunicipalitiesUseCase
import com.koru.capital.core.domain.usecase.GetStatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBusinessViewModel @Inject constructor(
    private val getStatesUseCase: GetStatesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMunicipalitiesUseCase: GetMunicipalitiesUseCase,
    private val addBusinessUseCase: AddBusinessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBusinessUiState())
    val uiState: StateFlow<AddBusinessUiState> = _uiState

    init {
        // Cargar estados y categorías en la inicialización
        loadStates()
        loadCategories()
    }

    private fun loadStates() {
        viewModelScope.launch {
            try {
                getStatesUseCase().collect { states ->
                    _uiState.value = _uiState.value.copy(states = states)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar estados: ${e.message}"
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                getCategoriesUseCase().collect { categories ->
                    _uiState.value = _uiState.value.copy(categories = categories)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar categorías: ${e.message}"
                )
            }
        }
    }

    fun onBusinessNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(businessName = name)
    }

    fun onDescriptionChanged(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onInvestmentChanged(investment: String) {
        _uiState.value = _uiState.value.copy(investment = investment)
    }

    fun onProfitChanged(profit: String) {
        _uiState.value = _uiState.value.copy(profitPercentage = profit)
    }

    fun onCategoryChanged(categoryId: String) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
    }

    fun onBusinessModelChanged(model: String) {
        _uiState.value = _uiState.value.copy(businessModel = model)
    }

    fun onMonthlyIncomeChanged(income: String) {
        _uiState.value = _uiState.value.copy(monthlyIncome = income)
    }

    // Al seleccionar un estado, carga los municipios correspondientes
    fun onStateChanged(stateId: String) {
        _uiState.value = _uiState.value.copy(
            selectedStateId = stateId,
            selectedMunicipalityId = "", // Resetear municipio seleccionado
            municipalities = emptyList() // Limpiar lista de municipios mientras se cargan los nuevos
        )
        loadMunicipalities(stateId)
    }

    private fun loadMunicipalities(stateId: String) {
        viewModelScope.launch {
            try {
                val municipalities = getMunicipalitiesUseCase(stateId)
                _uiState.value = _uiState.value.copy(municipalities = municipalities)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al cargar municipios: ${e.message}"
                )
            }
        }
    }

    fun onMunicipalityChanged(municipalityId: String) {
        _uiState.value = _uiState.value.copy(selectedMunicipalityId = municipalityId)
    }

    fun submitBusiness() {
        val state = _uiState.value

        // Validar campos requeridos
        if (state.businessName.isBlank() ||
            state.description.isBlank() ||
            state.investment.isBlank() ||
            state.profitPercentage.isBlank() ||
            state.selectedCategoryId.isBlank() ||
            state.selectedMunicipalityId.isBlank() ||
            state.businessModel.isBlank() ||
            state.monthlyIncome.isBlank()) {

            _uiState.value = _uiState.value.copy(
                errorMessage = "Por favor completa todos los campos requeridos"
            )
            return
        }

        // Crear el objeto Business
        try {
            val business = Business(
                name = state.businessName,
                description = state.description,
                investment = state.investment.toDoubleOrNull() ?: 0.0,
                profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
                categoryId = state.selectedCategoryId.toInt(),
                municipalityId = state.selectedMunicipalityId.toInt(),
                businessModel = state.businessModel,
                monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0
            )

            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                try {
                    val result = addBusinessUseCase(business)

                    if (result.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            errorMessage = null
                        )
                    } else {
                        val exception = result.exceptionOrNull()
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = false,
                            errorMessage = "Error al crear negocio: ${exception?.message ?: "Error desconocido"}"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Error al crear negocio: ${e.message}"
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Error en los datos: ${e.message}"
            )
        }
    }
}

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
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val states: List<State> = emptyList(),
    val municipalities: List<Municipality> = emptyList(),
    val categories: List<Category> = emptyList()
)