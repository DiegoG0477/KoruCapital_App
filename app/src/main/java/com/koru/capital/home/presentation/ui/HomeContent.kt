package com.koru.capital.home.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState // Import state for LazyColumn
import androidx.compose.material3.* // Import Material 3 components
import androidx.compose.runtime.* // Import derivedStateOf, LaunchedEffect, remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings2
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.home.presentation.ui.components.BusinessCard
import com.koru.capital.home.presentation.ui.components.FilterRow
import com.koru.capital.home.presentation.ui.components.FilterType
import com.koru.capital.home.presentation.viewmodel.HomeUiState
import com.koru.capital.core.ui.theme.* // Import theme colors

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onFilterClick: (FilterType) -> Unit,
    onSaveClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onCardClick: (String) -> Unit, // For navigating to details
    onSettingsClick: () -> Unit,
    onLoadMore: () -> Unit, // Callback for loading more data
    modifier: Modifier = Modifier
) {
    // Remember the state for the LazyColumn to observe scroll position
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // --- Top Section (Header and Filters) ---
        HomeTop(onSettingsClick = onSettingsClick)
        FilterRow(
            activeFilters = uiState.activeFilters,
            onFilterClick = onFilterClick
        )
        Spacer(modifier = Modifier.height(16.dp)) // Spacing before the list/content area

        // --- Main Content Area (List, Loaders, Errors, Empty State) ---
        Box(
            modifier = Modifier
                .weight(1f) // Takes remaining space
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter // Align content to the top
        ) {
            // Determine what to show based on the UI state
            when {
                // Case 1: Initial Loading (Show full screen loader only if list is currently empty)
                uiState.isLoadingInitial && uiState.businesses.isEmpty() -> {
                    CircularProgressIndicator(
                        color = KoruOrange,
                        modifier = Modifier.align(Alignment.Center) // Center the initial loader
                    )
                }

                // Case 2: Error State (Show error only if the list is empty)
                uiState.errorMessage != null && uiState.businesses.isEmpty() -> {
                    Text(
                        text = uiState.errorMessage, // Display the error message from state
                        color = KoruRed,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }

                // Case 3: Empty State (List is empty after loading, no errors)
                uiState.businesses.isEmpty() && !uiState.isLoadingInitial && !uiState.isLoadingMore -> {
                    Text(
                        text = "No se encontraron oportunidades.", // Generic empty message
                        color = KoruDarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }

                // Case 4: Display the list of businesses
                else -> {
                    LazyColumn(
                        state = listState, // Attach the LazyListState
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp) // Space between business cards
                    ) {
                        // --- Business Items ---
                        items(uiState.businesses, key = { it.id }) { business ->
                            BusinessCard(
                                business = business,
                                onSaveClick = { onSaveClick(business.id) },
                                onLikeClick = { onLikeClick(business.id) },
                                onCardClick = { onCardClick(business.id) } // Trigger navigation
                            )
                        }

                        // --- Loading More / End Reached Indicator ---
                        item {
                            // Show loading indicator at the bottom if currently loading more pages
                            if (uiState.isLoadingMore) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp), // Padding around the loader
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = KoruOrange)
                                }
                            }
                            // Optionally, show a message when the end of the list is reached
                            else if (uiState.endReached && uiState.businesses.isNotEmpty()) {
                                Text(
                                    text = "Has llegado al final",
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = KoruDarkGray,
                                    style = MaterialTheme.typography.bodySmall // Use a smaller style
                                )
                            }
                        }
                    } // End LazyColumn

                    // --- Scroll Detection Logic ---
                    // Calculate if we should trigger loading more items
                    val shouldLoadMore = remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            // Check if layoutInfo is available and list is not empty
                            if (layoutInfo.visibleItemsInfo.isEmpty() || layoutInfo.totalItemsCount == 0) {
                                false
                            } else {
                                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.last().index
                                val totalItems = layoutInfo.totalItemsCount
                                // Define the threshold (e.g., load when 3 items from the end are visible)
                                val threshold = 3
                                // Trigger condition:
                                !uiState.isLoadingMore && // Not already loading
                                        !uiState.endReached &&    // Haven't reached the end
                                        lastVisibleItemIndex >= totalItems - 1 - threshold // Check threshold (adjust index by -1 for item count)
                            }
                        }
                    }

                    // Trigger the onLoadMore callback when shouldLoadMore becomes true
                    LaunchedEffect(shouldLoadMore.value) {
                        if (shouldLoadMore.value) {
                            onLoadMore()
                        }
                    }
                } // End of else (list display)
            } // End of when
        } // End of Box
    } // End of Column (main layout)
}

// --- Reusable Top Bar Component --- (Keep as is)
@Composable
fun HomeTop(onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(top = 23.dp, bottom = 8.dp), // Adjust padding
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Exploración",
                color = KoruBlack,
                fontFamily = funnelSansFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
            )
            IconButton(onClick = onSettingsClick, modifier = Modifier.size(30.dp)) {
                Icon(
                    Lucide.Settings2,
                    contentDescription = "Configuración",
                    tint = KoruBlack
                )
            }
        }
    }
}