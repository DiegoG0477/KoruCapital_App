package com.koru.capital.auth.presentation.login.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@Composable
fun AuthLabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPasswordToggleVisible: Boolean = false, // Add parameter for visibility toggle if needed
    onPasswordToggleClick: (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    keyboardActions: androidx.compose.foundation.text.KeyboardActions = androidx.compose.foundation.text.KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = KoruDarkGray, // Use theme color
                fontFamily = funnelSansFamily
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        OutlinedTextField( // Using OutlinedTextField for consistency with other forms
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), // Consistent shape
            singleLine = true,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            trailingIcon = if (isPasswordToggleVisible && onPasswordToggleClick != null) { // Only show if needed
                {
                    // Add Eye icon logic here similar to Register password fields
                    // IconButton(onClick = onPasswordToggleClick) { Icon(...) }
                }
            } else null,
            colors = TextFieldDefaults.colors( // Consistent styling
                focusedContainerColor = KoruInputBackground,
                unfocusedContainerColor = KoruInputBackground,
                disabledContainerColor = KoruDisabledInput, // Specific disabled color?
                errorContainerColor = KoruInputBackground,
                focusedIndicatorColor = KoruOrangeAlternative, // Login uses alternative orange?
                unfocusedIndicatorColor = KoruTransparent,
                disabledIndicatorColor = KoruTransparent,
                errorIndicatorColor = KoruRed,
                focusedTextColor = KoruBlack,
                unfocusedTextColor = KoruBlack,
                disabledTextColor = KoruDarkGray.copy(alpha = 0.7f),
                errorTextColor = KoruBlack,
                // Add placeholder/icon colors if needed
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = KoruRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

