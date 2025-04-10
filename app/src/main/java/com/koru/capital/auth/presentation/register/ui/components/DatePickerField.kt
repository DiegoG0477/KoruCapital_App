package com.koru.capital.register.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CalendarDays // More specific icon
import com.composables.icons.lucide.Lucide
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@Composable
fun DatePickerField(
    value: String, // Display formatted date string
    label: String,
    onFieldClick: () -> Unit, // Trigger ViewModel action to show picker
    modifier: Modifier = Modifier,
    isError: Boolean = false, // Optional error state
    errorMessage: String? = null
) {

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(fontSize = 14.sp, color = KoruDarkGray),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        // Use Box with clickable to overlay the TextField
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onFieldClick) // Click triggers VM action
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {}, // No direct changes here
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = false, // Visually disabled, but Box handles click
                readOnly = true,
                placeholder = { Text("DD/MM/AAAA") }, // Added placeholder
                isError = isError,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors( // Consistent styling
                    disabledContainerColor = KoruInputBackground,
                    disabledTextColor = if (value.isEmpty()) KoruDarkGray else KoruBlack, // Show placeholder color if empty
                    disabledIndicatorColor = if(isError) KoruRed else KoruTransparent,
                    disabledLeadingIconColor = KoruDarkGray,
                    disabledTrailingIconColor = KoruDarkGray,
                    errorIndicatorColor = KoruRed // Ensure error border shows
                ),
                trailingIcon = {
                    Icon(
                        Lucide.CalendarDays,
                        contentDescription = "Seleccionar fecha",
                        tint = KoruDarkGray
                    )
                }
            )
        }
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = KoruRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}