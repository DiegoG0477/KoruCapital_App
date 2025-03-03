package com.koru.capital.home.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.Lucide
import com.koru.capital.core.ui.funnelSansFamily
import com.koru.capital.home.presentation.ui.components.BusinessCard
import com.koru.capital.home.presentation.ui.components.FilterItem
import com.koru.capital.home.presentation.ui.components.FilterRow

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

@Composable
fun HomeBody(){
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        BusinessCard()
    }
}