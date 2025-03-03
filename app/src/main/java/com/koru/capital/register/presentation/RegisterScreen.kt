package com.koru.capital.register.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Icon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.Lock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.koru.capital.core.ui.funnelSansFamily

@Preview(showBackground = true)
@Composable
fun RegisterScreen() {
    Scaffold(
        bottomBar = { RegisterBottom() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RegisterTop()
            Spacer(modifier = Modifier.height(25.dp))
            RegisterBody()
        }
    }
}

@Composable
fun RegisterTop(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            modifier = Modifier.padding(top = 60.dp).align(Alignment.CenterHorizontally),
            text = "Crear una Cuenta",
            color = Color.Black,
            fontFamily = funnelSansFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 75.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "¿Ya tienes una cuenta?",
                color = Color.Black,
                fontFamily = funnelSansFamily
            )

            Text(
                text = "Inicia Sesión",
                color = Color(0xFFF97316),
                fontFamily = funnelSansFamily
            )
        }
    }
}

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    treadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            trailingIcon = treadingIcon,
            isError = isError,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF2F2F2), // Fondo gris claro
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledContainerColor = Color(0xFFF2F2F2),
                errorContainerColor = Color(0xFFF2F2F2),
                focusedIndicatorColor = Color(0xFFF97316), // Borde naranja cuando está activo
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Red,
                focusedTextColor = Color.Black
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}


@Composable
fun RegisterBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }

        LabeledTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = "Correo electrónico",
            leadingIcon = {
                Icon(
                    Lucide.Mail,
                    contentDescription = "Correo",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LabeledTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = "Contraseña",
            leadingIcon = {
                Icon(
                    Lucide.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.Gray
                )
            },
            isError = password.value.isNotEmpty() && password.value.length < 8,
            treadingIcon = {
                Icon(
                    Lucide.Eye,
                    contentDescription = "Ver Contraseña",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316), // Naranja
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(15)
        ) {
            Text(
                text = "Continuar",
                fontFamily = funnelSansFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RegisterBottom() {
    BottomAppBar(
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Al hacer click en continuar usted reconoce y está de acuerdo con nuestras ")
                    withStyle(style = SpanStyle(color = Color(0xFFF97316))) {
                        append("políticas de privacidad")
                    }
                    append(" y nuestros ")
                    withStyle(style = SpanStyle(color = Color(0xFFF97316))) {
                        append("términos de uso")
                    }
                },

                fontFamily = funnelSansFamily,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}
