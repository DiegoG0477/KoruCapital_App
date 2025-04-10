package com.koru.capital.auth.presentation.register.ui.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.*
import com.koru.capital.auth.presentation.register.viewmodel.RegisterStep
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.register.presentation.components.LabeledTextField
import com.koru.capital.auth.presentation.register.viewmodel.RegisterUiState
import com.koru.capital.core.ui.theme.*

@Composable
fun RegisterEmailPasswordContent(
    uiState: RegisterUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onContinueClick: () -> Unit,
    onLoginClick: () -> Unit, // Callback for "Inicia Sesión" click
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header ---
        Spacer(modifier = Modifier.height(32.dp)) // Add space at the top
        Text(
            text = "Crear una Cuenta",
            color = KoruBlack,
            fontFamily = funnelSansFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 20.dp), // Adjust padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Center horizontally
        ) {
            Text(
                text = "¿Ya tienes una cuenta? ",
                color = KoruBlack,
                fontFamily = funnelSansFamily
            )
            Text(
                text = "Inicia Sesión",
                color = KoruOrangeAlternative, // Use theme color
                fontFamily = funnelSansFamily,
                fontWeight = FontWeight.Bold, // Make it bolder
                textDecoration = TextDecoration.Underline, // Add underline
                modifier = Modifier.clickable(onClick = onLoginClick) // Make clickable
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- Form Fields ---
        LabeledTextField(
            value = uiState.email,
            onValueChange = onEmailChanged,
            label = "Correo electrónico",
            leadingIcon = {
                Icon(Lucide.Mail, contentDescription = "Correo", tint = KoruDarkGray)
            },
            isError = !uiState.isEmailValid,
            errorMessage = uiState.emailErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next // Go to next field
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LabeledTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            label = "Contraseña",
            leadingIcon = {
                Icon(Lucide.Lock, contentDescription = "Password Icon", tint = KoruDarkGray)
            },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        if (uiState.isPasswordVisible) Lucide.EyeOff else Lucide.Eye,
                        contentDescription = "Ver Contraseña",
                        tint = KoruDarkGray
                    )
                }
            },
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = !uiState.isPasswordValid,
            errorMessage = uiState.passwordErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LabeledTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            label = "Confirmar contraseña",
            leadingIcon = {
                Icon(Lucide.LockKeyhole, contentDescription = "Confirm Password Icon", tint = KoruDarkGray) // Different icon
            },
            trailingIcon = {
                IconButton(onClick = onToggleConfirmPasswordVisibility) {
                    Icon(
                        if (uiState.isConfirmPasswordVisible) Lucide.EyeOff else Lucide.Eye,
                        contentDescription = "Ver Confirmar Contraseña",
                        tint = KoruDarkGray
                    )
                }
            },
            visualTransformation = if (uiState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = !uiState.isConfirmPasswordValid,
            errorMessage = uiState.confirmPasswordErrorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // Done action for the last field
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus() // Hide keyboard
                    onContinueClick() // Attempt to continue
                }
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // --- General Error Message ---
        if (uiState.registrationError != null && uiState.currentStep == RegisterStep.EMAIL_PASSWORD) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.registrationError,
                color = KoruRed,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }


        // --- Continue Button ---
        Button(
            onClick = {
                focusManager.clearFocus() // Hide keyboard on click
                onContinueClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp) // More space before button
                .height(56.dp), // Standard button height
            colors = ButtonDefaults.buttonColors(
                containerColor = KoruOrangeAlternative,
                contentColor = KoruWhite
            ),
            shape = RoundedCornerShape(12.dp), // Consistent corner shape
            enabled = !uiState.isLoading // Disable button when loading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = KoruWhite,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Continuar",
                    fontFamily = funnelSansFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Push disclaimer to bottom

        // --- Disclaimer ---
        val annotatedString = buildAnnotatedString {
            append("Al hacer click en continuar usted reconoce y está de acuerdo con nuestras ")

            pushStringAnnotation(tag = "policy", annotation = "https://example.com/privacy") // Replace with actual URL
            withStyle(style = SpanStyle(color = KoruOrangeAlternative, textDecoration = TextDecoration.Underline)) {
                append("políticas de privacidad")
            }
            pop()

            append(" y nuestros ")

            pushStringAnnotation(tag = "terms", annotation = "https://example.com/terms") // Replace with actual URL
            withStyle(style = SpanStyle(color = KoruOrangeAlternative, textDecoration = TextDecoration.Underline)) {
                append("términos de uso")
            }
            pop()
            append(".") // Add period
        }

        ClickableText(
            text = annotatedString,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            style = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                color = KoruDarkGray, // Use theme color
                fontFamily = funnelSansFamily,
                fontSize = 12.sp // Slightly smaller disclaimer text
            ),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
                annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            }
        )
    }
}