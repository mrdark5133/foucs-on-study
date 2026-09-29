package com.focusstudy.app.data.repository

import com.focusstudy.app.data.local.AppLimitDao
import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.util.Constants
import kotlinx.coroutines.flow.Flow

interface LimitRepository {
    fun getAllLimitsFlow(): Flow<List<AppLimit>>
    suspend fun getAllLimits(): List<AppLimit>
    suspend fun getLimit(packageName: String): AppLimit?
    fun getLimitFlow(packageName: String): Flow<AppLimit?>
    suspend fun ensureDefaultLimits()
    suspend fun updateLimit(packageName: String, minutes: Int, isEnabled: Boolean)
}

class LimitRepositoryImpl(
    private val appLimitDao: AppLimitDao
) : LimitRepository {

    override fun getAllLimitsFlow(): Flow<List<AppLimit>> = appLimitDao.getAllLimitsFlow()

    override suspend fun getAllLimits(): List<AppLimit> = appLimitDao.getAllLimits()

    override suspend fun getLimit(packageName: String): AppLimit? = appLimitDao.getLimit(packageName)

    override fun getLimitFlow(packageName: String): Flow<AppLimit?> = appLimitDao.getLimitFlow(packageName)

    override suspend fun ensureDefaultLimits() {
        val existing = appLimitDao.getAllLimits()
        if (existing.isEmpty()) {
            val defaults = Constants.DEFAULT_TRACKED_PACKAGES.map { (pkg, name) ->
                AppLimit(
                    packageName = pkg,
                    appName = name,
                    dailyLimitMinutes = Constants.DEFAULT_LIMIT_MINUTES,
                    isEnabled = true
                )
            }
            appLimitDao.insertLimitsIfNotExist(defaults)
        }
    }

    /**
     * Centralized function for modifying limits.
     * In V1, changes are persisted directly to the local Room database.
     * In V2, this single entry point will be wrapped with parent/guardian approval validation.
     */
    override suspend fun updateLimit(packageName: String, minutes: Int, isEnabled: Boolean) {
        val clampedMinutes = minutes.coerceIn(Constants.MIN_LIMIT_MINUTES, Constants.MAX_LIMIT_MINUTES)
        val existing = appLimitDao.getLimit(packageName)
        val appName = existing?.appName ?: Constants.DEFAULT_TRACKED_PACKAGES[packageName] ?: packageName

        val updated = AppLimit(
            packageName = packageName,
            appName = appName,
            dailyLimitMinutes = clampedMinutes,
            isEnabled = isEnabled
        )
        appLimitDao.upsertLimit(updated)
    }
}
