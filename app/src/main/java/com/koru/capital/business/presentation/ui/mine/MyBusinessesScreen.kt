package com.koru.capital.business.presentation.ui.mine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koru.capital.business.presentation.viewmodel.MyBusinessesNavigationEvent
import com.koru.capital.business.presentation.viewmodel.MyBusinessesViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyBusinessesScreen(
    onAddBusiness: () -> Unit,
    onEditBusiness: (String) -> Unit, // Add navigation lambda for editing
    viewModel: MyBusinessesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle navigation events
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is MyBusinessesNavigationEvent.NavigateToEditBusiness -> {
                    onEditBusiness(event.businessId)
                }
                is MyBusinessesNavigationEvent.NavigateToAddBusiness -> {
                    onAddBusiness() // Still handle add navigation if triggered from VM
                }
            }
        }
    }

    MyBusinessesContent(
        uiState = uiState,
        // Prefer triggering navigation via VM events when possible, but onAddBusiness direct nav is fine too
        onAddBusiness = viewModel::onAddBusinessClick, // Or directly use onAddBusiness lambda if preferred
        onFilterSelected = viewModel::onFilterSelected,
        onEditBusiness = viewModel::onEditBusinessClick, // Trigger VM event
        onDeleteRequest = viewModel::onDeleteBusinessRequest, // Trigger VM delete request
        onConfirmDelete = viewModel::onConfirmDelete, // Confirm delete in VM
        onDismissDelete = viewModel::dismissDeleteConfirmation // Dismiss dialog in VM
    )
}