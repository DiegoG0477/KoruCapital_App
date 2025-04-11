package com.koru.capital.business.presentation.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.business.presentation.viewmodel.BusinessDetailViewModel

@Composable
fun BusinessDetailScreen(
    businessId: String,
    onBackClick: () -> Unit,
    viewModel: BusinessDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAssociateClick = viewModel::onAssociateClick,
        onDismissAssociationDialog = viewModel::dismissAssociationDialog
    )
}