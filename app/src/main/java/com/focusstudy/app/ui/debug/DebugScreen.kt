package com.focusstudy.app.ui.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.focusstudy.app.ui.theme.AccentEmerald
import com.focusstudy.app.ui.theme.BackgroundDark
import com.focusstudy.app.ui.theme.BorderColor
import com.focusstudy.app.ui.theme.DangerRuby
import com.focusstudy.app.ui.theme.PrimaryIndigo
import com.focusstudy.app.ui.theme.SurfaceDark
import com.focusstudy.app.ui.theme.TextMuted
import com.focusstudy.app.ui.theme.TextPrimary
import com.focusstudy.app.ui.theme.TextSecondary
import com.focusstudy.app.util.Constants
import com.focusstudy.app.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(
    viewModel: DebugViewModel,
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
                        text = "Diagnostics & Test",
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
                actions = {
                    IconButton(onClick = { viewModel.refreshDiagnostics() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = TextSecondary
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
                    text = "Verify raw Android usage statistics, database records, and test overlay behavior.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            item {
                DiagnosticCard(title = "System & Timezone") {
                    DiagnosticRow("Local Timezone", uiState.localTimeZone)
                    DiagnosticRow("Start of Day (00:00:00)", uiState.startOfDayFormatted)
                    DiagnosticRow("Current Time", uiState.currentTimeFormatted)
                }
            }

            item {
                DiagnosticCard(title = "Permissions Status") {
                    DiagnosticStatusRow("Usage Access", uiState.hasUsagePermission)
                    DiagnosticStatusRow("Accessibility Service", uiState.hasAccessibilityPermission)
                    DiagnosticStatusRow("Draw Overlays", uiState.canDrawOverlays)
                }
            }

            item {
                DiagnosticCard(title = "Live UsageStats Foreground Time") {
                    DiagnosticRow(
                        "Instagram",
                        "${DateUtils.formatDuration(uiState.instagramUsedMillis)} (${uiState.instagramUsedMillis} ms)"
                    )
                    DiagnosticRow(
                        "YouTube",
                        "${DateUtils.formatDuration(uiState.youtubeUsedMillis)} (${uiState.youtubeUsedMillis} ms)"
                    )
                }
            }

            item {
                DiagnosticCard(title = "Overlay Trigger Tests") {
                    Text(
                        text = "Manually trigger the full-screen blocking overlay to test appearance and dismiss actions:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { viewModel.testTriggerOverlay(Constants.PACKAGE_INSTAGRAM, "Instagram") },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Test Instagram")
                        }
                        Button(
                            onClick = { viewModel.testTriggerOverlay(Constants.PACKAGE_YOUTUBE, "YouTube") },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Test YouTube")
                        }
                    }
                }
            }

            item {
                DiagnosticCard(title = "Room Database Records") {
                    Text(
                        text = "App Limits (${uiState.limitsInDb.size}):",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                    uiState.limitsInDb.forEach { limit ->
                        DiagnosticRow("• ${limit.appName}", "${limit.dailyLimitMinutes} min (Enabled: ${limit.isEnabled})")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Today's Usage Records (${uiState.usageRecordsInDb.size}):",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary
                    )
                    if (uiState.usageRecordsInDb.isEmpty()) {
                        Text("No cached records in DB yet", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    } else {
                        uiState.usageRecordsInDb.forEach { rec ->
                            DiagnosticRow("• ${rec.packageName}", "${rec.minutesUsed} min (${rec.date})")
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun DiagnosticCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun DiagnosticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, style = MaterialTheme.typography.labelLarge, color = TextPrimary)
    }
}

@Composable
private fun DiagnosticStatusRow(label: String, isGranted: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(
            text = if (isGranted) "GRANTED" else "NOT GRANTED",
            style = MaterialTheme.typography.labelLarge,
            color = if (isGranted) AccentEmerald else DangerRuby
        )
    }
}
