package com.turtlpass.di.moduleNavigation

import android.content.Context
import android.content.Intent
import jakarta.inject.Inject
import timber.log.Timber

class ModuleNavigation @Inject constructor(
    private val activityClassList: List<ActivityClass>
) {
    fun buildIntent(context: Context, activityLabel: ActivityLabel): Intent? {
        return buildIntent(context, activityClassList.first {
            it.activityLabel == activityLabel
        }.className)
    }

    private fun buildIntent(context: Context, className: String): Intent? {
        return try {
            Intent(context, Class.forName(className))
        } catch (e: ClassNotFoundException) {
            Timber.e(e)
            null
        }
    }
}
