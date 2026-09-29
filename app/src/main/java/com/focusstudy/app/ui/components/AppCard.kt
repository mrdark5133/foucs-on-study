package com.focusstudy.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.focusstudy.app.data.model.TrackedAppInfo
import com.focusstudy.app.ui.theme.AccentEmerald
import com.focusstudy.app.ui.theme.BorderColor
import com.focusstudy.app.ui.theme.DangerRuby
import com.focusstudy.app.ui.theme.InstagramGradientEnd
import com.focusstudy.app.ui.theme.InstagramGradientMiddle
import com.focusstudy.app.ui.theme.InstagramGradientStart
import com.focusstudy.app.ui.theme.SurfaceDark
import com.focusstudy.app.ui.theme.SurfaceElevated
import com.focusstudy.app.ui.theme.TextMuted
import com.focusstudy.app.ui.theme.TextPrimary
import com.focusstudy.app.ui.theme.TextSecondary
import com.focusstudy.app.ui.theme.WarningAmber
import com.focusstudy.app.ui.theme.YouTubeRed
import com.focusstudy.app.util.Constants
import com.focusstudy.app.util.DateUtils

@Composable
fun AppCard(
    appInfo: TrackedAppInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        appInfo.isLimitExceeded -> DangerRuby.copy(alpha = 0.6f)
        appInfo.isWarning -> WarningAmber.copy(alpha = 0.5f)
        else -> BorderColor
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIconBadge(packageName = appInfo.packageName, appName = appInfo.appName)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = appInfo.appName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = if (!appInfo.isInstalled) {
                            "Not installed on device"
                        } else if (appInfo.isEnabled) {
                            "Daily Limit: ${DateUtils.formatMinutes(appInfo.dailyLimitMinutes)}"
                        } else {
                            "Tracking Disabled"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            StatusBadge(appInfo = appInfo)
        }

        if (appInfo.isEnabled && appInfo.isInstalled) {
            Spacer(modifier = Modifier.height(16.dp))

            UsageProgressBar(
                progress = appInfo.progressFraction,
                isWarning = appInfo.isWarning,
                isExceeded = appInfo.isLimitExceeded,
                height = 8.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Used: ${DateUtils.formatMinutes(appInfo.usedMinutesToday)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = if (appInfo.isLimitExceeded) {
                        "Limit Reached"
                    } else {
                        "${DateUtils.formatMinutes(appInfo.remainingMinutes)} remaining"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = when {
                        appInfo.isLimitExceeded -> DangerRuby
                        appInfo.isWarning -> WarningAmber
                        else -> AccentEmerald
                    }
                )
            }
        }
    }
}

@Composable
fun AppIconBadge(
    packageName: String,
    appName: String,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = when (packageName) {
        Constants.PACKAGE_INSTAGRAM -> Brush.linearGradient(
            listOf(InstagramGradientStart, InstagramGradientMiddle, InstagramGradientEnd)
        )
        Constants.PACKAGE_YOUTUBE -> Brush.linearGradient(
            listOf(YouTubeRed, Color(0xFFCC0000))
        )
        else -> Brush.linearGradient(
            listOf(Color(0xFF6366F1), Color(0xFF8B5CF6))
        )
    }

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        when (packageName) {
            Constants.PACKAGE_YOUTUBE -> {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = appName,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            else -> {
                Text(
                    text = appName.firstOrNull()?.uppercase() ?: "A",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(appInfo: TrackedAppInfo) {
    when {
        !appInfo.isInstalled -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Uninstalled",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
        !appInfo.isEnabled -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Off",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
        appInfo.isLimitExceeded -> {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(DangerRuby.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Block,
                    contentDescription = "Blocked",
                    tint = DangerRuby,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Blocked",
                    style = MaterialTheme.typography.labelSmall,
                    color = DangerRuby
                )
            }
        }
        appInfo.isWarning -> {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarningAmber.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = WarningAmber,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Near Limit",
                    style = MaterialTheme.typography.labelSmall,
                    color = WarningAmber
                )
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentEmerald.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentEmerald
                )
            }
        }
    }
}
