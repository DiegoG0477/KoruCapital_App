package com.koru.capital.register.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.* // Import theme colors

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(
                fontSize = 14.sp,
                color = KoruDarkGray // Use theme color
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors( // Use consistent form colors
                focusedContainerColor = KoruInputBackground,
                unfocusedContainerColor = KoruInputBackground,
                disabledContainerColor = KoruInputBackground.copy(alpha = 0.5f),
                errorContainerColor = KoruInputBackground,
                focusedIndicatorColor = KoruOrange, // Or KoruOrangeAlternative if preferred for register
                unfocusedIndicatorColor = KoruTransparent,
                disabledIndicatorColor = KoruTransparent,
                errorIndicatorColor = KoruRed,
                focusedTextColor = KoruBlack,
                unfocusedTextColor = KoruBlack,
                disabledTextColor = KoruDarkGray.copy(alpha = 0.7f),
                errorTextColor = KoruBlack,
                focusedLeadingIconColor = KoruDarkGray,
                unfocusedLeadingIconColor = KoruDarkGray,
                disabledLeadingIconColor = KoruDarkGray.copy(alpha = 0.5f),
                errorLeadingIconColor = KoruDarkGray,
                focusedTrailingIconColor = KoruDarkGray,
                unfocusedTrailingIconColor = KoruDarkGray,
                disabledTrailingIconColor = KoruDarkGray.copy(alpha = 0.5f),
                errorTrailingIconColor = KoruRed
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = KoruRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp) // Indent error message slightly
            )
        }
    }
}