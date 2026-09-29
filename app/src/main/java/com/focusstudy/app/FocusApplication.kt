package com.focusstudy.app

import android.app.Application
import com.focusstudy.app.data.local.AppDatabase
import com.focusstudy.app.data.repository.LimitRepository
import com.focusstudy.app.data.repository.LimitRepositoryImpl
import com.focusstudy.app.data.repository.UsageRepository
import com.focusstudy.app.data.repository.UsageRepositoryImpl
import com.focusstudy.app.util.UsageStatsHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FocusApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val usageStatsHelper by lazy { UsageStatsHelper(this) }

    val limitRepository: LimitRepository by lazy {
        LimitRepositoryImpl(database.appLimitDao())
    }

    val usageRepository: UsageRepository by lazy {
        UsageRepositoryImpl(
            context = this,
            limitRepository = limitRepository,
            usageRecordDao = database.usageRecordDao(),
            usageStatsHelper = usageStatsHelper
        )
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            limitRepository.ensureDefaultLimits()
        }
    }
}
