package com.focusstudy.app.ui.settings

import com.focusstudy.app.data.model.AppLimit

data class SettingsUiState(
    val appLimits: List<AppLimit> = emptyList(),
    val isLoading: Boolean = true,
    val saveSuccessMessage: String? = null
)
