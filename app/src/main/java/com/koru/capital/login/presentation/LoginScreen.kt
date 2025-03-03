package com.koru.capital.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.funnelSansFamily

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Scaffold(
        bottomBar = { LoginBottom() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoginTop(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            LoginBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
        }
    }
}

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = funnelSansFamily
            ),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color(0xFFEEEEEE),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 8.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun LoginTop(modifier: Modifier){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 40.dp))
            .clip(RoundedCornerShape(bottomEnd = 40.dp))
            .background(
                Color(0xFFF97316)
            )
    ){
        Text(
            modifier = Modifier.padding(start = 40.dp, top = 40.dp),
            text = "MexiCrowd",
            color = Color.White,
            fontWeight = FontWeight(700),
            fontSize = 23.sp
        )

        Text(
            modifier = Modifier.padding(start = 40.dp, top = 16.dp),
            text = "Invierte en tu comunidad, crece con ella",
            color = Color.White,
            fontWeight = FontWeight(400),
            fontSize = 18.sp
        )
    }
}

@Composable
fun LoginBody(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LabeledTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = "Contraseña",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF97316), // Naranja
                contentColor = Color.White
            )
        ) {
            Text(text = "Iniciar sesión")
        }
    }
}

@Composable
fun LoginBottom() {
    BottomAppBar(
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes una cuenta? ",
                color = Color.Black
            )

            Text(
                text = "Regístrate aquí",
                color = Color(0xFFF97316)
            )
        }
    }
}