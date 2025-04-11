package com.koru.capital.home.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.koru.capital.core.ui.theme.*

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onFilterClick: (FilterType) -> Unit,
    onSaveClick: (String) -> Unit,
    onLikeClick: (String) -> Unit,
    onCardClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HomeTop(onSettingsClick = onSettingsClick)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                uiState.isLoadingInitial && uiState.businesses.isEmpty() -> {
                    CircularProgressIndicator(
                        color = KoruOrange,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.errorMessage != null && uiState.businesses.isEmpty() -> {
                    Text(
                        text = uiState.errorMessage,
                        color = KoruRed,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }

                uiState.businesses.isEmpty() && !uiState.isLoadingInitial && !uiState.isLoadingMore -> {
                    Text(
                        text = "No se encontraron oportunidades.",
                        color = KoruDarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(uiState.businesses, key = { it.id }) { business ->
                            BusinessCard(
                                business = business,
                                onSaveClick = { onSaveClick(business.id) },
                                onLikeClick = { onLikeClick(business.id) },
                                onCardClick = { onCardClick(business.id) }
                            )
                        }

                        item {
                            if (uiState.isLoadingMore) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = KoruOrange)
                                }
                            }
                            else if (uiState.endReached && uiState.businesses.isNotEmpty()) {
                                Text(
                                    text = "Has llegado al final",
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = KoruDarkGray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    val shouldLoadMore = remember {
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            if (layoutInfo.visibleItemsInfo.isEmpty() || layoutInfo.totalItemsCount == 0) {
                                false
                            } else {
                                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.last().index
                                val totalItems = layoutInfo.totalItemsCount
                                val threshold = 3
                                !uiState.isLoadingMore &&
                                        !uiState.endReached &&
                                        lastVisibleItemIndex >= totalItems - 1 - threshold
                            }
                        }
                    }

                    LaunchedEffect(shouldLoadMore.value) {
                        if (shouldLoadMore.value) {
                            onLoadMore()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTop(onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .padding(top = 23.dp, bottom = 8.dp),
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