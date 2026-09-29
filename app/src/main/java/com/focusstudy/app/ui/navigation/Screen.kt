package com.focusstudy.app.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object Onboarding : Screen("onboarding")
    data object Debug : Screen("debug")
}
