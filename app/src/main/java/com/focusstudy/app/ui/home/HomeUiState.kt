package com.focusstudy.app.ui.home

import com.focusstudy.app.data.model.TrackedAppInfo

data class HomeUiState(
    val trackedApps: List<TrackedAppInfo> = emptyList(),
    val totalUsedMinutesToday: Int = 0,
    val totalLimitMinutes: Int = 0,
    val hasUsagePermission: Boolean = false,
    val hasAccessibilityPermission: Boolean = false,
    val isLoading: Boolean = true,
    val todayDateFormatted: String = ""
)
