package com.koru.capital.auth.presentation.login.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.KoruOrangeAlternative
import com.koru.capital.core.ui.theme.KoruWhite

@Composable
fun LoginTop(modifier: Modifier = Modifier) { // Pass modifier
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)) // Combine clips
            .background(KoruOrangeAlternative) // Use theme color
            .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 20.dp) // Add vertical padding
            .fillMaxWidth() // Ensure it takes full width
    ) {
        Text(
            text = "KoruCapital",
            color = KoruWhite, // Use theme color
            fontWeight = FontWeight.Bold, // Bolder
            fontSize = 23.sp,
            fontFamily = funnelSansFamily // Use custom font
        )
        Spacer(modifier = Modifier.height(8.dp)) // Reduced spacer
        Text(
            text = "Invierte en tu comunidad, crece con ella",
            color = KoruWhite,
            fontWeight = FontWeight.Normal, // Normal weight might be better
            fontSize = 18.sp,
            fontFamily = funnelSansFamily // Use custom font
        )
    }
}
