package com.koru.capital.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.CircleUserRound
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Orbit
import com.composables.icons.lucide.CirclePlus
import com.composables.icons.lucide.Store

sealed class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
) {
    object Home : NavItem(
        icon = Lucide.Orbit,
        label = "Oportunidades",
        route = Routes.HOME
    )


    object AddBusiness : NavItem(
        icon = Lucide.CirclePlus,
        label = "Crear",
        route = Routes.ADD_BUSINESS_SCREEN
    )

    object MyBusinesses : NavItem(
        icon = Lucide.Store,
        label = "Mis Negocios",
        route = Routes.MY_BUSINESSES
    )

    object MyAccount : NavItem(
        icon = Lucide.CircleUserRound,
        label = "Mi Cuenta",
        route = Routes.MY_ACCOUNT
    )
}