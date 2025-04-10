// capital/business/presentation/ui/add/AddBusinessContent.kt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.*
import com.koru.capital.R // Asegúrate que R esté importado correctamente
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
    // Nuevos callbacks para selección de imagen
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onClearImageClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { AddBusinessTopBar(onBackClick = onBackClick) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
        ) {
            when {
                uiState.isLoading -> { // Loading state for submission
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = KoruOrange)
                    }
                }
                uiState.isSuccess -> { // Success state after submission
                    AddBusinessSuccessState(onDismiss = onBackClick)
                }
                else -> { // Default form state
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
                        onGalleryClick = onGalleryClick, // Pass new callback
                        onCameraClick = onCameraClick,   // Pass new callback
                        onClearImageClick = onClearImageClick, // Pass new callback
                        onSubmit = onSubmit
                    )
                }
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = KoruWhite
        )
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
    // Updated image action parameters
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onClearImageClick: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Error Message
        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = KoruRed,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(KoruRed.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Business Image Section
        FormSection(title = "Imagen del Negocio", icon = Lucide.Image) {
            ImageUploadBox(
                selectedImageUri = uiState.selectedImageUri,
                existingImageUrl = null, // Explicitly null for adding
                onGalleryClick = onGalleryClick,
                onCameraClick = onCameraClick,
                onClearImageClick = onClearImageClick
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Business Name Section
        FormSection(title = "Nombre del Negocio", icon = Lucide.Store) {
            OutlinedTextField(
                value = uiState.businessName,
                onValueChange = onBusinessNameChanged,
                placeholder = { Text("Ej. FreshMex") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Description Section
        FormSection(title = "Descripción", icon = Lucide.FileText) {
            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text("Breve descripción de tu negocio y sus ventajas competitivas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                singleLine = false // Allow multiple lines
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Investment & Profit Row
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
                        colors = formTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Use NumberDecimal
                        singleLine = true
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Use NumberDecimal
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // State & Municipality Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Estado", icon = Lucide.MapPin) {
                    StateDropdown(
                        states = uiState.states,
                        selectedStateId = uiState.selectedStateId,
                        onStateSelected = onStateSelected,
                        // Indicate loading state for states
                        isLoading = uiState.isLoadingStates
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                FormSection(title = "Municipio", icon = Lucide.MapPin) {
                    MunicipalityDropdown(
                        municipalities = uiState.municipalities,
                        selectedMunicipalityId = uiState.selectedMunicipalityId,
                        onMunicipalitySelected = onMunicipalitySelected,
                        enabled = uiState.selectedStateId.isNotEmpty(), // Enable only if state is selected
                        isLoading = uiState.isLoadingMunicipalities // Indicate loading state
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Category Section
        FormSection(title = "Categoría", icon = Lucide.Tags) {
            CategoryDropdown(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategorySelected,
                // Indicate loading state for categories
                isLoading = uiState.isLoadingCategories
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Business Model Section
        FormSection(title = "Modelo de Negocio", icon = Lucide.Star) {
            OutlinedTextField(
                value = uiState.businessModel,
                onValueChange = onBusinessModelChanged,
                placeholder = { Text("Describe brevemente cómo genera ingresos el negocio") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                singleLine = false // Allow multiple lines
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Monthly Income Section
        FormSection(title = "Ingresos Mensuales (MXN)", icon = Lucide.DollarSign) {
            OutlinedTextField(
                value = uiState.monthlyIncome,
                onValueChange = onMonthlyIncomeChanged,
                placeholder = { Text("Ej. 120000") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = formTextFieldColors(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Use NumberDecimal
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
            shape = RoundedCornerShape(35.dp),
            enabled = !uiState.isLoading // Disable button while general loading (submission)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = KoruWhite, strokeWidth = 2.dp)
            } else {
                Text(
                    text = "Publicar Negocio",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = funnelSansFamily,
                    color = KoruWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp)) // Footer spacing
    }
}

// --- Componentes Auxiliares (Dropdowns, ImageUploadBox, etc.) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StateDropdown(
    states: List<State>,
    selectedStateId: String,
    onStateSelected: (String) -> Unit,
    isLoading: Boolean // Add loading state
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedState = states.find { it.id == selectedStateId }

    ExposedDropdownMenuBox(
        expanded = !isLoading && expanded, // Don't expand while loading
        onExpandedChange = { if (!isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedState?.name ?: "",
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(if (isLoading) "Cargando..." else "Selecciona un estado") },
            enabled = !isLoading, // Disable while loading
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = !isLoading),
            trailingIcon = {
                if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                else ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                    onClick = {
                        onStateSelected(state.id)
                        expanded = false
                    }
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
    isLoading: Boolean // Add loading state
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMunicipality = municipalities.find { it.id == selectedMunicipalityId }

    ExposedDropdownMenuBox(
        expanded = enabled && !isLoading && expanded,
        onExpandedChange = { if (enabled && !isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedMunicipality?.name ?: "",
            onValueChange = { },
            readOnly = true,
            enabled = enabled && !isLoading,
            placeholder = {
                Text(
                    when {
                        isLoading -> "Cargando..."
                        !enabled -> "Selecciona un estado"
                        else -> "Selecciona un municipio"
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = enabled && !isLoading),
            trailingIcon = {
                if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                else ExposedDropdownMenuDefaults.TrailingIcon(expanded = enabled && !isLoading && expanded)
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
                    onClick = {
                        onMunicipalitySelected(municipality.id)
                        expanded = false
                    }
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
    isLoading: Boolean // Add loading state
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategory = categories.find { it.id == selectedCategoryId }

    ExposedDropdownMenuBox(
        expanded = !isLoading && expanded,
        onExpandedChange = { if (!isLoading) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory?.name ?: "",
            onValueChange = { },
            readOnly = true,
            placeholder = { Text(if (isLoading) "Cargando..." else "Selecciona una categoría") },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(8.dp),
            colors = formTextFieldColors(enabled = !isLoading),
            trailingIcon = {
                if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = KoruOrange)
                else ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
                tint = KoruOrange,
                modifier = Modifier.size(20.dp)
            )
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
    existingImageUrl: String?, // Added for edit functionality (but null here)
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onClearImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // In AddBusinessForm, existingImageUrl is always null
    val displayUri = selectedImageUri

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = KoruDarkGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(KoruGrayBackground),
            contentAlignment = Alignment.Center
        ) {
            if (displayUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(displayUri) // Show selected URI
                        .crossfade(true)
                        .build(),
                    contentDescription = "Imagen del Negocio",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onClearImageClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(32.dp)
                ) {
                    Icon(Lucide.X, contentDescription = "Quitar Imagen", tint = KoruWhite, modifier = Modifier.size(18.dp))
                }
            } else {
                // Placeholder with buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        Lucide.ImagePlus,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = KoruOrange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Agrega una imagen para tu negocio",
                        color = KoruDarkGray,
                        fontSize = 14.sp,
                        fontFamily = funnelSansFamily,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = onGalleryClick,
                            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange.copy(alpha = 0.1f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(Lucide.GalleryHorizontal, contentDescription = null, tint = KoruOrange, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Galería", color = KoruOrange, fontSize = 12.sp, fontFamily = funnelSansFamily)
                        }
                        Button(
                            onClick = onCameraClick,
                            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange.copy(alpha = 0.1f)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(Lucide.Camera, contentDescription = null, tint = KoruOrange, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Cámara", color = KoruOrange, fontSize = 12.sp, fontFamily = funnelSansFamily)
                        }
                    }
                }
            }
        }
        Text( // Recommendation text
            text = "Recomendado: 1200 x 800px",
            color = KoruDarkGray.copy(alpha = 0.8f),
            fontSize = 12.sp,
            fontFamily = funnelSansFamily,
            modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AddBusinessSuccessState(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                tint = KoruOrange
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Negocio creado con éxito!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = KoruOrange,
                fontFamily = funnelSansFamily
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
                shape = RoundedCornerShape(35.dp)
            ) {
                Text(
                    text = "Volver",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = funnelSansFamily,
                    color = KoruWhite
                )
            }
        }
    }
}

// Helper for consistent TextField colors
@Composable
fun formTextFieldColors(enabled: Boolean = true): TextFieldColors {
    return TextFieldDefaults.colors(
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
        focusedPlaceholderColor = KoruDarkGray,
        unfocusedPlaceholderColor = KoruDarkGray,
        disabledPlaceholderColor = KoruDarkGray.copy(alpha = 0.5f),
        focusedTrailingIconColor = KoruDarkGray,
        unfocusedTrailingIconColor = KoruDarkGray,
        disabledTrailingIconColor = KoruDarkGray.copy(alpha = 0.5f),
        errorTrailingIconColor = KoruRed
    )
}