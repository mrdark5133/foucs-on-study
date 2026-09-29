package com.focusstudy.app.util

data class RawUsageEvent(
    val eventType: Int,
    val timeStamp: Long,
    val packageName: String
) {
    companion object {
        const val TYPE_ACTIVITY_RESUMED = 1 // UsageEvents.Event.ACTIVITY_RESUMED / MOVE_TO_FOREGROUND
        const val TYPE_ACTIVITY_PAUSED = 2  // UsageEvents.Event.ACTIVITY_PAUSED / MOVE_TO_BACKGROUND
        const val TYPE_ACTIVITY_STOPPED = 23 // UsageEvents.Event.ACTIVITY_STOPPED
    }
}

object UsageCalculator {

    /**
     * Calculates total foreground duration in milliseconds from a sequence of sorted usage events
     * within [startTime, endTime].
     *
     * Handles ongoing sessions (where the app is currently in foreground at endTime).
     */
    fun calculateDurationFromEvents(
        events: List<RawUsageEvent>,
        startTime: Long,
        endTime: Long
    ): Long {
        if (events.isEmpty() || startTime >= endTime) return 0L

        var totalForegroundDuration = 0L
        var lastResumeTimestamp: Long? = null

        val sortedEvents = events.sortedBy { it.timeStamp }

        for (event in sortedEvents) {
            val eventTime = event.timeStamp.coerceIn(startTime, endTime)

            when (event.eventType) {
                RawUsageEvent.TYPE_ACTIVITY_RESUMED -> {
                    if (lastResumeTimestamp == null) {
                        lastResumeTimestamp = eventTime
                    }
                }
                RawUsageEvent.TYPE_ACTIVITY_PAUSED,
                RawUsageEvent.TYPE_ACTIVITY_STOPPED -> {
                    lastResumeTimestamp?.let { resumeTime ->
                        val duration = (eventTime - resumeTime).coerceAtLeast(0)
                        totalForegroundDuration += duration
                        lastResumeTimestamp = null
                    }
                }
            }
        }

        // If the app was resumed and never closed before endTime, add the open session
        lastResumeTimestamp?.let { resumeTime ->
            val duration = (endTime - resumeTime).coerceAtLeast(0)
            totalForegroundDuration += duration
        }

        return totalForegroundDuration
    }

    /**
     * Converts milliseconds to rounded whole minutes.
     */
    fun millisToMinutes(millis: Long): Int {
        return (millis / 60000L).toInt().coerceAtLeast(0)
    }

    /**
     * Calculates remaining minutes before limit is reached.
     */
    fun calculateRemainingMinutes(usedMinutes: Int, limitMinutes: Int): Int {
        if (limitMinutes <= 0) return 0
        return (limitMinutes - usedMinutes).coerceAtLeast(0)
    }

    /**
     * Returns true if usage has reached or exceeded the configured limit.
     */
    fun isLimitExceeded(usedMinutes: Int, limitMinutes: Int): Boolean {
        if (limitMinutes <= 0) return false
        return usedMinutes >= limitMinutes
    }

    /**
     * Returns true if usage is near the limit (e.g. >= 80% of limit or <= 5 mins remaining).
     */
    fun isWarningThresholdReached(
        usedMinutes: Int,
        limitMinutes: Int,
        warningRatio: Float = 0.8f
    ): Boolean {
        if (limitMinutes <= 0) return false
        if (isLimitExceeded(usedMinutes, limitMinutes)) return true
        val thresholdMinutes = (limitMinutes * warningRatio).toInt()
        val remainingMinutes = calculateRemainingMinutes(usedMinutes, limitMinutes)
        return usedMinutes >= thresholdMinutes || remainingMinutes <= 5
    }

    /**
     * Calculates usage progress fraction (0.0f to 1.0f).
     */
    fun calculateProgressFraction(usedMinutes: Int, limitMinutes: Int): Float {
        if (limitMinutes <= 0) return 0f
        return (usedMinutes.toFloat() / limitMinutes.toFloat()).coerceIn(0f, 1f)
    }
}
