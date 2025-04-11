package com.koru.capital.auth.presentation.register.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.koru.capital.auth.domain.model.RegistrationData
import com.koru.capital.auth.domain.usecase.RegisterUserUseCase
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.domain.usecase.GetCountriesUseCase
import com.koru.capital.core.domain.usecase.GetMunicipalitiesUseCase
import com.koru.capital.core.domain.usecase.GetStatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val emailErrorMessage: String? = null,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isPasswordValid: Boolean = true,
    val passwordErrorMessage: String? = null,
    val confirmPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false,
    val isConfirmPasswordValid: Boolean = true,
    val confirmPasswordErrorMessage: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val showDatePickerDialog: Boolean = false,
    val selectedCountry: Country? = null,
    val selectedState: State? = null,
    val selectedMunicipality: Municipality? = null,
    val countries: List<Country> = emptyList(),
    val isLoadingCountries: Boolean = false,
    val states: List<State> = emptyList(),
    val isLoadingStates: Boolean = false,
    val municipalities: List<Municipality> = emptyList(),
    val isLoadingMunicipalities: Boolean = false,
    val isLoading: Boolean = false,
    val currentStep: RegisterStep = RegisterStep.EMAIL_PASSWORD,
    val registrationError: String? = null,
    val isRegistrationComplete: Boolean = false
)

enum class RegisterStep { EMAIL_PASSWORD, PERSONAL_INFO }
sealed class RegisterNavigationEvent { object NavigateToPersonalInfo : RegisterNavigationEvent(); object NavigateToLogin : RegisterNavigationEvent() }

