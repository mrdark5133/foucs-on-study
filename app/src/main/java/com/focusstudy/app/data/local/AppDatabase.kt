package com.focusstudy.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.focusstudy.app.data.model.AppLimit
import com.focusstudy.app.data.model.UsageRecord
import com.focusstudy.app.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [AppLimit::class, UsageRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appLimitDao(): AppLimitDao
    abstract fun usageRecordDao(): UsageRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                Constants.DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Prepopulate default tracked apps
                        CoroutineScope(Dispatchers.IO).launch {
                            val defaultLimits = Constants.DEFAULT_TRACKED_PACKAGES.map { (pkg, name) ->
                                AppLimit(
                                    packageName = pkg,
                                    appName = name,
                                    dailyLimitMinutes = Constants.DEFAULT_LIMIT_MINUTES,
                                    isEnabled = true
                                )
                            }
                            getInstance(context).appLimitDao().insertLimitsIfNotExist(defaultLimits)
                        }
                    }
                })
                .build()
        }
    }
}
