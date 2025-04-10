package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.KoruDarkOrange
import com.koru.capital.core.ui.theme.KoruLightYellow


@Composable
fun BusinessTag(
    icon: ImageVector,
    text: String, // Text is now mandatory for this version
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = KoruLightYellow, // Use theme color
                shape = RoundedCornerShape(35.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp) // Reduced space
    ) {
        Icon(
            icon,
            modifier = Modifier.size(14.dp), // Slightly smaller icon
            contentDescription = null, // Content description handled by text
            tint = KoruDarkOrange // Use theme color
        )
        Text(
            text = text,
            color = KoruDarkOrange, // Use theme color
            fontSize = 11.sp, // Slightly smaller text
            fontFamily = funnelSansFamily,
            maxLines = 1 // Ensure tag text doesn't wrap excessively
        )
    }
}