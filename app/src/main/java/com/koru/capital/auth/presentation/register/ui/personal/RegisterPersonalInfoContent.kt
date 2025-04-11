package com.koru.capital.auth.presentation.register.ui.personal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.domain.model.Country
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.register.presentation.components.DatePickerField
import com.koru.capital.register.presentation.components.DropdownField
import com.koru.capital.register.presentation.components.LabeledTextField
import com.koru.capital.auth.presentation.register.viewmodel.RegisterStep
import com.koru.capital.auth.presentation.register.viewmodel.RegisterUiState
import com.koru.capital.core.ui.theme.*

@Composable
fun RegisterPersonalInfoContent(
    uiState: RegisterUiState,
    formattedBirthDate: String,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onBirthDateClick: () -> Unit,
    onCountrySelected: (Country?) -> Unit,
    onStateSelected: (State?) -> Unit,
    onMunicipalitySelected: (Municipality?) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Información Personal",
            fontFamily = funnelSansFamily,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = KoruBlack,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LabeledTextField(
            value = uiState.firstName,
            onValueChange = onFirstNameChanged,
            label = "Nombres",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LabeledTextField(
            value = uiState.lastName,
            onValueChange = onLastNameChanged,
            label = "Apellidos",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        DatePickerField(
            value = formattedBirthDate,
            label = "Fecha de Nacimiento",
            onFieldClick = onBirthDateClick,
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.registrationError != null && uiState.birthDate == null
        )

        DropdownField(
            label = "País",
            options = uiState.countries,
            selectedOption = uiState.selectedCountry,
            onOptionSelected = onCountrySelected,
            optionToString = { it.name },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.countries.isNotEmpty() && !uiState.isLoading,
            placeholder = if (uiState.isLoading && uiState.countries.isEmpty()) "Cargando..." else "Selecciona país",
            isError = uiState.registrationError != null && uiState.selectedCountry == null
        )

        DropdownField(
            label = "Estado",
            options = uiState.states,
            selectedOption = uiState.selectedState,
            onOptionSelected = onStateSelected,
            optionToString = { it.name },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.selectedCountry != null && uiState.states.isNotEmpty() && !uiState.isLoading,
            placeholder = when {
                uiState.selectedCountry == null -> "Selecciona un país primero"
                uiState.isLoading && uiState.states.isEmpty() -> "Cargando..."
                else -> "Selecciona estado"
            },
            isError = uiState.registrationError != null && uiState.selectedState == null
        )

        DropdownField(
            label = "Municipio",
            options = uiState.municipalities,
            selectedOption = uiState.selectedMunicipality,
            onOptionSelected = onMunicipalitySelected,
            optionToString = { it.name },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.selectedState != null && uiState.municipalities.isNotEmpty() && !uiState.isLoading,
            placeholder = when {
                uiState.selectedState == null -> "Selecciona un estado primero"
                uiState.isLoading && uiState.municipalities.isEmpty() -> "Cargando..."
                else -> "Selecciona municipio"
            },
            isError = uiState.registrationError != null && uiState.selectedMunicipality == null
        )

        if (uiState.registrationError != null && uiState.currentStep == RegisterStep.PERSONAL_INFO) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.registrationError,
                color = KoruRed,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }


        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KoruOrangeAlternative,
                contentColor = KoruWhite
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = KoruWhite,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Registrarse",
                    fontFamily = funnelSansFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}