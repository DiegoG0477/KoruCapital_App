package com.koru.capital.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.navigation.NavItem
import com.koru.capital.core.ui.theme.*

@Composable
fun BottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem.Home,
        NavItem.AddBusiness,
        NavItem.MyBusinesses,
        NavItem.MyAccount
    )

    NavigationBar(
        modifier = modifier.height(60.dp),
        containerColor = KoruGrayBackground
    ) {
        navItems.forEach { navItem ->
            val selected = currentRoute?.startsWith(navItem.route) == true

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
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigate(navItem.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KoruOrange,
                    selectedTextColor = KoruOrange,
                    unselectedIconColor = KoruDarkGray,
                    unselectedTextColor = KoruDarkGray,
                    indicatorColor = KoruTransparent
                ),
                alwaysShowLabel = true
            )
        }
    }
}