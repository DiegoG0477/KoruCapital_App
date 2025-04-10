// capital/core/ui/navigation/NavItem.kt
package com.koru.capital.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
// Importar el icono deseado, por ejemplo PlusCircle o Plus
import com.composables.icons.lucide.CircleUserRound
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Orbit
import com.composables.icons.lucide.CirclePlus // <-- Icono para "Crear"
import com.composables.icons.lucide.Store
// Eliminar import de Bookmark si ya no se usa

// Represents items in the Bottom Navigation Bar
sealed class NavItem(
    val icon: ImageVector,
    val label: String,
    val route: String // This route now corresponds to the ROOT route in Routes object
) {
    object Home : NavItem(
        icon = Lucide.Orbit,
        label = "Oportunidades",
        route = Routes.HOME // Use root route from Routes
    )

    // --- REMOVE NavItem.Saved ---
    /*
    object Saved : NavItem( // Renamed from Apoyos to match Routes
        icon = Lucide.Bookmark,
        label = "Guardados",
        route = Routes.SAVED // Use root route from Routes
    )
    */

    // --- ADD NavItem.AddBusiness ---
    object AddBusiness : NavItem( // Nuevo item
        icon = Lucide.CirclePlus, // Usar icono de "añadir"
        label = "Crear", // Etiqueta corta
        route = Routes.ADD_BUSINESS_SCREEN // Apunta DIRECTAMENTE a la pantalla
    )

    object MyBusinesses : NavItem( // Renamed from MisNegocios
        icon = Lucide.Store,
        label = "Mis Negocios",
        route = Routes.MY_BUSINESSES // Use root route from Routes
    )

    object MyAccount : NavItem( // Renamed from MiCuenta
        icon = Lucide.CircleUserRound,
        label = "Mi Cuenta",
        route = Routes.MY_ACCOUNT // Use root route from Routes
    )
}