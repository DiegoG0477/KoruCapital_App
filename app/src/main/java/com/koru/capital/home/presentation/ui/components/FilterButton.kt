package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.theme.* // Import theme colors

@Composable
fun FilterButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean, // Determines visual state
    modifier: Modifier = Modifier
) {
    // Determine colors based on active state using theme colors
    val containerColor = if (isActive) KoruOrange else KoruWhite
    val contentColor = if (isActive) KoruWhite else KoruBlack
    val borderColor = if (isActive) KoruOrange else KoruDarkGray

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(34.dp),
        modifier = modifier
            // .height(36.dp) // Allow content height or set fixed height
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(34.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = icon,
                contentDescription = null, // Text provides context
                tint = contentColor // Use dynamic content color
            )
            // Only show text if label is not empty (for the filter icon button)
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = contentColor // Use dynamic content color
                )
            }
        }
    }
}