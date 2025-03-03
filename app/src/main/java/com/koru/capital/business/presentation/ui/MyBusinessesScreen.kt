package com.koru.capital.business.presentation.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CirclePlus
import com.composables.icons.lucide.Lucide
import com.koru.capital.core.ui.funnelSansFamily
import com.composables.icons.lucide.Plus

@Preview(showBackground = true)
@Composable
private fun MyBusinessesScreenPreview() {
    MyBusinessesScreen(
        onAddBusiness = {}
    )
}


@Composable
fun MyBusinessesScreen(
    onAddBusiness: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MyBusinessesTop()
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            EmptyBusinessState(
                onAddBusinessClick = { onAddBusiness() }
            )
        }
    }
}

@Composable
fun MyBusinessesTop() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(top = 23.dp),
                text = "Mis Negocios",
                color = Color.Black,
                fontFamily = funnelSansFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
            )
        }
    }
}

@Composable
fun EmptyBusinessState(onAddBusinessClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .background(
                    color = Color(0XFFFFF4c4),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Lucide.CirclePlus,
                contentDescription = null,
                modifier = Modifier.size(45.dp),
                tint = Color(0XFFCC5500)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No tienes negocios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0XFFCC5500),
            fontFamily = funnelSansFamily
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Comienza agregando tu primer negocio para conectar con potenciales socios.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontFamily = funnelSansFamily
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onAddBusinessClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFFCC5500)
            ),
            shape = RoundedCornerShape(35.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Icon(
                Lucide.Plus,
                contentDescription = null,
                modifier = Modifier.height(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar Negocio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = funnelSansFamily
            )
        }
    }
}

