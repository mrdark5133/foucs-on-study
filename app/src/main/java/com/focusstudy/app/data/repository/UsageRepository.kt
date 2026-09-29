package com.focusstudy.app.data.repository

import android.content.Context
import android.content.pm.PackageManager
import com.focusstudy.app.data.local.UsageRecordDao
import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.data.model.TrackedAppInfo
import com.focusstudy.app.data.model.UsageRecord
import com.focusstudy.app.util.DateUtils
import com.focusstudy.app.util.UsageStatsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

interface UsageRepository {
    fun getTrackedAppsInfoFlow(): Flow<List<TrackedAppInfo>>
    suspend fun getTrackedAppsInfo(): List<TrackedAppInfo>
    suspend fun getAppUsageInfo(packageName: String): TrackedAppInfo?
    suspend fun refreshTodayUsage(): List<TrackedAppInfo>
    suspend fun isAppLimitReached(packageName: String): Boolean
}

class UsageRepositoryImpl(
    private val context: Context,
    private val limitRepository: LimitRepository,
    private val usageRecordDao: UsageRecordDao,
    private val usageStatsHelper: UsageStatsHelper
) : UsageRepository {

    override fun getTrackedAppsInfoFlow(): Flow<List<TrackedAppInfo>> {
        val todayStr = DateUtils.getFormattedDateString()
        return combine(
            limitRepository.getAllLimitsFlow(),
            usageRecordDao.getRecordsForDateFlow(todayStr)
        ) { limits, records ->
            val recordsMap = records.associateBy { it.packageName }
            limits.map { limit ->
                val cachedMinutes = recordsMap[limit.packageName]?.minutesUsed
                // Fetch live usage or fallback to cached
                val liveMillis = usageStatsHelper.getTodayForegroundDurationMillis(limit.packageName)
                val usedMinutes = if (liveMillis > 0L) {
                    (liveMillis / 60000L).toInt()
                } else {
                    cachedMinutes ?: 0
                }

                TrackedAppInfo(
                    packageName = limit.packageName,
                    appName = limit.appName,
                    dailyLimitMinutes = limit.dailyLimitMinutes,
                    isEnabled = limit.isEnabled,
                    usedMinutesToday = usedMinutes,
                    usedMillisToday = liveMillis,
                    isInstalled = isPackageInstalled(limit.packageName)
                )
            }
        }
    }

    override suspend fun getTrackedAppsInfo(): List<TrackedAppInfo> = withContext(Dispatchers.IO) {
        val todayStr = DateUtils.getFormattedDateString()
        val limits = limitRepository.getAllLimits()
        val records = usageRecordDao.getRecordsForDate(todayStr).associateBy { it.packageName }

        limits.map { limit ->
            val liveMillis = usageStatsHelper.getTodayForegroundDurationMillis(limit.packageName)
            val usedMinutes = if (liveMillis > 0L) {
                (liveMillis / 60000L).toInt()
            } else {
                records[limit.packageName]?.minutesUsed ?: 0
            }

            TrackedAppInfo(
                packageName = limit.packageName,
                appName = limit.appName,
                dailyLimitMinutes = limit.dailyLimitMinutes,
                isEnabled = limit.isEnabled,
                usedMinutesToday = usedMinutes,
                usedMillisToday = liveMillis,
                isInstalled = isPackageInstalled(limit.packageName)
            )
        }
    }

    override suspend fun getAppUsageInfo(packageName: String): TrackedAppInfo? = withContext(Dispatchers.IO) {
        val limit = limitRepository.getLimit(packageName) ?: return@withContext null
        val todayStr = DateUtils.getFormattedDateString()
        val liveMillis = usageStatsHelper.getTodayForegroundDurationMillis(packageName)
        val record = usageRecordDao.getRecord(packageName, todayStr)
        val usedMinutes = if (liveMillis > 0L) {
            (liveMillis / 60000L).toInt()
        } else {
            record?.minutesUsed ?: 0
        }

        TrackedAppInfo(
            packageName = limit.packageName,
            appName = limit.appName,
            dailyLimitMinutes = limit.dailyLimitMinutes,
            isEnabled = limit.isEnabled,
            usedMinutesToday = usedMinutes,
            usedMillisToday = liveMillis,
            isInstalled = isPackageInstalled(limit.packageName)
        )
    }

    override suspend fun refreshTodayUsage(): List<TrackedAppInfo> = withContext(Dispatchers.IO) {
        val todayStr = DateUtils.getFormattedDateString()
        val limits = limitRepository.getAllLimits()
        val now = System.currentTimeMillis()

        val updatedInfos = limits.map { limit ->
            val liveMillis = usageStatsHelper.getTodayForegroundDurationMillis(limit.packageName)
            val usedMinutes = (liveMillis / 60000L).toInt()

            // Save to Room for persistence across restarts
            usageRecordDao.upsertRecord(
                UsageRecord(
                    packageName = limit.packageName,
                    date = todayStr,
                    minutesUsed = usedMinutes,
                    lastUpdatedMillis = now
                )
            )

            TrackedAppInfo(
                packageName = limit.packageName,
                appName = limit.appName,
                dailyLimitMinutes = limit.dailyLimitMinutes,
                isEnabled = limit.isEnabled,
                usedMinutesToday = usedMinutes,
                usedMillisToday = liveMillis,
                isInstalled = isPackageInstalled(limit.packageName)
            )
        }

        updatedInfos
    }

    override suspend fun isAppLimitReached(packageName: String): Boolean = withContext(Dispatchers.IO) {
        val limit = limitRepository.getLimit(packageName) ?: return@withContext false
        if (!limit.isEnabled || limit.dailyLimitMinutes <= 0) return@withContext false

        val usedMinutes = usageStatsHelper.getTodayForegroundMinutes(packageName)
        usedMinutes >= limit.dailyLimitMinutes
    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}
