package com.koru.capital.business.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.usecase.GetCurrentUserIdUseCase // Importar para obtener ID
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.domain.usecase.AddBusinessUseCase
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Country // Importar modelo Country si se usa selector
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.usecase.GetCategoriesUseCase
import com.koru.capital.core.domain.usecase.GetCountriesUseCase // Importar UseCase de países
import com.koru.capital.core.domain.usecase.GetMunicipalitiesUseCase
import com.koru.capital.core.domain.usecase.GetStatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- Data Class (con indicadores de carga específicos) ---
data class AddBusinessUiState(
    val businessName: String = "",
    val description: String = "",
    val investment: String = "",
    val profitPercentage: String = "",
    val selectedCategoryId: String = "",
    // Añadir país si se va a seleccionar
    // val selectedCountryId: String = "", // Podría ser necesario si hay selector de país
    val selectedStateId: String = "",
    val selectedMunicipalityId: String = "",
    val businessModel: String = "",
    val monthlyIncome: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false, // Para envío final
    // Indicadores de carga específicos (opcional pero recomendado para UX)
    val isLoadingCountries: Boolean = false,
    val isLoadingStates: Boolean = false,
    val isLoadingCategories: Boolean = false,
    val isLoadingMunicipalities: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    // Listas para los dropdowns
    val countries: List<Country> = emptyList(), // Si hay selector de país
    val states: List<State> = emptyList(),
    val municipalities: List<Municipality> = emptyList(),
    val categories: List<Category> = emptyList()
)


