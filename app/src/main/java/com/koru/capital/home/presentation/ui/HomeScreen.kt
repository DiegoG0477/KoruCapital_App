package com.koru.capital.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    // Lambdas de navegación recibidas desde NavigationWrapper
    onNavigateToDetails: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        // Usa la nueva función centralizada para manejar clics en filtros
        onFilterClick = viewModel::handleFilterClick,
        onSaveClick = viewModel::toggleSaveBusiness,
        onLikeClick = viewModel::toggleLikeBusiness,
        onCardClick = { businessId ->
            // Llama a la lambda de navegación real
            onNavigateToDetails(businessId)
        },
        onSettingsClick = {
            // Llama a la lambda de navegación real
            onNavigateToSettings()
        },
        onLoadMore = viewModel::loadMoreBusinesses // Conectar la carga de más elementos
    )
}