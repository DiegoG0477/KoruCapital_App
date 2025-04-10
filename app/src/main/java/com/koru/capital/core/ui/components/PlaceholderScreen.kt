package com.koru.capital.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PlaceholderScreen(screenName: String, modifier: Modifier = Modifier, onNavigateExample: (() -> Unit)? = null) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$screenName Screen",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "(Contenido pendiente de implementación)",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            if(onNavigateExample != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigateExample) {
                    Text("Ejemplo Navegación")
                }
            }
        }
    }
}