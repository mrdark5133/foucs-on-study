package com.focusstudy.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.focusstudy.app.FocusApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            val app = context?.applicationContext as? FocusApplication ?: return
            CoroutineScope(Dispatchers.IO).launch {
                app.limitRepository.ensureDefaultLimits()
                app.usageRepository.refreshTodayUsage()
            }
        }
    }
}
