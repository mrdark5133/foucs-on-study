package com.focusstudy.app.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DateUtilsTest {

    @Test
    fun testGetStartOfDayMillis() {
        val tz = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance(tz).apply {
            set(2026, Calendar.SEPTEMBER, 23, 14, 35, 20)
            set(Calendar.MILLISECOND, 500)
        }
        val timestamp = calendar.timeInMillis

        val startOfDay = DateUtils.getStartOfDayMillis(timestamp, tz)

        val expectedCalendar = Calendar.getInstance(tz).apply {
            set(2026, Calendar.SEPTEMBER, 23, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        assertEquals(expectedCalendar.timeInMillis, startOfDay)
    }

    @Test
    fun testIsDifferentDayAcrossMidnight() {
        val tz = TimeZone.getTimeZone("UTC")
        val day1End = Calendar.getInstance(tz).apply {
            set(2026, Calendar.SEPTEMBER, 23, 23, 59, 59)
        }.timeInMillis

        val day2Start = Calendar.getInstance(tz).apply {
            set(2026, Calendar.SEPTEMBER, 24, 0, 0, 1)
        }.timeInMillis

        val sameDayLater = Calendar.getInstance(tz).apply {
            set(2026, Calendar.SEPTEMBER, 23, 12, 0, 0)
        }.timeInMillis

        assertTrue(DateUtils.isDifferentDay(day1End, day2Start, tz))
        assertFalse(DateUtils.isDifferentDay(day1End, sameDayLater, tz))
    }

    @Test
    fun testFormatMinutes() {
        assertEquals("0m", DateUtils.formatMinutes(0))
        assertEquals("45m", DateUtils.formatMinutes(45))
        assertEquals("1h", DateUtils.formatMinutes(60))
        assertEquals("1h 15m", DateUtils.formatMinutes(75))
        assertEquals("2h 30m", DateUtils.formatMinutes(150))
    }

    @Test
    fun testFormatDuration() {
        assertEquals("45s", DateUtils.formatDuration(45_000L))
        assertEquals("12m 30s", DateUtils.formatDuration(750_000L))
        assertEquals("1h 15m", DateUtils.formatDuration(4_500_000L))
    }
}
