package com.koru.capital.home.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onFilterClick = viewModel::handleFilterClick,
        onSaveClick = viewModel::toggleSaveBusiness,
        onLikeClick = viewModel::toggleLikeBusiness,
        onCardClick = { businessId ->
            onNavigateToDetails(businessId)
        },
        onSettingsClick = {
            onNavigateToSettings()
        },
        onLoadMore = viewModel::loadMoreBusinesses
    )
}