package com.koru.capital.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.koru.capital.core.ui.navigation.NavItem

@Composable
fun BottomNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val navItems = listOf(
        NavItem.Home,
        NavItem.Apoyos,
        NavItem.MisNegocios,
        NavItem.MiCuenta
    )

    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = Color(0xFFF5F5F5)
    ) {
        navItems.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        navItem.icon,
                        contentDescription = navItem.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = navItem.label,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentRoute == navItem.route,
                onClick = { onNavigate(navItem.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0XFFCC5500),
                    selectedTextColor = Color(0XFFCC5500),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                alwaysShowLabel = true
            )
        }
    }
}
