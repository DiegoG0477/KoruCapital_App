package com.koru.capital.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.Bookmark
import com.composables.icons.lucide.CircleUserRound
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Orbit
import com.composables.icons.lucide.Store

sealed class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String
) {
    object Home : NavItem(
        icon = Lucide.Orbit,
        label = "Oportunidades",
        route = "home"
    )

    object Apoyos : NavItem(
        icon = Lucide.Bookmark,
        label = "Guardados",
        route = "guardados"
    )

    object MisNegocios : NavItem(
        icon = Lucide.Store,
        label = "Mis Negocios",
        route = "mis_negocios"
    ) {
        object AddBusiness {
            const val route = "mis_negocios/add_business"
        }
    }

    object MiCuenta : NavItem(
        icon = Lucide.CircleUserRound,
        label = "Mi Cuenta",
        route = "mi_cuenta"
    )
}