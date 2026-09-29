package com.focusstudy.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.focusstudy.app.data.model.AppLimit
import kotlinx.coroutines.flow.Flow

@Dao
interface AppLimitDao {

    @Query("SELECT * FROM app_limits")
    fun getAllLimitsFlow(): Flow<List<AppLimit>>

    @Query("SELECT * FROM app_limits")
    suspend fun getAllLimits(): List<AppLimit>

    @Query("SELECT * FROM app_limits WHERE packageName = :packageName LIMIT 1")
    suspend fun getLimit(packageName: String): AppLimit?

    @Query("SELECT * FROM app_limits WHERE packageName = :packageName LIMIT 1")
    fun getLimitFlow(packageName: String): Flow<AppLimit?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLimit(limit: AppLimit)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLimitsIfNotExist(limits: List<AppLimit>)

    @Update
    suspend fun updateLimit(limit: AppLimit)

    @Query("UPDATE app_limits SET dailyLimitMinutes = :minutes, isEnabled = :isEnabled WHERE packageName = :packageName")
    suspend fun updateLimitValues(packageName: String, minutes: Int, isEnabled: Boolean)
}
