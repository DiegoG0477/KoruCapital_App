package com.koru.capital.business.presentation.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.icons.lucide.*
import com.koru.capital.business.presentation.viewmodel.AddBusinessUiState
import com.koru.capital.business.presentation.viewmodel.AddBusinessViewModel
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.ui.funnelSansFamily

@Composable
fun AddBusinessScreen(
    onBackClick: () -> Unit,
    viewModel: AddBusinessViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AddBusinessTop(onBackClick = onBackClick)

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0XFFCC5500))
            }
        } else if (uiState.isSuccess) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Lucide.CircleCheck,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0XFFCC5500)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Negocio creado con éxito!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0XFFCC5500),
                        fontFamily = funnelSansFamily
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onBackClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFCC5500)),
                        shape = RoundedCornerShape(35.dp)
                    ) {
                        Text(
                            text = "Volver",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = funnelSansFamily
                        )
                    }
                }
            }
        } else {
            AddBusinessForm(
                uiState = uiState,
                onBusinessNameChanged = viewModel::onBusinessNameChanged,
                onDescriptionChanged = viewModel::onDescriptionChanged,
                onInvestmentChanged = viewModel::onInvestmentChanged,
                onProfitChanged = viewModel::onProfitChanged,
                onCategoryChanged = viewModel::onCategoryChanged,
                onStateChanged = viewModel::onStateChanged,
                onMunicipalityChanged = viewModel::onMunicipalityChanged,
                onBusinessModelChanged = viewModel::onBusinessModelChanged,
                onMonthlyIncomeChanged = viewModel::onMonthlyIncomeChanged,
                onSubmit = { viewModel.submitBusiness() }
            )
        }
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
private fun AddBusinessForm(
    uiState: AddBusinessUiState,
    onBusinessNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onInvestmentChanged: (String) -> Unit,
    onProfitChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onMunicipalityChanged: (String) -> Unit,
    onBusinessModelChanged: (String) -> Unit,
    onMonthlyIncomeChanged: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        // Mensaje de error si existe
        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        // Imagen del Negocio
        FormSection(title = "Imagen del Negocio", icon = Lucide.Image) {
            ImageUploadBox(onImageSelected = { /* Handle image selection */ })
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre del Negocio
        FormSection(title = "Nombre del Negocio", icon = Lucide.Store) {
            OutlinedTextField(
                value = uiState.businessName,
                onValueChange = onBusinessNameChanged,
                placeholder = { Text("Ej. FreshMex") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Descripción
        FormSection(title = "Descripción", icon = Lucide.FileText) {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text("Breve descripción de tu negocio y sus ventajas competitivas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Inversión y Ganancia
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Inversión (MXN)", icon = Lucide.DollarSign) {
                    OutlinedTextField(
                        value = uiState.investment,
                        onValueChange = onInvestmentChanged,
                        placeholder = { Text("Ej. 500000") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Ganancia (%)", icon = Lucide.TrendingUp) {
                    OutlinedTextField(
                        value = uiState.profitPercentage,
                        onValueChange = onProfitChanged,
                        placeholder = { Text("Ej. 35") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Estado y Municipio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Estado", icon = Lucide.MapPin) {
                    StateDropdown(
                        states = uiState.states,
                        selectedStateId = uiState.selectedStateId,
                        onStateSelected = onStateChanged
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Municipio", icon = Lucide.MapPin) {
                    MunicipalityDropdown(
                        municipalities = uiState.municipalities,
                        selectedMunicipalityId = uiState.selectedMunicipalityId,
                        onMunicipalitySelected = onMunicipalityChanged,
                        enabled = uiState.selectedStateId.isNotEmpty()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Categoría
        FormSection(title = "Categoría", icon = Lucide.Tags) {
            CategoryDropdown(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategoryChanged
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Modelo de Negocio
        FormSection(title = "Modelo de Negocio", icon = Lucide.Star) {
            OutlinedTextField(
                value = uiState.businessModel,
                onValueChange = onBusinessModelChanged,
                placeholder = { Text("Describe brevemente cómo genera ingresos el negocio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ingresos Mensuales
        FormSection(title = "Ingresos Mensuales (MXN)", icon = Lucide.DollarSign) {
            OutlinedTextField(
                value = uiState.monthlyIncome,
                onValueChange = onMonthlyIncomeChanged,
                placeholder = { Text("Ej. 120000") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón Publicar
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0XFFCC5500)),
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
private fun StateDropdown(
    states: List<State>,
    selectedStateId: String,
    onStateSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedState = states.find { it.id == selectedStateId }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedState?.name ?: "",
            onValueChange = { },
            readOnly = true,
            placeholder = { Text("Selecciona un estado") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Icon(
                    Lucide.ChevronDown,
                    contentDescription = "Expandir",
                    tint = Color.Gray
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            states.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state.name) },
                    onClick = {
                        onStateSelected(state.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun MunicipalityDropdown(
    municipalities: List<Municipality>,
    selectedMunicipalityId: String,
    onMunicipalitySelected: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMunicipality = municipalities.find { it.id == selectedMunicipalityId }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedMunicipality?.name ?: "",
            onValueChange = { },
            readOnly = true,
            enabled = enabled,
            placeholder = {
                Text(
                    if (enabled) "Selecciona un municipio"
                    else "Selecciona primero un estado"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { expanded = true },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Icon(
                    Lucide.ChevronDown,
                    contentDescription = "Expandir",
                    tint = if (enabled) Color.Gray else Color.LightGray
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            municipalities.forEach { municipality ->
                DropdownMenuItem(
                    text = { Text(municipality.name) },
                    onClick = {
                        onMunicipalitySelected(municipality.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = { },
            readOnly = true,
            placeholder = { Text("Selecciona una categoría") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = {
                Icon(
                    Lucide.ChevronDown,
                    contentDescription = "Expandir",
                    tint = Color.Gray
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        onCategorySelected(category.id)
                        expanded = false
                    }
                )
            }
        }
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