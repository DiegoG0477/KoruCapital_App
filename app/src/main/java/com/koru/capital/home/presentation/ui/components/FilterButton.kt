package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterButton(filter: FilterItem) {
    Button(
        onClick = { filter.action() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(34.dp),
        modifier = Modifier
            .height(36.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(34.dp)
            )
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(34.dp),
                spotColor = Color.Transparent,
                ambientColor = Color.Transparent
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.height(14.dp),
                imageVector = filter.icon,
                contentDescription = filter.label,
                tint = Color.Black
            )
            Text(
                text = filter.label,
                fontSize = 12.sp
            )
        }
    }
}