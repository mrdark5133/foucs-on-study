package com.focusstudy.app.data.repository

import com.focusstudy.app.data.local.AppLimitDao
import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.util.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LimitRepositoryTest {

    private val mockDao = mockk<AppLimitDao>(relaxed = true)
    private lateinit var repository: LimitRepository

    @Before
    fun setUp() {
        repository = LimitRepositoryImpl(mockDao)
    }

    @Test
    fun testUpdateLimit_persistsCorrectValues() = runTest {
        coEvery { mockDao.getLimit(Constants.PACKAGE_INSTAGRAM) } returns AppLimit(
            packageName = Constants.PACKAGE_INSTAGRAM,
            appName = "Instagram",
            dailyLimitMinutes = 30,
            isEnabled = true
        )

        val slot = slot<AppLimit>()
        coEvery { mockDao.upsertLimit(capture(slot)) } returns Unit

        repository.updateLimit(
            packageName = Constants.PACKAGE_INSTAGRAM,
            minutes = 45,
            isEnabled = false
        )

        coVerify { mockDao.upsertLimit(any()) }
        assertEquals(Constants.PACKAGE_INSTAGRAM, slot.captured.packageName)
        assertEquals("Instagram", slot.captured.appName)
        assertEquals(45, slot.captured.dailyLimitMinutes)
        assertFalse(slot.captured.isEnabled)
    }

    @Test
    fun testUpdateLimit_clampsLimitsToValidRange() = runTest {
        coEvery { mockDao.getLimit(Constants.PACKAGE_YOUTUBE) } returns null

        val slot = slot<AppLimit>()
        coEvery { mockDao.upsertLimit(capture(slot)) } returns Unit

        // Test below minimum
        repository.updateLimit(
            packageName = Constants.PACKAGE_YOUTUBE,
            minutes = -5,
            isEnabled = true
        )
        assertEquals(Constants.MIN_LIMIT_MINUTES, slot.captured.dailyLimitMinutes)

        // Test above maximum
        repository.updateLimit(
            packageName = Constants.PACKAGE_YOUTUBE,
            minutes = 1000,
            isEnabled = true
        )
        assertEquals(Constants.MAX_LIMIT_MINUTES, slot.captured.dailyLimitMinutes)
    }

    @Test
    fun testEnsureDefaultLimits_insertsWhenEmpty() = runTest {
        coEvery { mockDao.getAllLimits() } returns emptyList()

        repository.ensureDefaultLimits()

        coVerify { mockDao.insertLimitsIfNotExist(any()) }
    }
}
