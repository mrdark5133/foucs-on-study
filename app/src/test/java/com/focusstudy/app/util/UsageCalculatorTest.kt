package com.focusstudy.app.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UsageCalculatorTest {

    @Test
    fun testIsLimitExceeded() {
        assertFalse(UsageCalculator.isLimitExceeded(usedMinutes = 10, limitMinutes = 30))
        assertFalse(UsageCalculator.isLimitExceeded(usedMinutes = 29, limitMinutes = 30))
        assertTrue(UsageCalculator.isLimitExceeded(usedMinutes = 30, limitMinutes = 30))
        assertTrue(UsageCalculator.isLimitExceeded(usedMinutes = 35, limitMinutes = 30))
        assertFalse(UsageCalculator.isLimitExceeded(usedMinutes = 10, limitMinutes = 0))
    }

    @Test
    fun testCalculateRemainingMinutes() {
        assertEquals(20, UsageCalculator.calculateRemainingMinutes(usedMinutes = 10, limitMinutes = 30))
        assertEquals(1, UsageCalculator.calculateRemainingMinutes(usedMinutes = 29, limitMinutes = 30))
        assertEquals(0, UsageCalculator.calculateRemainingMinutes(usedMinutes = 30, limitMinutes = 30))
        assertEquals(0, UsageCalculator.calculateRemainingMinutes(usedMinutes = 50, limitMinutes = 30))
    }

    @Test
    fun testWarningThreshold() {
        // Limit: 60 min. 80% threshold is 48 min.
        assertFalse(UsageCalculator.isWarningThresholdReached(usedMinutes = 30, limitMinutes = 60))
        assertFalse(UsageCalculator.isWarningThresholdReached(usedMinutes = 45, limitMinutes = 60))
        assertTrue(UsageCalculator.isWarningThresholdReached(usedMinutes = 48, limitMinutes = 60))
        assertTrue(UsageCalculator.isWarningThresholdReached(usedMinutes = 56, limitMinutes = 60)) // <= 5 min remaining
        assertTrue(UsageCalculator.isWarningThresholdReached(usedMinutes = 60, limitMinutes = 60)) // exceeded
    }

    @Test
    fun testCalculateProgressFraction() {
        assertEquals(0.0f, UsageCalculator.calculateProgressFraction(usedMinutes = 0, limitMinutes = 30), 0.001f)
        assertEquals(0.5f, UsageCalculator.calculateProgressFraction(usedMinutes = 15, limitMinutes = 30), 0.001f)
        assertEquals(1.0f, UsageCalculator.calculateProgressFraction(usedMinutes = 30, limitMinutes = 30), 0.001f)
        assertEquals(1.0f, UsageCalculator.calculateProgressFraction(usedMinutes = 45, limitMinutes = 30), 0.001f) // clamped to 1.0
    }

    @Test
    fun testCalculateDurationFromEvents_completedSessions() {
        val baseTime = 1_000_000L
        val events = listOf(
            // Session 1: 10 seconds
            RawUsageEvent(RawUsageEvent.TYPE_ACTIVITY_RESUMED, baseTime + 10_000L, "com.instagram.android"),
            RawUsageEvent(RawUsageEvent.TYPE_ACTIVITY_PAUSED, baseTime + 20_000L, "com.instagram.android"),
            // Session 2: 30 seconds
            RawUsageEvent(RawUsageEvent.TYPE_ACTIVITY_RESUMED, baseTime + 50_000L, "com.instagram.android"),
            RawUsageEvent(RawUsageEvent.TYPE_ACTIVITY_STOPPED, baseTime + 80_000L, "com.instagram.android")
        )

        val duration = UsageCalculator.calculateDurationFromEvents(
            events = events,
            startTime = baseTime,
            endTime = baseTime + 100_000L
        )

        assertEquals(40_000L, duration) // 10s + 30s = 40,000 ms
    }

    @Test
    fun testCalculateDurationFromEvents_ongoingSession() {
        val baseTime = 1_000_000L
        val events = listOf(
            // Session 1: Started at +10s and still open at +60s
            RawUsageEvent(RawUsageEvent.TYPE_ACTIVITY_RESUMED, baseTime + 10_000L, "com.google.android.youtube")
        )

        val duration = UsageCalculator.calculateDurationFromEvents(
            events = events,
            startTime = baseTime,
            endTime = baseTime + 60_000L
        )

        assertEquals(50_000L, duration) // 60s - 10s = 50,000 ms
    }

    @Test
    fun testCalculateDurationFromEvents_emptyEvents() {
        val duration = UsageCalculator.calculateDurationFromEvents(
            events = emptyList(),
            startTime = 0L,
            endTime = 100_000L
        )
        assertEquals(0L, duration)
    }
}
