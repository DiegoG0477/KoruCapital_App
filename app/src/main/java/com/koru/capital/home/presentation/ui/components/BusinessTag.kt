package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily

@Composable
fun BusinessTag(
    icon: ImageVector,
    text: String? = null,
    contentDescription: String? = null
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0XFFFFF4C4),
                shape = RoundedCornerShape(35.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            icon,
            modifier = Modifier.height(16.dp),
            contentDescription = contentDescription ?: text,
            tint = Color(0XFF993C07)
        )
        text?.let {
            Text(
                text = it,
                color = Color(0XFF993C07),
                fontSize = 12.sp,
                fontFamily = funnelSansFamily
            )
        }
    }
}