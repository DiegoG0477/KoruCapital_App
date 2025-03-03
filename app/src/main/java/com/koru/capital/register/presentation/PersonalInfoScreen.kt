package com.koru.capital.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.koru.capital.core.ui.funnelSansFamily
import java.util.*

@Preview(showBackground = true)
@Composable
fun PersonalInfoScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Información Personal",
                fontFamily = funnelSansFamily,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val name = remember { mutableStateOf("") }
            val lastName = remember { mutableStateOf("") }
            val birthDate = remember { mutableStateOf("") }
            val country = remember { mutableStateOf("") }
            val state = remember { mutableStateOf("") }
            val city = remember { mutableStateOf("") }

            LabeledTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = "Nombres",
                modifier = Modifier.fillMaxWidth()
            )

            LabeledTextField(
                value = lastName.value,
                onValueChange = { lastName.value = it },
                label = "Apellidos",
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                value = birthDate.value,
                onValueChange = { birthDate.value = it },
                label = "Fecha de Nacimiento"
            )

            DropdownField(
                value = country.value,
                onValueChange = { country.value = it },
                label = "País",
                options = listOf("México", "Argentina", "España", "Colombia", "Chile")
            )

            DropdownField(
                value = state.value,
                onValueChange = { state.value = it },
                label = "Estado",
                options = listOf("Ciudad de México", "Buenos Aires", "Madrid", "Bogotá", "Santiago")
            )

            DropdownField(
                value = city.value,
                onValueChange = { city.value = it },
                label = "Municipio",
                options = listOf("Benito Juárez", "Palermo", "Chamberí", "Usaquén", "Providencia")
            )

            Button(
                onClick = { /* Acción de registro */ },
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
                    text = "Registrarse",
                    fontFamily = funnelSansFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DatePickerField(value: String, onValueChange: (String) -> Unit, label: String) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(fontSize = 14.sp, color = Color.Gray),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        // Campo clickable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFF2F2F2),
                    disabledTextColor = Color.Black,
                    disabledIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    Icon(
                        Lucide.ChevronDown,
                        contentDescription = "Seleccionar fecha",
                        tint = Color.Gray
                    )
                }
            )
        }

        if (showDialog) {
            val calendar = Calendar.getInstance()
            android.app.DatePickerDialog(
                context,
                { _, year, month, day ->
                    onValueChange("$day/${month + 1}/$year")
                    showDialog = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                setOnDismissListener { showDialog = false }
                show()
            }
        }
    }
}

@Composable
fun DropdownField(value: String, onValueChange: (String) -> Unit, label: String, options: List<String>) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontFamily = funnelSansFamily,
            style = TextStyle(fontSize = 14.sp, color = Color.Gray),
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0f)
                    .height(56.dp),
                enabled = false,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontFamily = funnelSansFamily,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Lucide.ChevronDown,
                    contentDescription = "Expandir opciones",
                    tint = Color.Gray
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontFamily = funnelSansFamily,
                                color = Color.Black
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}