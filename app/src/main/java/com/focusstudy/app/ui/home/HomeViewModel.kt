package com.focusstudy.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusstudy.app.FocusApplication
import com.focusstudy.app.data.model.TrackedAppInfo
import com.focusstudy.app.util.DateUtils
import com.focusstudy.app.util.PermissionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<FocusApplication>()
    private val usageRepository = app.usageRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    fun onResume() {
        checkPermissions()
        refreshUsage()
    }

    private fun observeData() {
        viewModelScope.launch {
            usageRepository.getTrackedAppsInfoFlow().collect { apps ->
                updateStateWithApps(apps)
            }
        }
    }

    fun refreshUsage() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val apps = usageRepository.refreshTodayUsage()
            updateStateWithApps(apps)
            checkPermissions()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun checkPermissions() {
        val hasUsage = PermissionHelper.hasUsageStatsPermission(app)
        val hasAccessibility = PermissionHelper.isAccessibilityServiceEnabled(app)
        _uiState.update {
            it.copy(
                hasUsagePermission = hasUsage,
                hasAccessibilityPermission = hasAccessibility
            )
        }
    }

    private fun updateStateWithApps(apps: List<TrackedAppInfo>) {
        val totalUsed = apps.filter { it.isEnabled }.sumOf { it.usedMinutesToday }
        val totalLimit = apps.filter { it.isEnabled }.sumOf { it.dailyLimitMinutes }
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val formattedDate = dateFormat.format(Date())

        _uiState.update {
            it.copy(
                trackedApps = apps,
                totalUsedMinutesToday = totalUsed,
                totalLimitMinutes = totalLimit,
                todayDateFormatted = formattedDate,
                isLoading = false
            )
        }
    }
}
