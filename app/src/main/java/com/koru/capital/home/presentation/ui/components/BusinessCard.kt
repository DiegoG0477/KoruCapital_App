package com.koru.capital.home.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.BadgeDollarSign
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.Car
import com.composables.icons.lucide.Handshake
import com.composables.icons.lucide.Linkedin
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.MessageCircle
import com.composables.icons.lucide.Users
import com.koru.capital.R
import com.koru.capital.core.ui.funnelSansFamily

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