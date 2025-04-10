package com.koru.capital.business.presentation.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.business.presentation.viewmodel.BusinessDetailViewModel

@Composable
fun BusinessDetailScreen(
    businessId: String, // Passed via navigation args
    onBackClick: () -> Unit,
    viewModel: BusinessDetailViewModel = hiltViewModel() // Scoped to this screen
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BusinessDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAssociateClick = viewModel::onAssociateClick, // Show dialog via VM
        onDismissAssociationDialog = viewModel::dismissAssociationDialog // Dismiss dialog via VM
        // Pass save/like callbacks if added:
        // onSaveToggle = viewModel::toggleSave,
        // onLikeToggle = viewModel::toggleLike
    )
}