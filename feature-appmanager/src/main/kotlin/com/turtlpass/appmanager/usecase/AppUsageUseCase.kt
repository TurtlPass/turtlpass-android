package com.turtlpass.appmanager.usecase

import android.app.usage.UsageStatsManager
import com.turtlpass.appmanager.model.UsageTime
import java.util.Calendar
import javax.inject.Inject

class AppUsageUseCase @Inject constructor(
    private val usageStatsManager: UsageStatsManager,
) {
    operator fun invoke(beginTime: Long): MutableList<UsageTime> {
        return usageStatsManager.getAppsUsageTime(beginTime)
    }
}

fun UsageStatsManager.getAppsUsageTime(beginTime: Long): MutableList<UsageTime> {
    val endTime = Calendar.getInstance().timeInMillis
    return queryUsageStats(
        /* intervalType = */ UsageStatsManager.INTERVAL_BEST,
        /* beginTime = */ beginTime,
        /* endTime = */ endTime
    ).filter { usageStats ->
        usageStats.totalTimeInForeground > 0 && usageStats.lastTimeStamp in beginTime..endTime
    }.map {
        UsageTime(
            packageName = it.packageName,
            usageTime = it.totalTimeInForeground.convertMinute(),
        )
    }.toMutableList()
}

fun Long.convertMinute(): Int {
    val startTime = (Calendar.getInstance().timeInMillis - this) / 1000
    val currentDate = Calendar.getInstance().timeInMillis / 1000
    val difference = (currentDate - startTime)
    return if (difference < 120) {
        1
    } else {
        (difference / 60).toInt()
    }
}