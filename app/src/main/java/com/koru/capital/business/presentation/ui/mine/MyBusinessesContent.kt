package com.koru.capital.business.presentation.ui.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.* // Keep lucide icons if used
import com.koru.capital.R // Assuming placeholder image
import com.koru.capital.business.presentation.viewmodel.BusinessFilter
import com.koru.capital.business.presentation.viewmodel.BusinessListItemUiModel
import com.koru.capital.business.presentation.viewmodel.MyBusinessesUiState
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@Composable
fun MyBusinessesContent(
    uiState: MyBusinessesUiState,
    onAddBusiness: () -> Unit,
    onFilterSelected: (BusinessFilter) -> Unit,
    onEditBusiness: (String) -> Unit, // Callback for edit action
    onDeleteRequest: (BusinessListItemUiModel) -> Unit, // Callback to trigger delete confirmation
    onConfirmDelete: () -> Unit, // Callback for confirming deletion
    onDismissDelete: () -> Unit, // Callback to dismiss deletion dialog
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        MyBusinessesTop() // Reusable Top Bar/Header

        // Filter Tabs
        FilterSelector(
            selectedFilter = uiState.selectedFilter,
            onFilterSelected = onFilterSelected
        )

        Spacer(modifier = Modifier.height(8.dp)) // Reduced space after tabs

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter // Align list/loading/error to top
        ) {
            when {
                uiState.isLoading && uiState.businesses.isEmpty() -> { // Show full screen loader only if list is empty initially
                    CircularProgressIndicator(
                        color = KoruOrange,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = KoruRed,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }
                uiState.businesses.isEmpty() -> { // Handle empty state after loading
                    EmptyBusinessState(onAddBusinessClick = onAddBusiness)
                }
                else -> {
                    // Display the list of businesses
                    BusinessList(
                        businesses = uiState.businesses,
                        isLoading = uiState.isLoading, // Pass loading state for potential item overlay
                        onEdit = onEditBusiness,
                        onDelete = onDeleteRequest // Trigger delete request
                    )
                }
            }
        }

        // Delete Confirmation Dialog (Conditionally shown)
        if (uiState.showDeleteConfirmation) {
            DeleteConfirmationDialog(
                businessName = uiState.businessToDelete?.name ?: "este negocio",
                onConfirm = onConfirmDelete,
                onDismiss = onDismissDelete
            )
        }
    }
}

@Composable
fun MyBusinessesTop() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .padding(top = 16.dp, bottom = 8.dp) // Adjusted padding
    ) {
        Text(
            text = "Mis Negocios",
            color = KoruBlack,
            fontFamily = funnelSansFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 23.sp,
        )
    }
}

@Composable
fun FilterSelector(
    selectedFilter: BusinessFilter,
    onFilterSelected: (BusinessFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedFilter.ordinal,
        modifier = modifier.fillMaxWidth(),
        containerColor = KoruWhite, // Background color for the tab row
        contentColor = KoruOrange, // Color for the selected tab indicator and text
        indicator = { tabPositions ->
            if (selectedFilter.ordinal < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedFilter.ordinal]),
                    height = 3.dp,
                    color = KoruOrange
                )
            }
        }
    ) {
        BusinessFilter.values().forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = {
                    Text(
                        text = filter.title,
                        fontFamily = funnelSansFamily,
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selectedContentColor = KoruOrange,
                unselectedContentColor = KoruDarkGray
            )
        }
    }
}


@Composable
fun BusinessList(
    businesses: List<BusinessListItemUiModel>,
    isLoading: Boolean, // Can be used to show loading state on items
    onEdit: (String) -> Unit,
    onDelete: (BusinessListItemUiModel) -> Unit, // Pass full item for context
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(businesses, key = { it.id }) { business ->
            BusinessListItem(
                business = business,
                onEdit = { onEdit(business.id) },
                onDelete = { onDelete(business) } // Pass the business object
            )
        }
        // Optional: Show a loading item at the end if paginating later
        /*
        if (isLoading && businesses.isNotEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = KoruOrange)
                }
            }
        }
        */
    }
}

@Composable
fun BusinessListItem(
    business: BusinessListItemUiModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = KoruWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 4.dp), // Reduced end padding for icons
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image and Text content
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(business.imageUrl ?: R.drawable.sample_business) // Use placeholder
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.sample_business),
                error = painterResource(R.drawable.sample_business),
                contentDescription = business.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = business.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = funnelSansFamily
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    business.category?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = KoruDarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = funnelSansFamily
                        )
                    }
                    if (business.category != null && business.location != null) {
                        Text("•", fontSize = 12.sp, color = KoruDarkGray) // Separator
                    }
                    business.location?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            color = KoruDarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = funnelSansFamily
                        )
                    }
                }
            }

            // Action Icons (Edit/Delete - only if owned)
            if (business.isOwned) {
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Edit, // Use Material icon
                            contentDescription = "Editar",
                            tint = KoruDarkGray
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Delete, // Use Material icon
                            contentDescription = "Eliminar",
                            tint = KoruRed // Use red for delete
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyBusinessState(onAddBusinessClick: () -> Unit) {
    // Keep existing EmptyBusinessState composable, ensure it uses theme colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp), // Add vertical padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center vertically too
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(color = KoruLightYellow, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Lucide.Store, // Changed icon to represent business
                contentDescription = null,
                modifier = Modifier.size(45.dp),
                tint = KoruOrange
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No hay negocios aquí", // More generic message
            fontSize = 22.sp, // Slightly smaller
            fontWeight = FontWeight.Bold,
            color = KoruOrange,
            fontFamily = funnelSansFamily
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Parece que aún no tienes negocios en esta sección. ¡Explora o agrega uno nuevo!", // Adjusted text
            fontSize = 16.sp,
            color = KoruDarkGray,
            textAlign = TextAlign.Center,
            fontFamily = funnelSansFamily
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddBusinessClick,
            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
            shape = RoundedCornerShape(35.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Icon(Lucide.Plus, contentDescription = null, modifier = Modifier.size(20.dp), tint = KoruWhite)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Negocio", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = funnelSansFamily, color = KoruWhite)
        }
    }
}

// --- Delete Confirmation Dialog --- (Point g is partially done here)
@Composable
fun DeleteConfirmationDialog(
    businessName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Eliminación", fontWeight = FontWeight.Bold, fontFamily = funnelSansFamily) },
        text = { Text("¿Estás seguro de que quieres eliminar '$businessName'? Esta acción no se puede deshacer.", fontFamily = funnelSansFamily) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = KoruRed) // Destructive action color
            ) {
                Text("Eliminar", color = KoruWhite, fontFamily = funnelSansFamily)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KoruGrayBackground)
            ) {
                Text("Cancelar", color = KoruDarkGray, fontFamily = funnelSansFamily)
            }
        },
        containerColor = KoruWhite // Dialog background
    )
}