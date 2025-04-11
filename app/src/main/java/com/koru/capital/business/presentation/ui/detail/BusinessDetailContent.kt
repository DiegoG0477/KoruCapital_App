package com.koru.capital.business.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.composables.icons.lucide.*
import com.koru.capital.R
import com.koru.capital.business.domain.model.Business
import com.koru.capital.business.presentation.viewmodel.BusinessDetailUiState
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessDetailContent(
    uiState: BusinessDetailUiState,
    onBackClick: () -> Unit,
    onAssociateClick: () -> Unit,
    onDismissAssociationDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.business?.name ?: "Detalles",
                        fontFamily = funnelSansFamily,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoruWhite,
                    titleContentColor = KoruOrange,
                    navigationIconContentColor = KoruBlack,
                    actionIconContentColor = KoruBlack
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                uiState.business != null -> {
                    BusinessDetails(
                        business = uiState.business,
                        onAssociateClick = onAssociateClick
                    )
                }
                else -> {
                    Text(
                        text = "No se encontraron datos del negocio.",
                        modifier = Modifier.padding(32.dp).align(Alignment.Center)
                    )
                }
            }

            if (uiState.showAssociationDialog && uiState.business != null) {
                AssociationSuccessDialog(
                    businessName = uiState.business.name,
                    ownerName = uiState.business.ownerName ?: "Propietario",
                    ownerEmail = uiState.business.ownerEmail ?: "No disponible",
                    ownerPhone = uiState.business.ownerPhone ?: "No disponible",
                    onDismiss = onDismissAssociationDialog
                )
            }
        }
    }
}

@Composable
fun BusinessDetails(
    business: Business,
    onAssociateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(business.imageUrl ?: R.drawable.sample_business)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.sample_business),
            error = painterResource(R.drawable.sample_business),
            contentDescription = business.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Column(Modifier.padding(16.dp)) {
            Text(
                text = business.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = funnelSansFamily,
                color = KoruOrange
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                business.categoryName?.let { DetailTag(icon = Lucide.Tag, text = it) }
                business.locationName?.let { DetailTag(icon = Lucide.MapPin, text = it) }
                business.investmentRange?.let { DetailTag(icon = Lucide.BadgeDollarSign, text = it) }
            }
            Spacer(Modifier.height(16.dp))

            DetailSection(title = "Descripción", icon = Lucide.FileText) {
                Text(
                    text = business.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = funnelSansFamily,
                    color = KoruBlack
                )
            }
            Spacer(Modifier.height(16.dp))

            DetailSection(title = "Finanzas", icon = Lucide.TrendingUp) {
                FinancialDetailItem("Inversión Requerida:", "MXN ${"%,.0f".format(business.investment)}")
                FinancialDetailItem("Ganancia Estimada:", "${business.profitPercentage.toInt()}%")
                FinancialDetailItem("Ingresos Mensuales Est.:", "MXN ${"%,.0f".format(business.monthlyIncome)}")
            }
            Spacer(Modifier.height(16.dp))

            DetailSection(title = "Modelo de Negocio", icon = Lucide.Star) {
                Text(
                    text = business.businessModel,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = funnelSansFamily,
                    color = KoruBlack
                )
            }
            Spacer(Modifier.height(16.dp))


        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ExtendedFloatingActionButton(
            text = { Text("Asociarme", fontFamily = funnelSansFamily, fontWeight = FontWeight.Bold) },
            icon = { Icon(Lucide.Handshake, contentDescription = "Asociarme") },
            onClick = onAssociateClick,
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = KoruOrange,
            contentColor = KoruWhite
        )
    }
}

@Composable
fun DetailSection(title: String, icon: ImageVector? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
            icon?.let {
                Icon(it, contentDescription = null, tint = KoruOrange, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = funnelSansFamily,
                color = KoruBlack
            )
        }
        Divider(color = KoruGrayBackground, thickness = 1.dp)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@Composable
fun DetailTag(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .background(KoruLightYellow, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(icon, contentDescription = null, tint = KoruDarkOrange, modifier = Modifier.size(15.dp))
        Text(text, color = KoruDarkOrange, fontSize = 12.sp, fontFamily = funnelSansFamily)
    }
}

@Composable
fun FinancialDetailItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontFamily = funnelSansFamily, color = KoruDarkGray, fontSize = 14.sp)
        Text(value, fontFamily = funnelSansFamily, color = KoruBlack, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
    Spacer(Modifier.height(4.dp))
}


@Composable
fun AssociationSuccessDialog(
    businessName: String,
    ownerName: String,
    ownerEmail: String,
    ownerPhone: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Lucide.PartyPopper, contentDescription = null, tint = KoruOrange, modifier = Modifier.size(48.dp)) },
        title = { Text("¡Felicidades!", fontWeight = FontWeight.Bold, fontFamily = funnelSansFamily, textAlign = TextAlign.Center) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Has mostrado interés en asociarte con '$businessName'.", textAlign = TextAlign.Center, fontFamily = funnelSansFamily)
                Spacer(Modifier.height(16.dp))
                Text("Datos de Contacto:", fontWeight = FontWeight.Medium, fontFamily = funnelSansFamily)
                Spacer(Modifier.height(8.dp))
                Text("Nombre: $ownerName", fontFamily = funnelSansFamily)
                Text("Correo: $ownerEmail", fontFamily = funnelSansFamily)
                Text("Teléfono: $ownerPhone", fontFamily = funnelSansFamily)
                Spacer(Modifier.height(8.dp))
                Text("¡Ponte en contacto para explorar la oportunidad!", textAlign = TextAlign.Center, fontSize = 12.sp, color = KoruDarkGray, fontFamily = funnelSansFamily)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = KoruOrange)
            ) {
                Text("Entendido", color = KoruWhite, fontFamily = funnelSansFamily)
            }
        },
        containerColor = KoruWhite
    )
}