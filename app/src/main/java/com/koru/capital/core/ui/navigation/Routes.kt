// capital/core/ui/navigation/Routes.kt
package com.koru.capital.core.ui.navigation

object Routes {
    /**
     * Ruta placeholder usada como destino inicial mientras se verifica el estado de autenticación.
     */
    const val INITIAL_LOADING = "initial_loading"

    // --- Main Bottom Nav Root Destinations (Graph Routes) ---
    const val HOME = "home_root"
    // const val SAVED = "saved_root" // <-- ELIMINAR ROOT DE SAVED
    const val MY_BUSINESSES = "my_businesses_root"
    const val MY_ACCOUNT = "my_account_root"
    // ADD_BUSINESS no necesita un "root" porque se navega directo a la pantalla

    // --- Screens within Graphs ---

    // Home Flow
    const val HOME_SCREEN = "home"

    // Saved Flow (Eliminar si ya no se usa)
    // const val SAVED_SCREEN = "saved" // <-- ELIMINAR

    // My Businesses Flow
    const val MY_BUSINESSES_SCREEN = "my_businesses"
    // ADD_BUSINESS_SCREEN ya no está aquí, es top-level
    const val EDIT_BUSINESS_SCREEN = "edit_business/{businessId}" // Con argumento

    // My Account Flow
    const val MY_ACCOUNT_SCREEN = "my_account"
    const val EDIT_PROFILE_SCREEN = "edit_profile"
    const val SETTINGS_SCREEN = "settings" // Placeholder o pantalla real

    // --- Standalone Screens / Flows ---

    // Auth
    const val LOGIN_SCREEN = "login"

    // Register Flow (Graph route name is "register_graph", defined in NavigationWrapper)
    const val REGISTER_EMAIL_PASSWORD_SCREEN = "register_email_password"
    const val REGISTER_PERSONAL_INFO_SCREEN = "register_personal_info"

    // Business Screens (Standalone or Top-Level)
    const val BUSINESS_DETAIL_SCREEN = "business_detail/{businessId}" // Con argumento
    const val ADD_BUSINESS_SCREEN = "add_business" // <-- Ahora es top-level

    // --- Helper Functions for Routes with Arguments ---

    /** Genera la ruta para la pantalla de detalles de un negocio específico. */
    fun businessDetail(businessId: String) = "business_detail/$businessId"

    /** Genera la ruta para la pantalla de edición de un negocio específico. */
    fun editBusiness(businessId: String) = "edit_business/$businessId"
}