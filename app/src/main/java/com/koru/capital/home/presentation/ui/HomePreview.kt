package com.koru.capital.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.koru.capital.core.ui.theme.KoruTheme
import com.koru.capital.home.presentation.ui.components.FilterType
import com.koru.capital.home.presentation.viewmodel.BusinessCardUiModel
import com.koru.capital.home.presentation.viewmodel.HomeUiState

val sampleBusiness1 = BusinessCardUiModel(
    id = "1",
    imageUrl = null,
    title = "Preview Negocio 1",
    category = "Tech",
    location = "GDL",
    investmentRange = "50k-100k",
    partnerCount = 2,
    description = "Descripción breve 1.",
    businessModel = "B2C",
    ownerName = "Alice",
    ownerImageUrl = null,
    savedCount = 25,
    isSaved = false,
    isLiked = true
)
val sampleBusiness2 = BusinessCardUiModel(
    id = "2",
    imageUrl = null,
    title = "Preview Negocio Largo Nombre Para Probar Wrap",
    category = "Food",
    location = "CDMX",
    investmentRange = "<50k",
    partnerCount = 0,
    description = "Descripción breve 2 con más texto para ver cómo se ajusta.",
    businessModel = "Marketplace",
    ownerName = "Bob",
    ownerImageUrl = null,
    savedCount = 10,
    isSaved = true,
    isLiked = false
)

@Preview(showBackground = true, name = "Home Loading")
@Composable
private fun HomeContentLoadingPreview() {
    KoruTheme {
        HomeContent(
            uiState = HomeUiState(isLoadingInitial = true),
            onFilterClick = {},
            onSaveClick = {},
            onLikeClick = {},
            onCardClick = {},
            onSettingsClick = {},
            onLoadMore = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Home Error")
@Composable
private fun HomeContentErrorPreview() {
    KoruTheme {
        HomeContent(
            uiState = HomeUiState(errorMessage = "No se pudo conectar. Intenta de nuevo."),
            onFilterClick = {},
            onSaveClick = {},
            onLikeClick = {},
            onCardClick = {},
            onSettingsClick = {},
            onLoadMore = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Home Empty")
@Composable
private fun HomeContentEmptyPreview() {
    KoruTheme {
        HomeContent(
            uiState = HomeUiState(businesses = emptyList()),
            onFilterClick = {},
            onSaveClick = {},
            onLikeClick = {},
            onCardClick = {},
            onSettingsClick = {},
            onLoadMore = TODO(),
            modifier = TODO()
        )
    }
}

@Preview(showBackground = true, name = "Home With Data")
@Composable
private fun HomeContentWithDataPreview() {
    KoruTheme {
        HomeContent(
            uiState = HomeUiState(
                businesses = listOf(sampleBusiness1, sampleBusiness2),
                activeFilters = setOf(FilterType.NEAR_ME)
            ),
            onFilterClick = {},
            onSaveClick = {},
            onLikeClick = {},
            onCardClick = {},
            onSettingsClick = {},
            onLoadMore = TODO(),
            modifier = TODO()
        )
    }
}