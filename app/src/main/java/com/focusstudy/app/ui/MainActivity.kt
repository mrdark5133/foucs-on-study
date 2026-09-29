package com.focusstudy.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.focusstudy.app.ui.navigation.AppNavGraph
import com.focusstudy.app.ui.navigation.Screen
import com.focusstudy.app.ui.theme.BackgroundDark
import com.focusstudy.app.ui.theme.StudyFocusTheme
import com.focusstudy.app.util.PermissionHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StudyFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    val navController = rememberNavController()

                    val hasPermissions = PermissionHelper.hasUsageStatsPermission(this) &&
                            PermissionHelper.isAccessibilityServiceEnabled(this)
                    val startDestination = if (hasPermissions) Screen.Home.route else Screen.Onboarding.route

                    val shouldNavigateToSettings = intent.getBooleanExtra(EXTRA_NAVIGATE_TO_SETTINGS, false)
                    LaunchedEffect(shouldNavigateToSettings) {
                        if (shouldNavigateToSettings) {
                            navController.navigate(Screen.Settings.route)
                        }
                    }

                    AppNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    companion object {
        const val EXTRA_NAVIGATE_TO_SETTINGS = "extra_navigate_to_settings"
    }
}
