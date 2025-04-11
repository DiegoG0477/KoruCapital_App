package com.koru.capital.business.presentation.ui.add

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.koru.capital.business.presentation.viewmodel.AddBusinessUiState
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.ui.theme.KoruTheme

val sampleStates = listOf(State("1", "Jalisco"), State("2", "Nuevo León"))
val sampleMunicipalities = listOf(Municipality("10", "Guadalajara", "1"), Municipality("11", "Zapopan", "1"))
val sampleCategories = listOf(Category("100", "Tecnología", ""), Category("101", "Comida", ""))

@Preview(showBackground = true, name = "Empty Form")
@Composable
private fun AddBusinessScreenEmptyPreview() {
    KoruTheme {
        AddBusinessContent(
            uiState = AddBusinessUiState(
                states = sampleStates,
                categories = sampleCategories
            ),
            onBackClick = {},
            onBusinessNameChanged = {},
            onDescriptionChanged = {},
            onInvestmentChanged = {},
            onProfitChanged = {},
            onCategorySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onBusinessModelChanged = {},
            onMonthlyIncomeChanged = {},
            onLaunchGallery = TODO(),
            onLaunchCamera = TODO(),
            onClearImage = TODO(),
            onSubmit = TODO(),
            modifier = TODO(),
        )
    }
}

@Preview(showBackground = true, name = "Filled Form")
@Composable
private fun AddBusinessScreenFilledPreview() {
    KoruTheme {
        AddBusinessContent(
            uiState = AddBusinessUiState(
                businessName = "Tacos El Profe",
                description = "Los mejores tacos de la ciudad.",
                investment = "150000",
                profitPercentage = "40",
                selectedCategoryId = "101",
                selectedStateId = "1",
                selectedMunicipalityId = "10",
                businessModel = "Venta directa al consumidor.",
                monthlyIncome = "50000",
                states = sampleStates,
                municipalities = sampleMunicipalities,
                categories = sampleCategories,
                selectedImageUri = "content://com.example.provider/image.jpg".toUri() // Sample URI
            ),
            onBackClick = {},
            onBusinessNameChanged = {},
            onDescriptionChanged = {},
            onInvestmentChanged = {},
            onProfitChanged = {},
            onCategorySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onBusinessModelChanged = {},
            onMonthlyIncomeChanged = {},
            onLaunchGallery = TODO(),
            onLaunchCamera = TODO(),
            onClearImage = TODO(),
            onSubmit = TODO(),
            modifier = TODO(),
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun AddBusinessScreenLoadingPreview() {
    KoruTheme {
        AddBusinessContent(
            uiState = AddBusinessUiState(isLoading = true),
            onBackClick = {},
            onBusinessNameChanged = {},
            onDescriptionChanged = {},
            onInvestmentChanged = {},
            onProfitChanged = {},
            onCategorySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onBusinessModelChanged = {},
            onMonthlyIncomeChanged = {},
            onLaunchGallery = TODO(),
            onLaunchCamera = TODO(),
            onClearImage = TODO(),
            onSubmit = TODO(),
            modifier = TODO(),
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun AddBusinessScreenErrorPreview() {
    KoruTheme {
        AddBusinessContent(
            uiState = AddBusinessUiState(
                errorMessage = "Error de validación: El nombre es requerido.",
                states = sampleStates,
                categories = sampleCategories
            ),
            onBackClick = {},
            onBusinessNameChanged = {},
            onDescriptionChanged = {},
            onInvestmentChanged = {},
            onProfitChanged = {},
            onCategorySelected = {},
            onStateSelected = {},
            onMunicipalitySelected = {},
            onBusinessModelChanged = {},
            onMonthlyIncomeChanged = {},
            onLaunchGallery = TODO(),
            onLaunchCamera = TODO(),
            onClearImage = TODO(),
            onSubmit = TODO(),
            modifier = TODO(),
        )
    }
}


@Preview(showBackground = true, name = "Success State")
@Composable
private fun AddBusinessScreenSuccessPreview() {
    KoruTheme {
        AddBusinessContent(
            uiState = AddBusinessUiState(isSuccess = true),
            onBackClick = {},
            onBusinessNameChanged = {}, onDescriptionChanged = {}, onInvestmentChanged = {},
            onProfitChanged = {}, onCategorySelected = {}, onStateSelected = {},
            onMunicipalitySelected = {}, onBusinessModelChanged = {}, onMonthlyIncomeChanged = {},
            onLaunchGallery = TODO(),
            onLaunchCamera = TODO(),
            onClearImage = TODO(),
            onSubmit = TODO(),
            modifier = TODO(),
        )
    }
}