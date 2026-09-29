package com.focusstudy.app.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.ui.components.AppIconBadge
import com.focusstudy.app.ui.theme.AccentEmerald
import com.focusstudy.app.ui.theme.BackgroundDark
import com.focusstudy.app.ui.theme.BorderColor
import com.focusstudy.app.ui.theme.PrimaryIndigo
import com.focusstudy.app.ui.theme.SurfaceDark
import com.focusstudy.app.ui.theme.SurfaceElevated
import com.focusstudy.app.ui.theme.TextMuted
import com.focusstudy.app.ui.theme.TextPrimary
import com.focusstudy.app.ui.theme.TextSecondary
import com.focusstudy.app.util.DateUtils
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "App Limits & Focus",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundDark
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Configure daily usage limits for distracting apps. When your limit is reached, access is automatically blocked.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            items(uiState.appLimits, key = { it.packageName }) { appLimit ->
                AppLimitConfigCard(
                    appLimit = appLimit,
                    onToggle = { isEnabled ->
                        viewModel.onTrackingToggled(appLimit.packageName, isEnabled)
                    },
                    onLimitChange = { newMinutes ->
                        viewModel.onLimitMinutesChanged(appLimit.packageName, newMinutes)
                    }
                )
            }

            item {
                InfoFooterCard()
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppLimitConfigCard(
    appLimit: AppLimit,
    onToggle: (Boolean) -> Unit,
    onLimitChange: (Int) -> Unit
) {
    val presetMinutes = listOf(15, 30, 45, 60, 90, 120)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconBadge(
                    packageName = appLimit.packageName,
                    appName = appLimit.appName
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = appLimit.appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = if (appLimit.isEnabled) "Limit: ${DateUtils.formatMinutes(appLimit.dailyLimitMinutes)}" else "Disabled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (appLimit.isEnabled) AccentEmerald else TextMuted
                    )
                }
            }

            Switch(
                checked = appLimit.isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextPrimary,
                    checkedTrackColor = PrimaryIndigo,
                    uncheckedTrackColor = SurfaceElevated
                )
            )
        }

        if (appLimit.isEnabled) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Daily Time Limit",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Slider(
                value = appLimit.dailyLimitMinutes.toFloat(),
                onValueChange = { onLimitChange(it.roundToInt()) },
                valueRange = 5f..180f,
                steps = 34, // increments of 5 minutes from 5 to 180
                colors = SliderDefaults.colors(
                    thumbColor = PrimaryIndigo,
                    activeTrackColor = PrimaryIndigo,
                    inactiveTrackColor = SurfaceElevated
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Preset Quick Buttons
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetMinutes.forEach { minutes ->
                    val isSelected = appLimit.dailyLimitMinutes == minutes
                    PresetChip(
                        minutes = minutes,
                        isSelected = isSelected,
                        onClick = { onLimitChange(minutes) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetChip(
    minutes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) PrimaryIndigo else SurfaceElevated)
            .border(1.dp, if (isSelected) PrimaryIndigo else BorderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = DateUtils.formatMinutes(minutes),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) TextPrimary else TextSecondary
        )
    }
}

@Composable
private fun InfoFooterCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = PrimaryIndigo,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Daily usage counters reset automatically at 00:00 midnight based on your device's local timezone. Changes to limits take effect immediately.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )
    }
}
