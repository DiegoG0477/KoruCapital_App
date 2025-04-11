package com.koru.capital.business.presentation.ui.add

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.*
import com.koru.capital.business.presentation.viewmodel.AddBusinessUiState
import com.koru.capital.core.domain.model.Category
import com.koru.capital.core.domain.model.Municipality
import com.koru.capital.core.domain.model.State
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBusinessContent(
    uiState: AddBusinessUiState,
    onBackClick: () -> Unit,
    onBusinessNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onInvestmentChanged: (String) -> Unit,
    onProfitChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onStateSelected: (String) -> Unit,
    onMunicipalitySelected: (String) -> Unit,
    onBusinessModelChanged: (String) -> Unit,
    onMonthlyIncomeChanged: (String) -> Unit,
    onLaunchGallery: () -> Unit,
    onLaunchCamera: () -> Unit,
    onClearImage: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { AddBusinessTopBar(onBackClick = onBackClick) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && !uiState.isSuccess) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = KoruOrange)
                }
            }
            else if (uiState.isSuccess) {
                AddBusinessSuccessState(onDismiss = onBackClick)
            }
            else {
                AddBusinessForm(
                    uiState = uiState,
                    onBusinessNameChanged = onBusinessNameChanged,
                    onDescriptionChanged = onDescriptionChanged,
                    onInvestmentChanged = onInvestmentChanged,
                    onProfitChanged = onProfitChanged,
                    onCategorySelected = onCategorySelected,
                    onStateSelected = onStateSelected,
                    onMunicipalitySelected = onMunicipalitySelected,
                    onBusinessModelChanged = onBusinessModelChanged,
                    onMonthlyIncomeChanged = onMonthlyIncomeChanged,
                    onLaunchGallery = onLaunchGallery,
                    onLaunchCamera = onLaunchCamera,
                    onClearImage = onClearImage,
                    onSubmit = onSubmit
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBusinessTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Agregar Negocio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = KoruOrange,
                fontFamily = funnelSansFamily
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Lucide.ArrowLeft,
                    contentDescription = "Regresar",
                    modifier = Modifier.size(24.dp),
                    tint = KoruOrange
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = KoruWhite)
    )
}

