package com.focusstudy.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    /**
     * Returns the timestamp (in ms) of 00:00:00.000 for the given timestamp
     * in the device's local timezone.
     */
    fun getStartOfDayMillis(timestamp: Long = System.currentTimeMillis(), timeZone: TimeZone = TimeZone.getDefault()): Long {
        val calendar = Calendar.getInstance(timeZone).apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    /**
     * Returns the formatted date string "yyyy-MM-dd" for the given timestamp in the local timezone.
     */
    fun getFormattedDateString(timestamp: Long = System.currentTimeMillis(), timeZone: TimeZone = TimeZone.getDefault()): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            this.timeZone = timeZone
        }
        return sdf.format(Date(timestamp))
    }

    /**
     * Returns true if the two timestamps belong to different calendar days in the given timezone.
     */
    fun isDifferentDay(timestamp1: Long, timestamp2: Long, timeZone: TimeZone = TimeZone.getDefault()): Boolean {
        return getFormattedDateString(timestamp1, timeZone) != getFormattedDateString(timestamp2, timeZone)
    }

    /**
     * Formats minutes into human-readable format, e.g. "1 hr 15 min", "45 min", "0 min".
     */
    fun formatMinutes(minutes: Int): String {
        val safeMinutes = if (minutes < 0) 0 else minutes
        val hours = safeMinutes / 60
        val remainingMinutes = safeMinutes % 60
        return when {
            hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
            hours > 0 -> "${hours}h"
            else -> "${remainingMinutes}m"
        }
    }

    /**
     * Formats duration in milliseconds into a friendly string, e.g. "45m 12s".
     */
    fun formatDuration(durationMillis: Long): String {
        val totalSeconds = (durationMillis / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hours = minutes / 60
        val remainingMins = minutes % 60

        return when {
            hours > 0 -> "${hours}h ${remainingMins}m"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }
}
