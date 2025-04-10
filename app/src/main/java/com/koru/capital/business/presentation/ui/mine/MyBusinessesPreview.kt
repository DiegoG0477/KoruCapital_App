package com.koru.capital.business.presentation.ui.mine

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.business.presentation.viewmodel.MyBusinessesUiState
import com.koru.capital.core.ui.theme.KoruTheme

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun MyBusinessesScreenEmptyPreview() {
    KoruTheme {
        MyBusinessesContent(
            uiState = MyBusinessesUiState(isLoading = false, businesses = emptyList()),
            onAddBusiness = {},
            onFilterSelected = {},
            onEditBusiness = TODO(),
            onDeleteRequest = TODO(),
            onConfirmDelete = TODO(),
            onDismissDelete = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun MyBusinessesScreenLoadingPreview() {
    KoruTheme {
        MyBusinessesContent(
            uiState = MyBusinessesUiState(isLoading = true),
            onAddBusiness = {},
            onFilterSelected = {},
            onEditBusiness = TODO(),
            onDeleteRequest = TODO(),
            onConfirmDelete = TODO(),
            onDismissDelete = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun MyBusinessesScreenErrorPreview() {
    KoruTheme {
        MyBusinessesContent(
            uiState = MyBusinessesUiState(isLoading = false, errorMessage = "Failed to load data"),
            onAddBusiness = {},
            onFilterSelected = {},
            onEditBusiness = TODO(),
            onDeleteRequest = TODO(),
            onConfirmDelete = TODO(),
            onDismissDelete = TODO(),
            modifier = TODO()
        )
    }
}

// Add Preview with data once BusinessUiModel and BusinessList are defined
/*
@Preview(showBackground = true, name = "With Data")
@Composable
private fun MyBusinessesScreenWithDataPreview() {
    val sampleBusinesses = listOf(
        BusinessUiModel(id = "1", name = "My First Business"),
        BusinessUiModel(id = "2", name = "Another Venture")
    )
    MexiCrowdTheme {
        MyBusinessesContent(
            uiState = MyBusinessesUiState(isLoading = false, businesses = sampleBusinesses),
            onAddBusiness = {},
            onFilterSelected = {}
        )
    }
}
*/