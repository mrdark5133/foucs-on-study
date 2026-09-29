package com.focusstudy.app.ui.debug

import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.data.model.UsageRecord

data class DebugUiState(
    val hasUsagePermission: Boolean = false,
    val hasAccessibilityPermission: Boolean = false,
    val canDrawOverlays: Boolean = false,
    val localTimeZone: String = "",
    val startOfDayFormatted: String = "",
    val currentTimeFormatted: String = "",
    val instagramUsedMillis: Long = 0L,
    val youtubeUsedMillis: Long = 0L,
    val limitsInDb: List<AppLimit> = emptyList(),
    val usageRecordsInDb: List<UsageRecord> = emptyList(),
    val logOutput: String = ""
)
