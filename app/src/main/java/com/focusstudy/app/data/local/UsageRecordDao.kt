package com.focusstudy.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.focusstudy.app.data.model.UsageRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageRecordDao {

    @Query("SELECT * FROM usage_records WHERE date = :date")
    fun getRecordsForDateFlow(date: String): Flow<List<UsageRecord>>

    @Query("SELECT * FROM usage_records WHERE date = :date")
    suspend fun getRecordsForDate(date: String): List<UsageRecord>

    @Query("SELECT * FROM usage_records WHERE packageName = :packageName AND date = :date LIMIT 1")
    suspend fun getRecord(packageName: String, date: String): UsageRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecord(record: UsageRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecords(records: List<UsageRecord>)

    @Query("DELETE FROM usage_records WHERE date < :beforeDate")
    suspend fun deleteOldRecords(beforeDate: String)
}
