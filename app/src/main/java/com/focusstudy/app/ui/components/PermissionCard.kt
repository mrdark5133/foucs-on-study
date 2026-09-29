package com.focusstudy.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.focusstudy.app.ui.theme.AccentEmerald
import com.focusstudy.app.ui.theme.BorderColor
import com.focusstudy.app.ui.theme.PrimaryIndigo
import com.focusstudy.app.ui.theme.SurfaceDark
import com.focusstudy.app.ui.theme.TextMuted
import com.focusstudy.app.ui.theme.TextPrimary
import com.focusstudy.app.ui.theme.TextSecondary
import com.focusstudy.app.ui.theme.WarningAmber

@Composable
fun PermissionCard(
    hasUsagePermission: Boolean,
    hasAccessibilityPermission: Boolean,
    onGrantUsageClick: () -> Unit,
    onGrantAccessibilityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (hasUsagePermission && hasAccessibilityPermission) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, WarningAmber.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Permission Alert",
                tint = WarningAmber,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Permissions Required",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Study Focus needs system permissions to accurately monitor usage and protect your concentration.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Usage Access Permission Row
        PermissionItemRow(
            title = "Usage Access",
            description = "Tracks foreground time for Instagram & YouTube",
            isGranted = hasUsagePermission,
            onGrantClick = onGrantUsageClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Accessibility Service Permission Row
        PermissionItemRow(
            title = "Accessibility Service",
            description = "Enforces limits when target apps are opened",
            isGranted = hasAccessibilityPermission,
            onGrantClick = onGrantAccessibilityClick
        )
    }
}

@Composable
private fun PermissionItemRow(
    title: String,
    description: String,
    isGranted: Boolean,
    onGrantClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (isGranted) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Granted",
                tint = AccentEmerald,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Grant", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
