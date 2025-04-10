package com.koru.capital.profile.presentation.ui.myAcc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp // For Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import com.composables.icons.lucide.Instagram // Use specific icons if available
import com.composables.icons.lucide.Linkedin
import com.composables.icons.lucide.Lucide // Or general Lucide
import com.composables.icons.lucide.Mail
import com.koru.capital.R // Placeholder image resource
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.profile.domain.model.UserProfile
import com.koru.capital.profile.presentation.viewmodel.MyAccountUiState
import com.koru.capital.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountContent(
    uiState: MyAccountUiState,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Perfil", fontFamily = funnelSansFamily, fontWeight = FontWeight.Bold) },
                actions = {
                    // IconButton(onClick = onSettingsClick) { Icon(Icons.Default.Settings, "Ajustes") }
                    IconButton(onClick = onLogoutClick) { Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar Sesión") }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoruWhite,
                    titleContentColor = KoruOrange,
                    actionIconContentColor = KoruDarkGray
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(KoruGrayBackground) // Light background for profile
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = KoruOrange
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = KoruRed,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }
                uiState.userProfile != null -> {
                    UserProfileDetails(
                        profile = uiState.userProfile,
                        onEditClick = onEditProfileClick
                    )
                }
                else -> {
                    Text(
                        text = "No se pudo cargar la información del perfil.",
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun UserProfileDetails(
    profile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // --- Profile Picture ---
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(profile.profileImageUrl ?: R.drawable.sample_profile) // Placeholder
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
                .border(2.dp, KoruOrange, CircleShape) // Add a border
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Name ---
        Text(
            text = "${profile.firstName} ${profile.lastName}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontFamily = funnelSansFamily,
            color = KoruBlack
        )
        Spacer(modifier = Modifier.height(4.dp))

        // --- Join Date --- (Optional)
        profile.joinDate?.let {
            Text(
                text = "Miembro desde $it",
                style = MaterialTheme.typography.bodySmall,
                color = KoruDarkGray,
                fontFamily = funnelSansFamily
            )
            Spacer(modifier = Modifier.height(16.dp))
        }


        // --- Bio --- (Optional)
        profile.bio?.let { bio ->
            if (bio.isNotBlank()) {
                Text(
                    text = bio,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontFamily = funnelSansFamily,
                    color = KoruBlack
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- Social Links ---
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            profile.linkedInUrl?.let { url ->
                SocialLinkButton(
                    icon = Lucide.Linkedin,
                    url = url,
                    contentDescription = "LinkedIn"
                )
                Spacer(Modifier.width(16.dp))
            }
            profile.instagramUrl?.let { url ->
                SocialLinkButton(
                    icon = Lucide.Instagram, // Use specific icon
                    url = url,
                    contentDescription = "Instagram"
                )
            }
            // Add more social links if needed
        }
        if(profile.linkedInUrl != null || profile.instagramUrl != null) {
            Spacer(modifier = Modifier.height(24.dp))
        }


        // --- Contact Info --- (May be hidden or shown based on privacy)
        // You might want to put this in a Card for visual grouping
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = KoruWhite)){
            Column(Modifier.padding(16.dp)) {
                Text("Información de Contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontFamily = funnelSansFamily)
                Spacer(Modifier.height(8.dp))
                ProfileInfoRow(icon = Lucide.Mail, text = profile.email)
                // Add Phone if available and privacy allows
                // ProfileInfoRow(icon = Lucide.Phone, text = profile.phone ?: "No disponible")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))


        // --- Edit Button ---
        Button(
            onClick = onEditClick,
            shape = RoundedCornerShape(35.dp),
            colors = ButtonDefaults.buttonColors(containerColor = KoruOrange),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp), tint = KoruWhite)
            Spacer(Modifier.width(8.dp))
            Text("Editar Perfil", color = KoruWhite, fontFamily = funnelSansFamily)
        }

        Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
    }
}

@Composable
fun SocialLinkButton(icon: ImageVector, url: String, contentDescription: String) {
    val uriHandler = LocalUriHandler.current
    IconButton(
        onClick = { uriHandler.openUri(url) },
        modifier = Modifier
            .size(48.dp)
            .background(KoruLightYellow, CircleShape)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = KoruDarkOrange,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, text: String?) {
    if (!text.isNullOrBlank()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
            Icon(icon, contentDescription = null, tint = KoruDarkGray, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(text, fontFamily = funnelSansFamily, style = MaterialTheme.typography.bodyLarge)
        }
    }
}