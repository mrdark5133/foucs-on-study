package com.focusstudy.app.util

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log

class UsageStatsHelper(private val context: Context) {

    private val usageStatsManager by lazy {
        context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    }

    /**
     * Gets today's total foreground duration in milliseconds for the given package.
     */
    fun getTodayForegroundDurationMillis(packageName: String): Long {
        val startOfDay = DateUtils.getStartOfDayMillis()
        val now = System.currentTimeMillis()
        return getForegroundDurationMillis(packageName, startOfDay, now)
    }

    /**
     * Gets today's total foreground duration in whole minutes for the given package.
     */
    fun getTodayForegroundMinutes(packageName: String): Int {
        val millis = getTodayForegroundDurationMillis(packageName)
        return UsageCalculator.millisToMinutes(millis)
    }

    /**
     * Queries foreground duration between [startTime, endTime] for [packageName].
     * First attempts precise Event calculation via [queryEvents], then falls back to [queryUsageStats].
     */
    fun getForegroundDurationMillis(packageName: String, startTime: Long, endTime: Long): Long {
        val manager = usageStatsManager ?: return 0L
        if (!PermissionHelper.hasUsageStatsPermission(context)) return 0L

        return try {
            val durationFromEvents = calculateFromUsageEvents(manager, packageName, startTime, endTime)
            if (durationFromEvents > 0L) {
                durationFromEvents
            } else {
                calculateFromAggregateStats(manager, packageName, startTime, endTime)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error querying usage stats for $packageName: ${e.message}")
            0L
        }
    }

    /**
     * Calculates foreground duration by aggregating UsageEvents (most accurate for today's sessions).
     */
    private fun calculateFromUsageEvents(
        manager: UsageStatsManager,
        targetPackage: String,
        startTime: Long,
        endTime: Long
    ): Long {
        val events = manager.queryEvents(startTime, endTime) ?: return 0L
        val rawEventsList = mutableListOf<RawUsageEvent>()

        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.packageName == targetPackage) {
                rawEventsList.add(
                    RawUsageEvent(
                        eventType = event.eventType,
                        timeStamp = event.timeStamp,
                        packageName = event.packageName
                    )
                )
            }
        }

        return UsageCalculator.calculateDurationFromEvents(rawEventsList, startTime, endTime)
    }

    /**
     * Fallback to queryUsageStats if queryEvents yields 0 or is restricted.
     */
    private fun calculateFromAggregateStats(
        manager: UsageStatsManager,
        targetPackage: String,
        startTime: Long,
        endTime: Long
    ): Long {
        val statsList = manager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        ) ?: return 0L

        return statsList
            .filter { it.packageName == targetPackage }
            .sumOf { it.totalTimeInForeground }
    }

    companion object {
        private const val TAG = "UsageStatsHelper"
    }
}
