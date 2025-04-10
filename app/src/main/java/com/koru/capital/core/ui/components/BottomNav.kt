package com.koru.capital.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koru.capital.core.ui.navigation.NavItem
import com.koru.capital.core.ui.theme.* // Import theme colors

@Composable
fun BottomNav(
    currentRoute: String?, // Route can be null initially or during transitions
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem.Home,
        NavItem.AddBusiness, // Updated name
        NavItem.MyBusinesses, // Updated name
        NavItem.MyAccount // Updated name
    )

    NavigationBar(
        modifier = modifier.height(60.dp),
        containerColor = KoruGrayBackground // Use theme color
    ) {
        navItems.forEach { navItem ->
            // Determine if the current route is part of this nav item's hierarchy
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
                    // Navigate only if not already selected to avoid redundant navigation
                    if (!selected) {
                        onNavigate(navItem.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = KoruOrange, // Use theme color
                    selectedTextColor = KoruOrange, // Use theme color
                    unselectedIconColor = KoruDarkGray, // Use theme color
                    unselectedTextColor = KoruDarkGray, // Use theme color
                    indicatorColor = KoruTransparent // Use theme color
                ),
                alwaysShowLabel = true
            )
        }
    }
}