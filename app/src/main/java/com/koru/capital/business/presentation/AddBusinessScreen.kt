package com.koru.capital.business.presentation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.FileText
import com.composables.icons.lucide.Image
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Store
import com.composables.icons.lucide.Tags
import com.composables.icons.lucide.TrendingUp
import com.koru.capital.core.ui.funnelSansFamily

@Preview(showBackground = true)
@Composable
fun AddBusinessScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AddBusinessTop(onBackClick = onBackClick)
        AddBusinessForm()
    }
}

@Composable
private fun AddBusinessTop(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                Lucide.ArrowLeft,
                contentDescription = "Regresar",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = "Agregar Negocio",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0XFFCC5500),
            fontFamily = funnelSansFamily
        )
    }
}

@Composable
private fun AddBusinessForm() {
    var businessName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }
    var profit by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var businessModel by remember { mutableStateOf("") }
    var monthlyIncome by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Imagen del Negocio
        FormSection(
            title = "Imagen del Negocio",
            icon = Lucide.Image
        ) {
            ImageUploadBox(
                onImageSelected = { /* Handle image selection */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre del Negocio
        FormSection(
            title = "Nombre del Negocio",
            icon = Lucide.Store
        ) {
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                placeholder = { Text("Ej. FreshMex") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0XFFCC5500),
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Descripción
        FormSection(
            title = "Descripción",
            icon = Lucide.FileText
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Breve descripción de tu negocio y sus ventajas competitivas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0XFFCC5500),
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Inversión y Ganancia
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(
                    title = "Inversión (MXN)",
                    icon = Lucide.DollarSign
                ) {
                    OutlinedTextField(
                        value = investment,
                        onValueChange = { investment = it },
                        placeholder = { Text("Ej. 500000") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFFCC5500),
                            unfocusedBorderColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                FormSection(
                    title = "Ganancia (%)",
                    icon = Lucide.TrendingUp
                ) {
                    OutlinedTextField(
                        value = profit,
                        onValueChange = { profit = it },
                        placeholder = { Text("Ej. 35") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0XFFCC5500),
                            unfocusedBorderColor = Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Categoría
        FormSection(
            title = "Categoría",
            icon = Lucide.Tags
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = { },
                placeholder = { Text("Selecciona categoría") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0XFFCC5500),
                    unfocusedBorderColor = Color.Gray
                ),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Lucide.ChevronDown,
                        contentDescription = "Seleccionar categoría",
                        tint = Color.Gray
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Modelo de Negocio
        FormSection(
            title = "Modelo de Negocio",
            icon = Lucide.Star
        ) {
            OutlinedTextField(
                value = businessModel,
                onValueChange = { businessModel = it },
                placeholder = { Text("Describe brevemente cómo genera ingresos el negocio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0XFFCC5500),
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ingresos Mensuales
        FormSection(
            title = "Ingresos Mensuales (MXN)",
            icon = Lucide.DollarSign
        ) {
            OutlinedTextField(
                value = monthlyIncome,
                onValueChange = { monthlyIncome = it },
                placeholder = { Text("Ej. 120000") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0XFFCC5500),
                    unfocusedBorderColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Publicar
        Button(
            onClick = { /* Handle publish */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFFCC5500)
            ),
            shape = RoundedCornerShape(35.dp)
        ) {
            Text(
                text = "Publicar Negocio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = funnelSansFamily
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FormSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0XFFCC5500),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontFamily = funnelSansFamily
            )
        }
        content()
    }
}

@Composable
private fun ImageUploadBox(
    onImageSelected: (Uri) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0XFFF5F5F5))
            .clickable { /* Handle click */ },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Lucide.Image,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0XFFCC5500)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Haz clic para subir una imagen",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = funnelSansFamily
            )
            Text(
                text = "Recomendado: 1200 x 800px",
                color = Color.Gray,
                fontSize = 12.sp,
                fontFamily = funnelSansFamily
            )
        }
    }
}