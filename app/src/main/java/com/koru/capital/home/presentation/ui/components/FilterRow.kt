package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Grid2x2Check
import com.composables.icons.lucide.ListFilter
import com.composables.icons.lucide.Lucide

sealed class FilterItem(
    val icon: ImageVector,
    val label: String,
    val action: () -> Unit
) {
    object NearMe : FilterItem(
        icon = Lucide.Compass,
        label = "Cerca de mí",
        action = { /* Acción cuando se selecciona */ }
    )

    object LessThan50K : FilterItem(
        icon = Lucide.DollarSign,
        label = "0 - 50k",
        action = { /* Acción cuando se selecciona */ }
    )

    object Category : FilterItem(
        icon = Lucide.Grid2x2Check,
        label = "Categoría",
        action = { /* Acción cuando se selecciona */ }
    )

    object FilterBtn : FilterItem(
        icon = Lucide.ListFilter,
        label = "",
        action = { /* Acción cuando se selecciona */ }
    )
}

@Composable
fun FilterRow(filters: List<FilterItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEach { filter ->
            FilterButton(filter)
        }
    }
}