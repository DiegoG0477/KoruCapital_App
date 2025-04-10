package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Grid2x2Check
import com.composables.icons.lucide.ListFilter
import com.composables.icons.lucide.Lucide

// Enum definition remains the same
enum class FilterType(val icon: ImageVector, val label: String) {
    NEAR_ME(Lucide.Compass, "Cerca de mí"),
    LESS_THAN_50K(Lucide.DollarSign, "0 - 50k"),
    CATEGORY(Lucide.Grid2x2Check, "Categoría"),
    MORE_FILTERS(Lucide.ListFilter, "") // Button for more complex filters
}

@Composable
fun FilterRow(
    activeFilters: Set<FilterType>, // Current active filters from ViewModel state
    onFilterClick: (FilterType) -> Unit, // Callback to ViewModel when a filter is clicked
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()) // Allow scrolling if filters overflow
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between buttons
    ) {
        // Iterate through all defined filter types
        FilterType.values().forEach { filterType ->
            FilterButton(
                text = filterType.label,
                icon = filterType.icon,
                onClick = { onFilterClick(filterType) }, // Call the ViewModel callback
                isActive = activeFilters.contains(filterType) // Check if this filter is in the active set
            )
        }
    }
}