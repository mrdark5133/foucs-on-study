package com.focusstudy.app.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusstudy.app.FocusApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = getApplication<FocusApplication>()
    private val limitRepository = app.limitRepository

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadLimits()
    }

    private fun loadLimits() {
        viewModelScope.launch {
            limitRepository.getAllLimitsFlow().collect { limits ->
                _uiState.update {
                    it.copy(
                        appLimits = limits,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onLimitMinutesChanged(packageName: String, newMinutes: Int) {
        viewModelScope.launch {
            val current = _uiState.value.appLimits.find { it.packageName == packageName }
            val isEnabled = current?.isEnabled ?: true
            limitRepository.updateLimit(packageName, newMinutes, isEnabled)
        }
    }

    fun onTrackingToggled(packageName: String, isEnabled: Boolean) {
        viewModelScope.launch {
            val current = _uiState.value.appLimits.find { it.packageName == packageName }
            val minutes = current?.dailyLimitMinutes ?: 30
            limitRepository.updateLimit(packageName, minutes, isEnabled)
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(saveSuccessMessage = null) }
    }
}