@Composable
private fun AddBusinessForm(
    uiState: AddBusinessUiState,
    onBusinessNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onInvestmentChanged: (String) -> Unit,
    onProfitChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onStateSelected: (String) -> Unit,
    onMunicipalitySelected: (String) -> Unit,
    onBusinessModelChanged: (String) -> Unit,
    onMonthlyIncomeChanged: (String) -> Unit,
    onLaunchGallery: () -> Unit,
    onLaunchCamera: () -> Unit,
    onClearImage: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = KoruRed,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .background(KoruRed.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                textAlign = TextAlign.Center
            )
        }

        FormSection(title = "Imagen del Negocio", icon = Lucide.Image) {
            ImageUploadBox(
                selectedImageUri = uiState.selectedImageUri,
                onLaunchGallery = onLaunchGallery,
                onLaunchCamera = onLaunchCamera,
                onClearImage = onClearImage
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        FormSection(title = "Nombre del Negocio", icon = Lucide.Store) {
            OutlinedTextField(
                value = uiState.businessName,
                onValueChange = onBusinessNameChanged,
                placeholder = { Text("Ej. FreshMex") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                singleLine = true,
                isError = uiState.errorMessage?.contains("nombre", ignoreCase = true) == true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        FormSection(title = "Descripción", icon = Lucide.FileText) {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text("Breve descripción de tu negocio...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                isError = uiState.errorMessage?.contains("descripción", ignoreCase = true) == true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Inversión (MXN)", icon = Lucide.DollarSign) {
                    OutlinedTextField(
                        value = uiState.investment,
                        onValueChange = onInvestmentChanged,
                        placeholder = { Text("Ej. 500000") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = formTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = uiState.errorMessage?.contains("inversión", ignoreCase = true) == true
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
                        colors = formTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        isError = uiState.errorMessage?.contains("ganancia", ignoreCase = true) == true
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Estado", icon = Lucide.MapPin) {
                    StateDropdown(
                        states = uiState.states,
                        selectedStateId = uiState.selectedStateId,
                        onStateSelected = onStateSelected,
                        isLoading = uiState.isLoadingStates,
                        isError = uiState.errorMessage?.contains("estado", ignoreCase = true) == true
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Municipio", icon = Lucide.MapPin) {
                    MunicipalityDropdown(
                        municipalities = uiState.municipalities,
                        selectedMunicipalityId = uiState.selectedMunicipalityId,
                        onMunicipalitySelected = onMunicipalitySelected,
                        enabled = uiState.selectedStateId.isNotEmpty() && !uiState.isLoadingMunicipalities && uiState.municipalities.isNotEmpty(),
                        isLoading = uiState.isLoadingMunicipalities,
                        isError = uiState.errorMessage?.contains("municipio", ignoreCase = true) == true
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        FormSection(title = "Categoría", icon = Lucide.Tags) {
            CategoryDropdown(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategorySelected,
                isLoading = uiState.isLoadingCategories,
                isError = uiState.errorMessage?.contains("categoría", ignoreCase = true) == true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        FormSection(title = "Modelo de Negocio", icon = Lucide.Star) {
            OutlinedTextField(
                value = uiState.businessModel,
                onValueChange = onBusinessModelChanged,
                placeholder = { Text("Describe cómo genera ingresos...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                isError = uiState.errorMessage?.contains("modelo", ignoreCase = true) == true
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        FormSection(title = "Ingresos Mensuales (MXN)", icon = Lucide.DollarSign) {
            OutlinedTextField(
                value = uiState.monthlyIncome,
                onValueChange = onMonthlyIncomeChanged,
                placeholder = { Text("Ej. 120000") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = uiState.errorMessage?.contains("ingreso", ignoreCase = true) == true
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
            shape = RoundedCornerShape(35.dp),
            enabled = !uiState.isLoading && !uiState.isLoadingCategories && !uiState.isLoadingStates && !uiState.isLoadingMunicipalities
        ) {
            Text("Publicar Negocio", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = funnelSansFamily, color = KoruWhite)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StateDropdown(
    states: List<State>,
    selectedStateId: String,
    onStateSelected: (String) -> Unit,
    isLoading: Boolean,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedState = states.find { it.id == selectedStateId }

    ExposedDropdownMenuBox(
        expanded = !isLoading && expanded,
        onExpandedChange = { if (!isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (isLoading) "Cargando..." else selectedState?.name ?: "",
            onValueChange = { },
            readOnly = true,
            enabled = !isLoading,
            placeholder = { Text("Selecciona un estado") },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = !isLoading),
            isError = isError,
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        )
        ExposedDropdownMenu(
            expanded = !isLoading && expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            states.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state.name, fontFamily = funnelSansFamily) },
                    onClick = { onStateSelected(state.id); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MunicipalityDropdown(
    municipalities: List<Municipality>,
    selectedMunicipalityId: String,
    onMunicipalitySelected: (String) -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMunicipality = municipalities.find { it.id == selectedMunicipalityId }

    ExposedDropdownMenuBox(
        expanded = enabled && !isLoading && expanded,
        onExpandedChange = { if (enabled && !isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (isLoading) "Cargando..." else selectedMunicipality?.name ?: "",
            onValueChange = { },
            readOnly = true,
            enabled = enabled && !isLoading,
            placeholder = { Text(if (enabled) "Selecciona municipio" else "Selecciona estado") },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = enabled && !isLoading),
            isError = isError,
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = enabled && expanded)
                }
            }
        )
        ExposedDropdownMenu(
            expanded = enabled && !isLoading && expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            municipalities.forEach { municipality ->
                DropdownMenuItem(
                    text = { Text(municipality.name, fontFamily = funnelSansFamily) },
                    onClick = { onMunicipalitySelected(municipality.id); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    isLoading: Boolean,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    ExposedDropdownMenuBox(
        expanded = !isLoading && expanded,
        onExpandedChange = { if (!isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (isLoading) "Cargando..." else selectedCategory?.name ?: "",
            onValueChange = { },
            readOnly = true,
            enabled = !isLoading,
            placeholder = { Text("Selecciona una categoría") },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = !isLoading),
            isError = isError,
            trailingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            }
        )
        ExposedDropdownMenu(
            expanded = !isLoading && expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name, fontFamily = funnelSansFamily) },
                    onClick = { onCategorySelected(category.id); expanded = false }
                )
            }
        }
    }
}

@Composable
private fun FormSection(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
            Icon(icon, contentDescription = null, tint = KoruOrange, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = KoruBlack,
                fontFamily = funnelSansFamily
            )
        }
        content()
    }
}

@Composable
private fun ImageUploadBox(
    selectedImageUri: Uri?,
    onLaunchGallery: () -> Unit,
    onLaunchCamera: () -> Unit,
    onClearImage: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, KoruDarkGray, RoundedCornerShape(12.dp))
                .background(KoruGrayBackground),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedImageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onClearImage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(KoruBlack.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(Lucide.X, contentDescription = "Limpiar imagen", tint = KoruWhite, modifier = Modifier.size(18.dp))
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Lucide.ImagePlus, contentDescription = "Agregar imagen", modifier = Modifier.size(48.dp), tint = KoruOrange.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Añade una imagen para tu negocio", color = KoruDarkGray, fontSize = 14.sp, fontFamily = funnelSansFamily, textAlign = TextAlign.Center)
                    Text("(Recomendado: 1200x800px)", color = KoruDarkGray.copy(alpha = 0.7f), fontSize = 12.sp, fontFamily = funnelSansFamily)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedButton(onClick = onLaunchGallery) {
                Icon(Lucide.GalleryHorizontal, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Galería")
            }
            OutlinedButton(onClick = onLaunchCamera) {
                Icon(Lucide.Camera, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Cámara")
            }
        }
    }
}

@Composable
fun AddBusinessSuccessState(onDismiss: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(Lucide.CircleCheck,  contentDescription = "Agregar imagen", modifier = Modifier.size(64.dp), tint = KoruOrange)
            Spacer(modifier = Modifier.height(16.dp))
            Text("¡Negocio creado con éxito!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = KoruOrange, fontFamily = funnelSansFamily)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
                shape = RoundedCornerShape(35.dp)
            ) {
                Text("Volver", fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = funnelSansFamily, color = KoruWhite)
            }
        }
    }
}

@Composable
fun formTextFieldColors(enabled: Boolean = true): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = KoruInputBackground, unfocusedContainerColor = KoruInputBackground,
        disabledContainerColor = KoruInputBackground.copy(alpha = 0.5f), errorContainerColor = KoruInputBackground,
        focusedIndicatorColor = KoruOrange, unfocusedIndicatorColor = KoruTransparent,
        disabledIndicatorColor = KoruTransparent, errorIndicatorColor = KoruRed,
        focusedTextColor = KoruBlack, unfocusedTextColor = KoruBlack,
        disabledTextColor = KoruDarkGray.copy(alpha = 0.7f), errorTextColor = KoruBlack,
        focusedPlaceholderColor = KoruDarkGray, unfocusedPlaceholderColor = KoruDarkGray,
        disabledPlaceholderColor = KoruDarkGray.copy(alpha = 0.5f),
        focusedTrailingIconColor = KoruDarkGray, unfocusedTrailingIconColor = KoruDarkGray,
        disabledTrailingIconColor = KoruDarkGray.copy(alpha = 0.5f), errorTrailingIconColor = KoruRed,
        focusedLeadingIconColor = KoruDarkGray,
        unfocusedLeadingIconColor = KoruDarkGray,
        disabledLeadingIconColor = KoruDarkGray.copy(alpha = 0.5f),
        errorLeadingIconColor = KoruDarkGray
    )
}