package com.koru.capital.auth.presentation.register.ui.personal

import android.app.DatePickerDialog // <-- IMPORTAR DatePickerDialog
import android.os.Build // <-- IMPORTAR Build
import androidx.annotation.RequiresApi // <-- IMPORTAR RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext // <-- IMPORTAR LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.auth.presentation.register.viewmodel.RegisterNavigationEvent
import com.koru.capital.auth.presentation.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate // <-- IMPORTAR LocalDate
import java.util.Calendar // <-- IMPORTAR Calendar

// Añadir anotación RequiresApi si tu minSdk es menor a 26 (Oreo)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterPersonalInfoScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current // Obtener el contexto del Composable

    // --- Mostrar DatePickerDialog cuando el estado lo indique ---
    if (uiState.showDatePickerDialog) {
        val calendar = Calendar.getInstance()
        // Configurar fecha inicial del diálogo con la fecha seleccionada o una por defecto
        uiState.birthDate?.let {
            calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
        } ?: run {
            // Fecha por defecto si no hay ninguna seleccionada (ej: hace 18 años)
            calendar.add(Calendar.YEAR, -18)
        }

        DatePickerDialog(
            context, // <-- USAR EL CONTEXTO DEL COMPOSABLE
            { _, year, month, day ->
                // Llamar al ViewModel cuando se SELECCIONA una fecha
                viewModel.onBirthDateChanged(LocalDate.of(year, month + 1, day))
                viewModel.onDatePickerDismissed() // Ocultar el diálogo después de seleccionar
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Llamar al ViewModel cuando se CIERRA el diálogo (Cancelar o click fuera)
            setOnDismissListener {
                viewModel.onDatePickerDismissed()
            }
            // Opcional: Configurar límites de fecha
            // datePicker.maxDate = System.currentTimeMillis()
            show() // Mostrar el diálogo
        }
    }
    // --- Fin lógica DatePickerDialog ---


    // Handle navigation event (e.g., success)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is RegisterNavigationEvent.NavigateToLogin -> {
                    onRegistrationComplete()
                }
                else -> { /* Ignore other events on this screen */ }
            }
        }
    }

    RegisterPersonalInfoContent(
        uiState = uiState,
        formattedBirthDate = viewModel.formatDate(uiState.birthDate),
        onFirstNameChanged = viewModel::onFirstNameChanged,
        onLastNameChanged = viewModel::onLastNameChanged,
        // CAMBIO: Llamar a la función del VM que actualiza el flag
        onBirthDateClick = viewModel::requestShowDatePicker,
        onCountrySelected = viewModel::onCountrySelected,
        onStateSelected = viewModel::onStateSelected,
        onMunicipalitySelected = viewModel::onMunicipalitySelected,
        onSubmitClick = viewModel::submitRegistration
    )
}