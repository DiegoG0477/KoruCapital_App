package com.koru.capital.core.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.koru.capital.auth.presentation.login.ui.LoginScreen
import com.koru.capital.auth.presentation.register.ui.basic.RegisterEmailPasswordScreen
import com.koru.capital.auth.presentation.register.ui.personal.RegisterPersonalInfoScreen
import com.koru.capital.auth.presentation.register.viewmodel.RegisterViewModel
import com.koru.capital.business.presentation.ui.add.AddBusinessScreen
import com.koru.capital.business.presentation.ui.detail.BusinessDetailScreen
import com.koru.capital.business.presentation.ui.edit.EditBusinessScreen
import com.koru.capital.business.presentation.ui.mine.MyBusinessesScreen
import com.koru.capital.core.ui.components.PlaceholderScreen
import com.koru.capital.home.presentation.ui.HomeScreen
import com.koru.capital.profile.presentation.ui.EditProfileScreen
import com.koru.capital.core.ui.theme.KoruOrange
import com.koru.capital.profile.presentation.ui.myAcc.MyAccountScreen


@Composable
fun NavigationWrapper(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.INITIAL_LOADING) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = KoruOrange)
            }
        }

        composable(Routes.LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register_graph")
                }
            )
        }

        navigation(
            startDestination = Routes.REGISTER_EMAIL_PASSWORD_SCREEN,
            route = "register_graph"
        ) {
            composable(Routes.REGISTER_EMAIL_PASSWORD_SCREEN) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry("register_graph")
                }
                val registerViewModel: RegisterViewModel = hiltViewModel(parentEntry)
                RegisterEmailPasswordScreen(
                    viewModel = registerViewModel,
                    onNavigateToPersonalInfo = {
                        navController.navigate(Routes.REGISTER_PERSONAL_INFO_SCREEN)
                    },
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN_SCREEN) {
                            popUpTo("register_graph") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.REGISTER_PERSONAL_INFO_SCREEN) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry("register_graph")
                }
                val registerViewModel: RegisterViewModel = hiltViewModel(parentEntry)
                RegisterPersonalInfoScreen(
                    viewModel = registerViewModel,
                    onRegistrationComplete = {
                        navController.navigate(Routes.LOGIN_SCREEN) {
                            popUpTo("register_graph") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        navigation(
            startDestination = Routes.HOME_SCREEN,
            route = Routes.HOME
        ) {
            composable(Routes.HOME_SCREEN) {
                HomeScreen(
                    onNavigateToDetails = { businessId ->
                        navController.navigate(Routes.businessDetail(businessId))
                    },
                    onNavigateToSettings = {
                        navController.navigate(Routes.SETTINGS_SCREEN)
                    }
                )
            }
        }


        navigation(
            startDestination = Routes.MY_BUSINESSES_SCREEN,
            route = Routes.MY_BUSINESSES
        ) {
            composable(Routes.MY_BUSINESSES_SCREEN) {
                MyBusinessesScreen(
                    onAddBusiness = { navController.navigate(Routes.ADD_BUSINESS_SCREEN) },
                    onEditBusiness = { businessId ->
                        navController.navigate(Routes.editBusiness(businessId))
                    }
                )
            }
            composable(
                route = Routes.EDIT_BUSINESS_SCREEN,
                arguments = listOf(navArgument("businessId") { type = NavType.StringType })
            ) { backStackEntry ->
                val businessId = backStackEntry.arguments?.getString("businessId")
                if (businessId != null) {
                    EditBusinessScreen(
                        businessId = businessId,
                        onBackClick = { navController.popBackStack() }
                    )
                } else {
                    navController.popBackStack()
                }
            }
        }

        navigation(
            startDestination = Routes.MY_ACCOUNT_SCREEN,
            route = Routes.MY_ACCOUNT
        ) {
            composable(Routes.MY_ACCOUNT_SCREEN) {
                MyAccountScreen(
                    onNavigateToEditProfile = { navController.navigate(Routes.EDIT_PROFILE_SCREEN) },
                    onNavigateToSettings = { navController.navigate(Routes.SETTINGS_SCREEN) },
                    onLogout = {
                        navController.navigate(Routes.LOGIN_SCREEN) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.EDIT_PROFILE_SCREEN) {
                EditProfileScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.SETTINGS_SCREEN) {
                PlaceholderScreen(screenName = "Ajustes") {
                    navController.popBackStack()
                }
            }
        }


        composable(Routes.ADD_BUSINESS_SCREEN) {
            AddBusinessScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = Routes.BUSINESS_DETAIL_SCREEN,
            arguments = listOf(navArgument("businessId") { type = NavType.StringType })
        ) { backStackEntry ->
            val businessId = backStackEntry.arguments?.getString("businessId")
            if (businessId != null) {
                BusinessDetailScreen(
                    businessId = businessId,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

    }
}