package com.focusstudy.app.util

object Constants {
    const val PACKAGE_INSTAGRAM = "com.instagram.android"
    const val PACKAGE_YOUTUBE = "com.google.android.youtube"

    const val APP_NAME_INSTAGRAM = "Instagram"
    const val APP_NAME_YOUTUBE = "YouTube"

    const val DEFAULT_LIMIT_MINUTES = 30
    const val MIN_LIMIT_MINUTES = 1
    const val MAX_LIMIT_MINUTES = 720 // 12 hours

    const val DATABASE_NAME = "study_focus_db"

    // Initial default tracked apps configuration
    val DEFAULT_TRACKED_PACKAGES = mapOf(
        PACKAGE_INSTAGRAM to APP_NAME_INSTAGRAM,
        PACKAGE_YOUTUBE to APP_NAME_YOUTUBE
    )
}
