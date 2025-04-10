package com.koru.capital.auth.presentation.login.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.KoruBlack
import com.koru.capital.core.ui.theme.KoruOrangeAlternative
import com.koru.capital.core.ui.theme.KoruWhite

@Composable
fun LoginBottom(onNavigateToRegister: () -> Unit) { // Add lambda
    // Use standard Surface/Row for bottom content, not BottomAppBar if Scaffold handles nav
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = KoruWhite // Or transparent if background should show through
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), // Adjust padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = KoruBlack, // Use theme colors
                fontFamily = funnelSansFamily
            )
            Text(
                text = "Regístrate aquí",
                color = KoruOrangeAlternative,
                fontFamily = funnelSansFamily,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = onNavigateToRegister) // Use lambda
            )
        }
    }
}