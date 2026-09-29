package com.focusstudy.app.data.model

data class TrackedAppInfo(
    val packageName: String,
    val appName: String,
    val dailyLimitMinutes: Int,
    val isEnabled: Boolean,
    val usedMinutesToday: Int,
    val usedMillisToday: Long = 0L,
    val isInstalled: Boolean = true
) {
    val remainingMinutes: Int
        get() = if (isEnabled) (dailyLimitMinutes - usedMinutesToday).coerceAtLeast(0) else 0

    val isLimitExceeded: Boolean
        get() = isEnabled && dailyLimitMinutes > 0 && usedMinutesToday >= dailyLimitMinutes

    val isWarning: Boolean
        get() = isEnabled && dailyLimitMinutes > 0 && !isLimitExceeded &&
                (usedMinutesToday >= (dailyLimitMinutes * 0.8f).toInt() || remainingMinutes <= 5)

    val progressFraction: Float
        get() = if (dailyLimitMinutes > 0) (usedMinutesToday.toFloat() / dailyLimitMinutes.toFloat()).coerceIn(0f, 1f) else 0f
}
