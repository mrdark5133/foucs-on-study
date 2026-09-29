package com.focusstudy.app.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.focusstudy.app.FocusApplication
import com.focusstudy.app.ui.overlay.BlockOverlayActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FocusAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var lastEnforcedPackage: String? = null
    private var lastEnforcedTimestamp: Long = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val eventPackage = event.packageName?.toString() ?: return

        // Prevent infinite loops: ignore our own app, system UI, and launchers
        if (eventPackage == packageName ||
            eventPackage == "com.android.systemui" ||
            eventPackage.contains("launcher", ignoreCase = true)
        ) {
            return
        }

        // Debounce repeated events for the same package within 2 seconds
        val now = System.currentTimeMillis()
        if (eventPackage == lastEnforcedPackage && (now - lastEnforcedTimestamp) < 2000L) {
            return
        }

        serviceScope.launch {
            checkAndEnforceLimit(eventPackage)
        }
    }

    private suspend fun checkAndEnforceLimit(targetPackage: String) {
        val app = application as? FocusApplication ?: return
        val limitRepo = app.limitRepository
        val usageHelper = app.usageStatsHelper

        val limit = limitRepo.getLimit(targetPackage) ?: return
        if (!limit.isEnabled || limit.dailyLimitMinutes <= 0) return

        val usedMinutes = usageHelper.getTodayForegroundMinutes(targetPackage)
        if (usedMinutes >= limit.dailyLimitMinutes) {
            lastEnforcedPackage = targetPackage
            lastEnforcedTimestamp = System.currentTimeMillis()

            if (com.focusstudy.app.BuildConfig.DEBUG) {
                Log.d(TAG, "Limit reached for $targetPackage ($usedMinutes/${limit.dailyLimitMinutes} min). Triggering overlay.")
            }

            launchBlockOverlay(targetPackage, limit.appName, usedMinutes, limit.dailyLimitMinutes)
        }
    }

    private fun launchBlockOverlay(
        packageName: String,
        appName: String,
        usedMinutes: Int,
        limitMinutes: Int
    ) {
        val intent = Intent(this, BlockOverlayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(BlockOverlayActivity.EXTRA_PACKAGE_NAME, packageName)
            putExtra(BlockOverlayActivity.EXTRA_APP_NAME, appName)
            putExtra(BlockOverlayActivity.EXTRA_USED_MINUTES, usedMinutes)
            putExtra(BlockOverlayActivity.EXTRA_LIMIT_MINUTES, limitMinutes)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {
        if (com.focusstudy.app.BuildConfig.DEBUG) {
            Log.d(TAG, "FocusAccessibilityService interrupted")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        private const val TAG = "FocusAccessibility"
    }
}
