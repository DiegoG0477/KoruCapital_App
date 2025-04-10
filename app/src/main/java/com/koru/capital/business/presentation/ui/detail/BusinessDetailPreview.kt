package com.koru.capital.business.presentation.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.presentation.viewmodel.BusinessDetailUiState
import com.koru.capital.core.ui.theme.KoruTheme

// Sample data for preview (adjust domain model as needed)
val sampleBusinessDetail = Business(
    name = "Tacos Finos 'El Güero'", description = "La experiencia culinaria definitiva en tacos. Usamos ingredientes frescos de origen local y recetas secretas familiares. Buscamos expandirnos a nuevas ubicaciones.",
    investment = 120000.0, profitPercentage = 35.0, categoryId = 101, municipalityId = 10,
    businessModel = "Restaurante físico con servicio a domicilio a través de apps. Modelo de franquicia en desarrollo.",
    monthlyIncome = 65000.0,
    // Added fields for preview based on DetailContent needs
    categoryName = "Restaurantes", locationName = "Zapopan, JAL", investmentRange = "100k-150k",
    imageUrl = null, // Use placeholder
    ownerName = "Don Güero", ownerEmail = "guero@tacosfinos.com", ownerPhone = "+52 33 1234 5678"
)

@Preview(showBackground = true, name = "Detail - Loading")
@Composable
private fun BusinessDetailContentLoadingPreview() {
    KoruTheme {
        BusinessDetailContent(
            uiState = BusinessDetailUiState(isLoading = true),
            onBackClick = {}, onAssociateClick = {}, onDismissAssociationDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Detail - Error")
@Composable
private fun BusinessDetailContentErrorPreview() {
    KoruTheme {
        BusinessDetailContent(
            uiState = BusinessDetailUiState(isLoading = false, errorMessage = "No se pudo cargar el negocio."),
            onBackClick = {}, onAssociateClick = {}, onDismissAssociationDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Detail - Success")
@Composable
private fun BusinessDetailContentSuccessPreview() {
    KoruTheme {
        BusinessDetailContent(
            uiState = BusinessDetailUiState(isLoading = false, business = sampleBusinessDetail),
            onBackClick = {}, onAssociateClick = {}, onDismissAssociationDialog = {}
        )
    }
}

@Preview(showBackground = true, name = "Detail - Association Dialog")
@Composable
private fun BusinessDetailAssociationDialogPreview() {
    KoruTheme {
        BusinessDetailContent(
            uiState = BusinessDetailUiState(
                isLoading = false,
                business = sampleBusinessDetail,
                showAssociationDialog = true // Show the dialog
            ),
            onBackClick = {}, onAssociateClick = {}, onDismissAssociationDialog = {}
        )
    }
}