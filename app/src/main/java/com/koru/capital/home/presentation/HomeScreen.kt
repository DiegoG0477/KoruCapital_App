package com.koru.capital.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.BadgeDollarSign
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Car
import com.composables.icons.lucide.Compass
import com.composables.icons.lucide.DollarSign
import com.composables.icons.lucide.Grid2x2Check
import com.composables.icons.lucide.Handshake
import com.composables.icons.lucide.Linkedin
import com.composables.icons.lucide.ListFilter
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.MessageCircle
import com.koru.capital.core.ui.funnelSansFamily
import com.composables.icons.lucide.Users
import com.koru.capital.R

@Preview(showBackground = true)
@Composable
fun HomeScreen(
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeTop()
        Spacer(modifier = Modifier.height(25.dp))
        HomeBody()
    }
}

@Composable
fun HomeTop() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(top = 23.dp),
                text = "Exploración",
                color = Color.Black,
                fontFamily = funnelSansFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
            )

            Icon(
                Lucide.Settings2,
                contentDescription = "Configuración",
                modifier = Modifier.padding(top = 23.dp).height(30.dp),
                tint = Color.Black
            )
        }
    }

    FilterRow(
        filters = listOf(
            FilterItem.NearMe,
            FilterItem.LessThan50K,
            FilterItem.Category,
            FilterItem.FilterBtn
        )
    )
}

sealed class FilterItem(
    val icon: ImageVector,
    val label: String,
    val action: () -> Unit
) {
    object NearMe : FilterItem(
        icon = Lucide.Compass,
        label = "Cerca de mí",
        action = { /* Acción cuando se selecciona */ }
    )

    object LessThan50K : FilterItem(
        icon = Lucide.DollarSign,
        label = "0 - 50k",
        action = { /* Acción cuando se selecciona */ }
    )

    object Category : FilterItem(
        icon = Lucide.Grid2x2Check,
        label = "Categoría",
        action = { /* Acción cuando se selecciona */ }
    )

    object FilterBtn : FilterItem(
        icon = Lucide.ListFilter,
        label = "",
        action = { /* Acción cuando se selecciona */ }
    )
}

@Composable
fun FilterRow(filters: List<FilterItem>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEach { filter ->
            FilterButton(filter)
        }
    }
}

@Composable
fun FilterButton(filter: FilterItem) {
    Button(
        onClick = { filter.action() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(34.dp),
        modifier = Modifier
            .height(36.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(34.dp)
            )
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(34.dp),
                spotColor = Color.Transparent,
                ambientColor = Color.Transparent
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.height(14.dp),
                imageVector = filter.icon,
                contentDescription = filter.label,
                tint = Color.Black
            )
            Text(
                text = filter.label,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun HomeBody(){
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        BusinessCard()
    }
}

@Composable
fun BusinessCard() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.sample_business),
                contentDescription = "Sample Business",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(9.dp))
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "SLICE: The World's First Nipper with Dual Replaceable Blades  Effortless Precision, Endless Possibilities",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = funnelSansFamily,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BusinessTag(icon = Lucide.Car, text = "Automóviles")
                BusinessTag(icon = Lucide.MapPin, text = "Tuxtla Gutiérrez")
                BusinessTag(icon = Lucide.BadgeDollarSign, text = "5k - 25k")
                Icon(
                    Lucide.Users,
                    modifier = Modifier.height(19.dp),
                    contentDescription = "partners_quantity",
                    //tint = Color.DarkGray
                    tint = Color(0XFF993c07)
                )
            }

            Text(
                modifier = Modifier.padding(top = 9.dp),
                text = "SolidServicios es una empresa Mexicana, proveedora de soluciones de ingeniería para el área de diseño y manufactura industrial. Buscamos Ser la mejor alternativa en la implantación de soluciones CAD/CAM/CAE y servicios enfocados al diseño y desarrollo de productos en la industria manufacturera.",
                fontFamily = funnelSansFamily
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = "Modelo de negocio:",
                color = Color(0XFFCC5500),
                fontSize = 12.sp,
                fontFamily = funnelSansFamily
            )

            Text(
                modifier = Modifier.padding(top = 9.dp),
                text = "B2B con contratos anuales y logística propia",
                fontFamily = funnelSansFamily
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(35.dp)
                        )
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_profile),
                            contentDescription = "Sample Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(26.dp)
                                .width(26.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = "Daniel Rodríguez",
                            fontFamily = funnelSansFamily,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            Lucide.MessageCircle,
                            modifier = Modifier.height(20.dp),
                            contentDescription = "message",
                            tint = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            Lucide.Linkedin,
                            modifier = Modifier.height(20.dp),
                            contentDescription = "linkedin",
                            tint = Color.DarkGray
                        )
                    }

                    // Iconos de partnership y guardados
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Lucide.Handshake,
                            modifier = Modifier.height(21.dp),
                            contentDescription = "partnership",
                            tint = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Icon(
                            Lucide.Bookmark,
                            modifier = Modifier.height(21.dp),
                            contentDescription = "saves_quantity",
                            tint = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = "54",
                            fontFamily = funnelSansFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun BusinessTag(
    icon: ImageVector,
    text: String? = null,
    contentDescription: String? = null
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0XFFFFF4C4),
                shape = RoundedCornerShape(35.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            icon,
            modifier = Modifier.height(16.dp),
            contentDescription = contentDescription ?: text,
            tint = Color(0XFF993C07)
        )
        text?.let {
            Text(
                text = it,
                color = Color(0XFF993C07),
                fontSize = 12.sp,
                fontFamily = funnelSansFamily
            )
        }
    }
}