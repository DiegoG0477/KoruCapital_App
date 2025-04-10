package com.koru.capital.auth.presentation.register.ui.personal

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.auth.presentation.register.viewmodel.RegisterStep
import com.koru.capital.auth.presentation.register.viewmodel.RegisterUiState
import com.koru.capital.core.ui.theme.KoruTheme
import java.time.LocalDate

val sampleCountry = Country("MX", "México")
val sampleState = State("MX-JAL", "Jalisco")
val sampleMunicipality = Municipality("MX-JAL-GDL", "Guadalajara", "MX-JAL")

@Preview(showBackground = true, name = "Personal Info - Empty")
@Composable
private fun RegisterPersonalInfoContentEmptyPreview() {
    KoruTheme {
        RegisterPersonalInfoContent(
            uiState = RegisterUiState(
                currentStep = RegisterStep.PERSONAL_INFO,
                countries = listOf(sampleCountry), // Provide some data for preview
                states = listOf(sampleState)
            ),
            formattedBirthDate = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onBirthDateClick = {},
            onCountrySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onSubmitClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Personal Info - Filled")
@Composable
private fun RegisterPersonalInfoContentFilledPreview() {
    KoruTheme {
        RegisterPersonalInfoContent(
            uiState = RegisterUiState(
                currentStep = RegisterStep.PERSONAL_INFO,
                firstName = "Juan",
                lastName = "Perez",
                birthDate = LocalDate.of(1995, 5, 15),
                selectedCountry = sampleCountry,
                selectedState = sampleState,
                selectedMunicipality = sampleMunicipality,
                countries = listOf(sampleCountry),
                states = listOf(sampleState),
                municipalities = listOf(sampleMunicipality)
            ),
            formattedBirthDate = "15/05/1995",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onBirthDateClick = {},
            onCountrySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onSubmitClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Personal Info - Error")
@Composable
private fun RegisterPersonalInfoContentErrorPreview() {
    KoruTheme {
        RegisterPersonalInfoContent(
            uiState = RegisterUiState(
                currentStep = RegisterStep.PERSONAL_INFO,
                firstName = "Ana",
                // Missing last name, date, municipality
                selectedCountry = sampleCountry,
                selectedState = sampleState,
                countries = listOf(sampleCountry),
                states = listOf(sampleState),
                municipalities = listOf(sampleMunicipality),
                registrationError = "Completa todos los campos requeridos"
            ),
            formattedBirthDate = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onBirthDateClick = {},
            onCountrySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onSubmitClick = {}
        )
    }
}


@Preview(showBackground = true, name = "Personal Info - Loading Dropdowns")
@Composable
private fun RegisterPersonalInfoContentLoadingDropdownsPreview() {
    KoruTheme {
        RegisterPersonalInfoContent(
            uiState = RegisterUiState(
                currentStep = RegisterStep.PERSONAL_INFO,
                firstName = "Test",
                lastName = "User",
                isLoading = true, // Simulate loading state
                countries = emptyList() // No countries loaded yet
            ),
            formattedBirthDate = "",
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onBirthDateClick = {},
            onCountrySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onSubmitClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Personal Info - Submitting")
@Composable
private fun RegisterPersonalInfoContentSubmittingPreview() {
    KoruTheme {
        RegisterPersonalInfoContent(
            uiState = RegisterUiState(
                currentStep = RegisterStep.PERSONAL_INFO,
                firstName = "Juan", lastName = "Perez", birthDate = LocalDate.of(1995, 5, 15),
                selectedCountry = sampleCountry, selectedState = sampleState, selectedMunicipality = sampleMunicipality,
                countries = listOf(sampleCountry), states = listOf(sampleState), municipalities = listOf(sampleMunicipality),
                isLoading = true // Simulate submission loading
            ),
            formattedBirthDate = "15/05/1995",
            onFirstNameChanged = {}, onLastNameChanged = {}, onBirthDateClick = {}, onCountrySelected = {}, onStateSelected = {}, onMunicipalitySelected = {}, onSubmitClick = {}
        )
    }
}