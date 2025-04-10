package com.koru.capital.profile.presentation.ui.edit

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color // Import Color if needed explicitly
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.composables.icons.lucide.*
import com.koru.capital.R
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.profile.presentation.viewmodel.EditProfileUiState
import com.koru.capital.register.presentation.components.LabeledTextField
import com.koru.capital.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onBackClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onLinkedInChange: (String) -> Unit,
    onInstagramChange: (String) -> Unit,
    onImagePickerClick: () -> Unit, // Lambda to launch the picker
    onImageSelected: (Uri?) -> Unit, // Actual selection handler (might be redundant now)
    onSaveChanges: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontFamily = funnelSansFamily, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KoruWhite,
                    titleContentColor = KoruOrange,
                    navigationIconContentColor = KoruBlack
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = KoruOrange)
                }
            } else {
                // --- Profile Image ---
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(uiState.newProfileImageUri ?: uiState.currentProfileImageUrl ?: R.drawable.sample_profile)
                            .crossfade(true)
                            .transformations(coil3.transform.CircleCropTransformation())
                            .build(),
                        placeholder = painterResource(R.drawable.sample_profile),
                        error = painterResource(R.drawable.sample_profile),
                        contentDescription = "Foto de Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, KoruOrange, CircleShape)
                            .clickable(onClick = onImagePickerClick) // Make image clickable to change
                    )
                    // Overlay icon suggesting editability
                    Box(
                        modifier = Modifier
                            .size(120.dp) // Match image size
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape) // Semi-transparent overlay
                            .clickable(onClick = onImagePickerClick), // Also clickable
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Lucide.Camera,
                            contentDescription = "Cambiar foto",
                            tint = KoruWhite.copy(alpha = 0.8f), // Make icon slightly transparent
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    /* Alternative: Button below image
                     Button(onClick = onImagePickerClick, modifier = Modifier.align(Alignment.BottomCenter).offset(y = 20.dp)) {
                         Icon(Lucide.Camera, ...)
                         Text("Cambiar foto")
                     }
                     */
                }
                Spacer(Modifier.height(24.dp))

                // --- Text Fields ---
                LabeledTextField(
                    value = uiState.firstName,
                    onValueChange = onFirstNameChange,
                    label = "Nombre(s)",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(vertical = 8.dp),
                    isError = uiState.errorMessage?.contains("Nombre") == true
                )
                LabeledTextField(
                    value = uiState.lastName,
                    onValueChange = onLastNameChange,
                    label = "Apellido(s)",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(vertical = 8.dp),
                    isError = uiState.errorMessage?.contains("apellido") == true
                )
                LabeledTextField(
                    value = uiState.bio,
                    onValueChange = onBioChange,
                    label = "Biografía (Opcional)",
                    maxLines = 4,
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(vertical = 8.dp).heightIn(min = 100.dp)
                )
                LabeledTextField(
                    value = uiState.linkedInUrl,
                    onValueChange = onLinkedInChange,
                    label = "Perfil LinkedIn (URL Completa, Opcional)",
                    leadingIcon = { Icon(Lucide.Linkedin, contentDescription = null, tint=KoruDarkGray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LabeledTextField(
                    value = uiState.instagramUrl,
                    onValueChange = onInstagramChange,
                    label = "Instagram (URL Completa, Opcional)",
                    leadingIcon = { Icon(Lucide.Instagram, contentDescription = null, tint=KoruDarkGray) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // --- Error Message ---
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage,
                        color = KoruRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // --- Save Button ---
                Button(
                    onClick = onSaveChanges,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !uiState.isSaving,
                    shape = RoundedCornerShape(35.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KoruOrange)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = KoruWhite, strokeWidth = 2.dp)
                    } else {
                        Text("Guardar Cambios", color = KoruWhite, fontFamily = funnelSansFamily, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}