package com.koru.capital.register.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownField(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    optionToString: (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Selecciona...",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(fontSize = 14.sp, color = KoruDarkGray),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = enabled && expanded,
            onExpandedChange = { if (enabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption?.let { optionToString(it) } ?: "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                placeholder = { Text(placeholder) },
                enabled = enabled,
                isError = isError,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = KoruInputBackground,
                    unfocusedContainerColor = KoruInputBackground,
                    disabledContainerColor = KoruInputBackground.copy(alpha = 0.5f),
                    errorContainerColor = KoruInputBackground,
                    focusedIndicatorColor = KoruOrange,
                    unfocusedIndicatorColor = KoruTransparent,
                    disabledIndicatorColor = KoruTransparent,
                    errorIndicatorColor = KoruRed,
                    focusedTextColor = KoruBlack,
                    unfocusedTextColor = KoruBlack,
                    disabledTextColor = KoruDarkGray.copy(alpha = 0.7f),
                    errorTextColor = KoruBlack,
                    focusedTrailingIconColor = KoruDarkGray,
                    unfocusedTrailingIconColor = KoruDarkGray,
                    disabledTrailingIconColor = KoruDarkGray.copy(alpha = 0.5f),
                    errorTrailingIconColor = KoruRed,
                    disabledPlaceholderColor = KoruDarkGray.copy(alpha = 0.5f),
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = enabled && expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = enabled && expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionToString(option),
                                fontFamily = funnelSansFamily,
                                color = KoruBlack
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
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