@HiltViewModel
class AddBusinessViewModel @Inject constructor(
    // Inyectar todos los UseCases necesarios
    private val getCountriesUseCase: GetCountriesUseCase, // Añadido
    private val getStatesUseCase: GetStatesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMunicipalitiesUseCase: GetMunicipalitiesUseCase,
    private val addBusinessUseCase: AddBusinessUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBusinessUiState())
    val uiState: StateFlow<AddBusinessUiState> = _uiState.asStateFlow()

    // Podríamos definir un país por defecto si la app es solo para uno
    private val defaultCountryId = "MX" // Ejemplo: Asumir México

    init {
        loadInitialRequiredData()
    }

    // --- Data Loading ---
    private fun loadInitialRequiredData() {
        // Cargar datos que no dependen de selecciones previas
        loadCategories()
        // Cargar países si hay selector, o cargar estados del país por defecto
        // loadCountries() // Opción 1: Si el usuario selecciona país
        loadStates(defaultCountryId) // Opción 2: Cargar estados del país por defecto
    }

    // Cargar Países (si se necesita selector)
    private fun loadCountries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCountries = true, errorMessage = null) }
            getCountriesUseCase().fold(
                onSuccess = { countries ->
                    _uiState.update { it.copy(countries = countries, isLoadingCountries = false) }
                    // Podrías preseleccionar el primero si quieres
                    // if (countries.isNotEmpty()) {
                    //     // ¡OJO! Llamar a onCountrySelected aquí podría causar bucle si no se maneja bien
                    //     // _uiState.update { it.copy(selectedCountryId = countries.first().id) }
                    //     // loadStates(countries.first().id) // Cargar estados del primer país
                    // }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingCountries = false, errorMessage = "Error cargando países: ${error.message}") }
                }
            )
        }
    }

    // Cargar Estados para un País
    private fun loadStates(countryId: String) {
        if (countryId.isBlank()) return // No cargar si no hay país

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStates = true, errorMessage = null, states = emptyList(), selectedStateId = "", municipalities = emptyList(), selectedMunicipalityId = "") } // Limpiar dependientes
            getStatesUseCase(countryId).fold(
                onSuccess = { states ->
                    _uiState.update { it.copy(states = states, isLoadingStates = false) }
                    // No cargar municipios hasta que se seleccione un estado
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingStates = false, errorMessage = "Error cargando estados: ${error.message}", states = emptyList()) }
                }
            )
        }
    }

    // Cargar Categorías
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCategories = true, errorMessage = null) }
            getCategoriesUseCase().fold(
                onSuccess = { categories ->
                    _uiState.update { it.copy(categories = categories, isLoadingCategories = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingCategories = false, errorMessage = "Error cargando categorías: ${error.message}", categories = emptyList()) }
                }
            )
        }
    }

    // Cargar Municipios para un Estado
    private fun loadMunicipalities(stateId: String) {
        if (stateId.isBlank()) {
            _uiState.update { it.copy(municipalities = emptyList(), selectedMunicipalityId = "") } // Limpiar
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMunicipalities = true, errorMessage = null, municipalities = emptyList(), selectedMunicipalityId = "") } // Limpiar al cargar
            getMunicipalitiesUseCase(stateId).fold(
                onSuccess = { municipalities ->
                    _uiState.update { it.copy(municipalities = municipalities, isLoadingMunicipalities = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingMunicipalities = false, errorMessage = "Error cargando municipios: ${error.message}", municipalities = emptyList()) }
                }
            )
        }
    }

    // --- Event Handlers ---
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

    // Evento cuando se selecciona un estado en el Dropdown
    fun onStateSelected(stateId: String) {
        // Solo actuar si el ID seleccionado es diferente al actual
        if (stateId != _uiState.value.selectedStateId) {
            _uiState.update {
                it.copy(
                    selectedStateId = stateId,
                    selectedMunicipalityId = "", // Resetear municipio seleccionado
                    municipalities = emptyList(), // Limpiar lista de municipios
                    isLoadingMunicipalities = stateId.isNotBlank(), // Mostrar carga si se seleccionó un estado válido
                    errorMessage = null
                )
            }
            // Cargar municipios para el nuevo estado seleccionado
            loadMunicipalities(stateId)
        }
    }

    fun onMunicipalitySelected(municipalityId: String) {
        _uiState.update { it.copy(selectedMunicipalityId = municipalityId, errorMessage = null) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri, errorMessage = null) }
    }


    // --- Form Submission ---
    fun submitBusiness() {
        val state = _uiState.value
        if (!isValid(state)) {
            // El mensaje de error se establece dentro de isValid
            _uiState.update { it.copy(isLoading = false) } // Asegurarse que no esté cargando
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) } // Indicar carga de envío

        viewModelScope.launch {
            val ownerId = getCurrentUserIdUseCase()
            if (ownerId == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error: Sesión inválida.") }
                return@launch
            }

            // Crear objeto de dominio Business
            val businessData = Business(
                id = null, // ID es null para creación
                name = state.businessName,
                description = state.description,
                investment = state.investment.toDoubleOrNull() ?: 0.0,
                profitPercentage = state.profitPercentage.toDoubleOrNull() ?: 0.0,
                categoryId = state.selectedCategoryId.toIntOrNull() ?: 0, // Conversión segura
                municipalityId = state.selectedMunicipalityId.toIntOrNull() ?: 0, // Conversión segura
                businessModel = state.businessModel,
                monthlyIncome = state.monthlyIncome.toDoubleOrNull() ?: 0.0,
                imageUrl = null // La URL se manejará vía imageUri en el UseCase
            )

            // Llamar al UseCase
            addBusinessUseCase(ownerId, businessData, state.selectedImageUri).fold(
                onSuccess = { createdBusiness ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    // La Screen se encargará de navegar al detectar isSuccess = true
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Error al crear negocio: ${exception.message}") }
                }
            )
        }
    }

    // --- Validation Helper ---
    private fun isValid(state: AddBusinessUiState): Boolean {
        var message: String? = null
        when {
            state.businessName.isBlank() -> message = "El nombre del negocio es requerido"
            state.description.isBlank() -> message = "La descripción es requerida"
            state.investment.isBlank() || state.investment.toDoubleOrNull() == null || state.investment.toDouble() <= 0 -> message = "La inversión debe ser un número positivo"
            state.profitPercentage.isBlank() || state.profitPercentage.toDoubleOrNull() == null || state.profitPercentage.toDouble() <= 0 -> message = "La ganancia debe ser un número positivo"
            state.selectedCategoryId.isBlank() || state.selectedCategoryId.toIntOrNull() == null -> message = "Selecciona una categoría válida"
            state.selectedStateId.isBlank() -> message = "Selecciona un estado"
            state.selectedMunicipalityId.isBlank() || state.selectedMunicipalityId.toIntOrNull() == null -> message = "Selecciona un municipio válido"
            state.businessModel.isBlank() -> message = "El modelo de negocio es requerido"
            state.monthlyIncome.isBlank() || state.monthlyIncome.toDoubleOrNull() == null || state.monthlyIncome.toDouble() <= 0 -> message = "El ingreso mensual debe ser positivo"
            // state.selectedImageUri == null -> message = "La imagen del negocio es requerida" // Hacerla opcional?
        }
        _uiState.update { it.copy(errorMessage = message) }
        return message == null
    }

    // --- Reset State ---
    fun resetFormState() {
        // Mantener listas cargadas, limpiar el resto
        _uiState.update { currentState ->
            AddBusinessUiState(
                // Mantener listas
                countries = currentState.countries,
                states = currentState.states,
                categories = currentState.categories,
                // Resetear el resto
                businessName = "",
                description = "",
                investment = "",
                profitPercentage = "",
                selectedCategoryId = "",
                selectedStateId = "", // Podrías mantener el país si es fijo
                selectedMunicipalityId = "",
                businessModel = "",
                monthlyIncome = "",
                selectedImageUri = null,
                isLoading = false,
                isLoadingCountries = false,
                isLoadingStates = false,
                isLoadingCategories = false,
                isLoadingMunicipalities = false,
                isSuccess = false,
                errorMessage = null,
                municipalities = emptyList() // Limpiar municipios al resetear
            )
        }
        // Podrías recargar estados si limpiaste la selección de país (si existiera)
        // if (_uiState.value.selectedCountryId.isBlank() && _uiState.value.countries.isNotEmpty()){}
        // O recargar estados del país por defecto si limpiaste selectedStateId
        // if(_uiState.value.selectedStateId.isBlank()){ loadStates(defaultCountryId) }
    }
}