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
    onEditBusiness: (String) -> Unit,
    viewModel: MyBusinessesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                is MyBusinessesNavigationEvent.NavigateToEditBusiness -> {
                    onEditBusiness(event.businessId)
                }
                is MyBusinessesNavigationEvent.NavigateToAddBusiness -> {
                    onAddBusiness()
                }
            }
        }
    }

    MyBusinessesContent(
        uiState = uiState,
        onAddBusiness = viewModel::onAddBusinessClick,
        onFilterSelected = viewModel::onFilterSelected,
        onEditBusiness = viewModel::onEditBusinessClick,
        onDeleteRequest = viewModel::onDeleteBusinessRequest,
        onConfirmDelete = viewModel::onConfirmDelete,
        onDismissDelete = viewModel::dismissDeleteConfirmation
    )
}