package com.focusstudy.app.ui.debug

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusstudy.app.FocusApplication
import com.focusstudy.app.ui.overlay.BlockOverlayActivity
import com.focusstudy.app.util.Constants
import com.focusstudy.app.util.DateUtils
import com.focusstudy.app.util.PermissionHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DebugViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<FocusApplication>()
    private val limitRepository = app.limitRepository
    private val database = app.database
    private val usageStatsHelper = app.usageStatsHelper

    private val _uiState = MutableStateFlow(DebugUiState())
    val uiState: StateFlow<DebugUiState> = _uiState.asStateFlow()

    init {
        refreshDiagnostics()
    }

    fun refreshDiagnostics() {
        viewModelScope.launch {
            val tz = TimeZone.getDefault()
            val startOfDay = DateUtils.getStartOfDayMillis()
            val now = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = tz
            }

            val hasUsage = PermissionHelper.hasUsageStatsPermission(app)
            val hasAccessibility = PermissionHelper.isAccessibilityServiceEnabled(app)
            val canOverlay = PermissionHelper.canDrawOverlays(app)

            val igMillis = withContext(Dispatchers.IO) {
                usageStatsHelper.getTodayForegroundDurationMillis(Constants.PACKAGE_INSTAGRAM)
            }
            val ytMillis = withContext(Dispatchers.IO) {
                usageStatsHelper.getTodayForegroundDurationMillis(Constants.PACKAGE_YOUTUBE)
            }

            val limits = withContext(Dispatchers.IO) {
                limitRepository.getAllLimits()
            }
            val records = withContext(Dispatchers.IO) {
                val todayStr = DateUtils.getFormattedDateString()
                database.usageRecordDao().getRecordsForDate(todayStr)
            }

            _uiState.update {
                it.copy(
                    hasUsagePermission = hasUsage,
                    hasAccessibilityPermission = hasAccessibility,
                    canDrawOverlays = canOverlay,
                    localTimeZone = "${tz.id} (${tz.getDisplayName(false, TimeZone.SHORT)})",
                    startOfDayFormatted = sdf.format(Date(startOfDay)),
                    currentTimeFormatted = sdf.format(Date(now)),
                    instagramUsedMillis = igMillis,
                    youtubeUsedMillis = ytMillis,
                    limitsInDb = limits,
                    usageRecordsInDb = records,
                    logOutput = "Queried stats at ${sdf.format(Date(now))}"
                )
            }
        }
    }

    fun testTriggerOverlay(packageName: String, appName: String) {
        val intent = Intent(app, BlockOverlayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(BlockOverlayActivity.EXTRA_PACKAGE_NAME, packageName)
            putExtra(BlockOverlayActivity.EXTRA_APP_NAME, appName)
            putExtra(BlockOverlayActivity.EXTRA_USED_MINUTES, 30)
            putExtra(BlockOverlayActivity.EXTRA_LIMIT_MINUTES, 30)
        }
        app.startActivity(intent)
    }
}
