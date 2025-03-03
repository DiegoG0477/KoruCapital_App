package com.koru.capital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.koru.capital.core.ui.components.BottomNav
import com.koru.capital.core.ui.navigation.NavItem
import com.koru.capital.home.presentation.HomeScreen
import com.koru.capital.business.presentation.MyBusinessesScreen
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.navigation
import com.koru.capital.business.presentation.AddBusinessScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

        setContent {
            val navController = rememberNavController()
            var currentRoute by remember { mutableStateOf(NavItem.Home.route) }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                currentRoute = when {
                    destination.hierarchy.any { it.route == NavItem.Home.route } -> NavItem.Home.route
                    destination.hierarchy.any { it.route == NavItem.Apoyos.route } -> NavItem.Apoyos.route
                    destination.hierarchy.any { it.route == NavItem.MisNegocios.route } -> NavItem.MisNegocios.route
                    destination.hierarchy.any { it.route == NavItem.MiCuenta.route } -> NavItem.MiCuenta.route
                    else -> NavItem.Home.route
                }
            }

            androidx.compose.material3.Scaffold(
                bottomBar = {
                    BottomNav(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(NavItem.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            ) { paddingValues ->
//                NavHost(
//                    navController = navController,
//                    startDestination = NavItem.Home.route,
//                    modifier = Modifier.padding(paddingValues)
//                ) {
//                    composable(NavItem.Home.route) { HomeScreen() }
//                    composable(NavItem.MisNegocios.route) { MyBusinessesScreen() }
//                }
                // MainActivity.kt
                NavHost(
                    navController = navController,
                    startDestination = NavItem.Home.route,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(NavItem.Home.route) { HomeScreen() }

                    navigation(
                        startDestination = "mis_negocios_main",
                        route = NavItem.MisNegocios.route
                    ) {
                        composable("mis_negocios_main") {
                            MyBusinessesScreen(
                                onAddBusiness = {
                                    navController.navigate(NavItem.MisNegocios.AddBusiness.route)
                                }
                            )
                        }
                        composable(NavItem.MisNegocios.AddBusiness.route) {
                            AddBusinessScreen(
                                onBackClick = { navController.popBackStack() },
//                                onSubmitSuccess = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}