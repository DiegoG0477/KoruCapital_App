package com.koru.capital.auth.presentation.register.ui.personal

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.auth.presentation.register.viewmodel.RegisterNavigationEvent
import com.koru.capital.auth.presentation.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterPersonalInfoScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (uiState.showDatePickerDialog) {
        val calendar = Calendar.getInstance()
        uiState.birthDate?.let {
            calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
        } ?: run {
            calendar.add(Calendar.YEAR, -18)
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                viewModel.onBirthDateChanged(LocalDate.of(year, month + 1, day))
                viewModel.onDatePickerDismissed()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener {
                viewModel.onDatePickerDismissed()
            }
            show()
        }
    }


    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is RegisterNavigationEvent.NavigateToLogin -> {
                    onRegistrationComplete()
                }
                else -> {  }
            }
        }
    }

    RegisterPersonalInfoContent(
        uiState = uiState,
        formattedBirthDate = viewModel.formatDate(uiState.birthDate),
        onFirstNameChanged = viewModel::onFirstNameChanged,
        onLastNameChanged = viewModel::onLastNameChanged,
        onBirthDateClick = viewModel::requestShowDatePicker,
        onCountrySelected = viewModel::onCountrySelected,
        onStateSelected = viewModel::onStateSelected,
        onMunicipalitySelected = viewModel::onMunicipalitySelected,
        onSubmitClick = viewModel::submitRegistration
    )
}