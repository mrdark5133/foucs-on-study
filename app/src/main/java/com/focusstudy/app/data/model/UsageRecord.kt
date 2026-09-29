package com.focusstudy.app.data.model

import androidx.room.Entity

@Entity(
    tableName = "usage_records",
    primaryKeys = ["packageName", "date"]
)
data class UsageRecord(
    val packageName: String,
    val date: String, // "yyyy-MM-dd"
    val minutesUsed: Int,
    val lastUpdatedMillis: Long
)