@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getStatesUseCase: GetStatesUseCase,
    private val getMunicipalitiesUseCase: GetMunicipalitiesUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RegisterNavigationEvent>()
    val navigationEvent: SharedFlow<RegisterNavigationEvent> = _navigationEvent.asSharedFlow()

    private val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    @RequiresApi(Build.VERSION_CODES.O)
    private val birthDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        loadCountries()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingCountries = true, registrationError = null) }
            getCountriesUseCase().fold(
                onSuccess = { countries ->
                    _uiState.update { it.copy(countries = countries, isLoadingCountries = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingCountries = false, registrationError = "Error cargando países: ${error.message}", countries = emptyList()) }
                }
            )
        }
    }

    private fun loadStatesForCountry(country: Country) {
        val countryId = country.id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStates = true, registrationError = null, states = emptyList(), selectedState = null, municipalities = emptyList(), selectedMunicipality = null) }
            getStatesUseCase(countryId).fold(
                onSuccess = { states ->
                    _uiState.update { it.copy(states = states, isLoadingStates = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingStates = false, registrationError = "Error cargando estados: ${error.message}", states = emptyList()) }
                }
            )
        }
    }

    private fun loadMunicipalitiesForState(state: State) {
        val stateId = state.id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMunicipalities = true, registrationError = null, municipalities = emptyList(), selectedMunicipality = null) }
            getMunicipalitiesUseCase(stateId).fold(
                onSuccess = { municipalities ->
                    _uiState.update { it.copy(municipalities = municipalities, isLoadingMunicipalities = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoadingMunicipalities = false, registrationError = "Error cargando municipios: ${error.message}", municipalities = emptyList()) }
                }
            )
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, isEmailValid = true, emailErrorMessage = null, registrationError = null) }
    }
    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, isPasswordValid = true, passwordErrorMessage = null, registrationError = null) }
    }
    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, isConfirmPasswordValid = true, confirmPasswordErrorMessage = null, registrationError = null) }
    }
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun proceedToPersonalInfo() {
        val state = _uiState.value
        val isEmailValid = emailRegex.matches(state.email)
        val isPasswordValid = state.password.length >= 8
        val doPasswordsMatch = state.password == state.confirmPassword

        _uiState.update {
            it.copy(
                isEmailValid = isEmailValid, emailErrorMessage = if (!isEmailValid && state.email.isNotEmpty()) "Formato inválido" else null,
                isPasswordValid = isPasswordValid, passwordErrorMessage = if (isPasswordValid) null else "Mínimo 8 caracteres",
                isConfirmPasswordValid = doPasswordsMatch, confirmPasswordErrorMessage = if (doPasswordsMatch) null else "Las contraseñas no coinciden",
                registrationError = null
            )
        }

        if (isEmailValid && isPasswordValid && doPasswordsMatch) {
            _uiState.update { it.copy(currentStep = RegisterStep.PERSONAL_INFO) }
            viewModelScope.launch {
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToPersonalInfo)
            }
        } else {
            _uiState.update { it.copy(registrationError = "Por favor corrige los errores") }
        }
    }

    fun onFirstNameChanged(name: String) {
        _uiState.update { it.copy(firstName = name, registrationError = null) }
    }
    fun onLastNameChanged(name: String) {
        _uiState.update { it.copy(lastName = name, registrationError = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onBirthDateChanged(date: LocalDate?) {
        _uiState.update { it.copy(birthDate = date, registrationError = null, showDatePickerDialog = false) }
    }

    fun requestShowDatePicker() {
        _uiState.update { it.copy(showDatePickerDialog = true) }
    }

    fun onDatePickerDismissed() {
        _uiState.update { it.copy(showDatePickerDialog = false) }
    }

    fun onCountrySelected(country: Country?) {
        if (country != _uiState.value.selectedCountry) {
            _uiState.update {
                it.copy(
                    selectedCountry = country,
                    selectedState = null,
                    selectedMunicipality = null,
                    states = emptyList(),
                    municipalities = emptyList(),
                    registrationError = null,
                    isLoadingStates = country != null,
                    isLoadingMunicipalities = false
                )
            }
            if (country != null) {
                loadStatesForCountry(country)
            } else {
                _uiState.update { it.copy(states = emptyList(), isLoadingStates = false) }
            }
        }
    }

    fun onStateSelected(state: State?) {
        if (state != _uiState.value.selectedState) {
            _uiState.update {
                it.copy(
                    selectedState = state,
                    selectedMunicipality = null,
                    municipalities = emptyList(),
                    registrationError = null,
                    isLoadingMunicipalities = state != null
                )
            }
            if (state != null) {
                loadMunicipalitiesForState(state)
            } else {
                _uiState.update { it.copy(municipalities = emptyList(), isLoadingMunicipalities = false) }
            }
        }
    }

    fun onMunicipalitySelected(municipality: Municipality?) {
        _uiState.update { it.copy(selectedMunicipality = municipality, registrationError = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun submitRegistration() {
        if (!validatePersonalInfo()) {
            _uiState.update { it.copy(registrationError = "Completa todos los campos requeridos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, registrationError = null) }
        val state = _uiState.value

        val registrationData = RegistrationData(
            email = state.email,
            password = state.password,
            firstName = state.firstName,
            lastName = state.lastName,
            birthDate = state.birthDate!!,
            countryId = state.selectedCountry!!.id,
            stateId = state.selectedState!!.id,
            municipalityId = state.selectedMunicipality!!.id
        )

        viewModelScope.launch {
            registerUserUseCase(registrationData).fold(
                onSuccess = { authToken ->
                    _uiState.update { it.copy(isLoading = false, isRegistrationComplete = true) }
                    _navigationEvent.emit(RegisterNavigationEvent.NavigateToLogin)
                },
                onFailure = { exception ->
                    if (exception.message == "Registration succeeded but failed to process token data.") {
                        println("WARN: Registration successful, but auto-login token failed. Navigating to login.")
                        _uiState.update { it.copy(isLoading = false, isRegistrationComplete = false, registrationError = null) }
                        _navigationEvent.emit(RegisterNavigationEvent.NavigateToLogin)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                registrationError = "Error en el registro: ${exception.message}"
                            )
                        }
                    }
                }
            )
        }
    }

    private fun validatePersonalInfo(): Boolean {
        val state = _uiState.value
        return state.firstName.isNotBlank() &&
                state.lastName.isNotBlank() &&
                state.birthDate != null &&
                state.selectedCountry != null &&
                state.selectedState != null &&
                state.selectedMunicipality != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(date: LocalDate?): String {
        return date?.format(birthDateFormatter) ?: ""
    }
}