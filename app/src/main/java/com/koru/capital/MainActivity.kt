// capital/MainActivity.kt
package com.koru.capital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels // Importar para by viewModels
import androidx.compose.foundation.layout.Box // Necesario para el composable de carga
import androidx.compose.foundation.layout.fillMaxSize // Necesario para el composable de carga
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator // Necesario para el composable de carga
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // Necesario para el composable de carga
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel // Para ViewModel en Composables (si fuera necesario)
import androidx.lifecycle.ViewModel // Para MainViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle // Importar lifecycle-aware collector
import androidx.lifecycle.viewModelScope // Para MainViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost // Necesario si NavigationWrapper se moviera aquí
import androidx.navigation.compose.composable // Necesario para el composable de carga
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koru.capital.auth.domain.usecase.CheckAuthStatusUseCase // Necesario para MainViewModel
import com.koru.capital.core.ui.components.BottomNav
import com.koru.capital.core.ui.navigation.NavigationWrapper
import com.koru.capital.core.ui.navigation.Routes
import com.koru.capital.core.ui.theme.KoruTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel // Para MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow // Para MainViewModel
import kotlinx.coroutines.flow.asStateFlow // Para MainViewModel
import kotlinx.coroutines.flow.update // Para MainViewModel
import kotlinx.coroutines.launch // Para MainViewModel
import javax.inject.Inject // Para MainViewModel

// --- Definición del ViewModel y su Estado DENTRO del archivo MainActivity.kt ---

sealed interface AuthCheckStatus {
    object Loading : AuthCheckStatus
    object LoggedIn : AuthCheckStatus
    object LoggedOut : AuthCheckStatus
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase
) : ViewModel() {

    private val _authCheckStatus = MutableStateFlow<AuthCheckStatus>(AuthCheckStatus.Loading)
    val authCheckStatus = _authCheckStatus.asStateFlow()

    init {
        performAuthCheck()
    }

    private fun performAuthCheck() {
        viewModelScope.launch {
            val isLoggedIn = checkAuthStatusUseCase()
            _authCheckStatus.update {
                if (isLoggedIn) AuthCheckStatus.LoggedIn else AuthCheckStatus.LoggedOut
            }
        }
    }
}

// --- Fin Definición ViewModel ---


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inyectar MainViewModel usando KTX
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

//        WindowCompat.getInsetsController(window, window.decorView).apply {
//            isAppearanceLightStatusBars = !resources.configuration.isNightModeActive
//        }

        setContent {
            KoruTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Observar el estado del chequeo de autenticación
                val authCheckStatus by mainViewModel.authCheckStatus.collectAsStateWithLifecycle()

                // Efecto para realizar la navegación inicial DESPUÉS del chequeo
                LaunchedEffect(authCheckStatus) {
                    if (authCheckStatus != AuthCheckStatus.Loading) {
                        val destination = if (authCheckStatus == AuthCheckStatus.LoggedIn) {
                            Routes.HOME // Destino si está logueado
                        } else {
                            Routes.LOGIN_SCREEN // Destino si no está logueado
                        }
                        // Navegar al destino y limpiar la pantalla de carga inicial
                        navController.navigate(destination) {
                            popUpTo(Routes.INITIAL_LOADING) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                // Lógica para mostrar/ocultar BottomNav (sin cambios, pero depende de la ruta actual)
                val currentRootRoute = remember(currentDestination) {
                    navDestinationToRootRoute(currentDestination)
                }
                // Usar la extension property es más limpio
                val showBottomBar = currentDestination.shouldShowBottomBar

                Scaffold(
                    bottomBar = {
                        // Mostrar solo si la ruta es adecuada Y el chequeo inicial terminó
                        if (showBottomBar && authCheckStatus != AuthCheckStatus.Loading) {
                            BottomNav(
                                currentRoute = currentRootRoute,
                                onNavigate = { route ->
                                    // Si la ruta es la de "Agregar Negocio", navega directamente
                                    if (route == Routes.ADD_BUSINESS_SCREEN) {
                                        navController.navigate(route) {
                                            // No hacer popUpTo aquí si quieres poder volver
                                            launchSingleTop = true // Evita múltiples instancias
                                        }
                                    } else {
                                        // Comportamiento normal para las otras pestañas raíz
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    // NavigationWrapper ahora inicia en una ruta de carga temporal
                    NavigationWrapper(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        startDestination = Routes.INITIAL_LOADING // Inicia aquí mientras se chequea auth
                    )
                }
            }
        }
    }

    // --- Helpers para BottomNav ---
    private fun navDestinationToRootRoute(destination: NavDestination?): String? {
        return destination?.hierarchy?.firstOrNull { navDest ->
            navDest.route in rootRoutesForBottomNav // Usa el set actualizado
        }?.route
    }

    // Actualizar este set
    private val rootRoutesForBottomNav = setOf(
        Routes.HOME,
        // Routes.SAVED, // <-- ELIMINAR
        Routes.ADD_BUSINESS_SCREEN, // <-- AÑADIR RUTA DIRECTA DE AGREGAR
        Routes.MY_BUSINESSES,
        Routes.MY_ACCOUNT
    )

    // Asegurarse que INITIAL_LOADING y ADD_BUSINESS_SCREEN estén en las rutas ocultas
    private val bottomNavHiddenRoutes = setOf(
        Routes.INITIAL_LOADING, // Ocultar durante la carga inicial
        Routes.LOGIN_SCREEN,
        "register_graph", // Ocultar durante todo el flujo de registro
        Routes.ADD_BUSINESS_SCREEN, // <-- AÑADIR: Ocultar barra en pantalla de agregar
        Routes.EDIT_BUSINESS_SCREEN,
        Routes.BUSINESS_DETAIL_SCREEN
        // Añadir otras pantallas individuales si es necesario
    )

    // Extension property que considera las rutas raíz y las ocultas
    private val NavDestination?.shouldShowBottomBar: Boolean
        get() {
            if (this == null) return false
            // Verificar si estamos en una ruta que *podría* mostrar la barra (es raíz o hija de raíz)
            // OJO: Para el caso de ADD_BUSINESS_SCREEN, que es ruta directa y root, necesitamos la segunda condición
            val isInPotentiallyVisibleHierarchy = hierarchy.any { it.route in rootRoutesForBottomNav }
            // Verificar si estamos en una ruta donde explícitamente queremos ocultarla
            val isExplicitlyHidden = hierarchy.any { it.route in bottomNavHiddenRoutes }
            // Mostrar solo si está en jerarquía visible Y no está explícitamente oculta
            return isInPotentiallyVisibleHierarchy && !isExplicitlyHidden
        }
}