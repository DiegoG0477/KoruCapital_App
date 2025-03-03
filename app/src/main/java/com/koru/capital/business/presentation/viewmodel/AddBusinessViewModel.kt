package com.koru.capital.business.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.business.domain.Business
import com.koru.capital.business.domain.usecase.AddBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBusinessViewModel @Inject constructor(
    private val addBusinessUseCase: AddBusinessUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBusinessUiState())
    val uiState: StateFlow<AddBusinessUiState> = _uiState

    // Métodos para actualizar cada campo (en un ejemplo real podrías abstraerlos)
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
    fun onCategoryChanged(category: String) {
        _uiState.value = _uiState.value.copy(category = category)
    }
    fun onBusinessModelChanged(model: String) {
        _uiState.value = _uiState.value.copy(businessModel = model)
    }
    fun onMonthlyIncomeChanged(income: String) {
        _uiState.value = _uiState.value.copy(monthlyIncome = income)
    }

    fun submitBusiness() {
        val state = _uiState.value
        // Convertir los valores a tipos numéricos y construir el objeto de dominio
        val business = Business(
            name = state.businessName,
            description = state.description,
            investment = state.investment.toDoubleOrNull() ?: 0.0,
            profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
            category = state.category,
            businessModel = state.businessModel,
            monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0
        )
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = addBusinessUseCase(business)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isSuccess = true, isLoading = false)
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.exceptionOrNull()?.message,
                    isLoading = false
                )
            }
        }
    }
}

data class AddBusinessUiState(
    val businessName: String = "",
    val description: String = "",
    val investment: String = "",
    val profitPercentage: String = "",
    val category: String = "",
    val businessModel: String = "",
    val monthlyIncome: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
