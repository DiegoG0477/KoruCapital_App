package com.koru.capital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koru.capital.auth.domain.usecase.CheckAuthStatusUseCase
import com.koru.capital.core.ui.components.BottomNav
import com.koru.capital.core.ui.navigation.NavigationWrapper
import com.koru.capital.core.ui.navigation.Routes
import com.koru.capital.core.ui.theme.KoruTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


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



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            KoruTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val authCheckStatus by mainViewModel.authCheckStatus.collectAsStateWithLifecycle()

                LaunchedEffect(authCheckStatus) {
                    if (authCheckStatus != AuthCheckStatus.Loading) {
                        val destination = if (authCheckStatus == AuthCheckStatus.LoggedIn) {
                            Routes.HOME
                        } else {
                            Routes.LOGIN_SCREEN
                        }
                        navController.navigate(destination) {
                            popUpTo(Routes.INITIAL_LOADING) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }

                val currentRootRoute = remember(currentDestination) {
                    navDestinationToRootRoute(currentDestination)
                }
                val showBottomBar = currentDestination.shouldShowBottomBar

                Scaffold(
                    bottomBar = {
                        if (showBottomBar && authCheckStatus != AuthCheckStatus.Loading) {
                            BottomNav(
                                currentRoute = currentRootRoute,
                                onNavigate = { route ->
                                    if (route == Routes.ADD_BUSINESS_SCREEN) {
                                        navController.navigate(route) {
                                            launchSingleTop = true
                                        }
                                    } else {
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
                    NavigationWrapper(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        startDestination = Routes.INITIAL_LOADING
                    )
                }
            }
        }
    }

    private fun navDestinationToRootRoute(destination: NavDestination?): String? {
        return destination?.hierarchy?.firstOrNull { navDest ->
            navDest.route in rootRoutesForBottomNav
        }?.route
    }

    private val rootRoutesForBottomNav = setOf(
        Routes.HOME,
        Routes.ADD_BUSINESS_SCREEN,
        Routes.MY_BUSINESSES,
        Routes.MY_ACCOUNT
    )

    private val bottomNavHiddenRoutes = setOf(
        Routes.INITIAL_LOADING,
        Routes.LOGIN_SCREEN,
        "register_graph",
        Routes.ADD_BUSINESS_SCREEN,
        Routes.EDIT_BUSINESS_SCREEN,
        Routes.BUSINESS_DETAIL_SCREEN
    )

    private val NavDestination?.shouldShowBottomBar: Boolean
        get() {
            if (this == null) return false
            val isInPotentiallyVisibleHierarchy = hierarchy.any { it.route in rootRoutesForBottomNav }
            val isExplicitlyHidden = hierarchy.any { it.route in bottomNavHiddenRoutes }
            return isInPotentiallyVisibleHierarchy && !isExplicitlyHidden
        }
}