package com.focusstudy.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.focusstudy.app.ui.debug.DebugScreen
import com.focusstudy.app.ui.debug.DebugViewModel
import com.focusstudy.app.ui.home.HomeScreen
import com.focusstudy.app.ui.home.HomeViewModel
import com.focusstudy.app.ui.onboarding.OnboardingScreen
import com.focusstudy.app.ui.settings.SettingsScreen
import com.focusstudy.app.ui.settings.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToDebug = { navController.navigate(Screen.Debug.route) }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel()
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Debug.route) {
            val debugViewModel: DebugViewModel = viewModel()
            DebugScreen(
                viewModel = debugViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
