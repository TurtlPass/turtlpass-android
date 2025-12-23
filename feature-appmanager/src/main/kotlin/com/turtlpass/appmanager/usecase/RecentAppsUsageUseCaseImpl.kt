package com.turtlpass.appmanager.usecase

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.model.toUi
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import javax.inject.Inject

class RecentAppsUsageUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val usageStatsManager: UsageStatsManager,
    private val packageManager: PackageManager,
    private val retrieveInstalledAppUseCase: RetrieveInstalledAppUseCase,
    @param:ApplicationContext private val context: Context,
) : RecentAppsUsageUseCase {
    override operator fun invoke(
        minUsageSeconds: Long,
        limit: Int,
        refreshIntervalMs: Long,
    ): Flow<Result<List<InstalledAppUi>>> {
        return flow {
            emit(Result.Loading)

            var lastEmitted: List<InstalledAppUi>? = null
            val minUsageMillis = minUsageSeconds * 1000L
            val ourPackage = context.packageName

            while (currentCoroutineContext().isActive) {

                try {
                    val endTime = System.currentTimeMillis()
                    val startTime = endTime - (1000L * 60 * 60 * 24 * 7) // last 7 days

                    val stats = usageStatsManager.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        startTime,
                        endTime
                    )

                    if (!stats.isNullOrEmpty()) {

                        val apps = stats
                            .filter { it.packageName != ourPackage }
                            .filter { it.lastTimeUsed > 0 }
                            .filter { it.totalTimeInForeground >= minUsageMillis }
                            .sortedByDescending { it.lastTimeUsed }
                            .distinctBy { it.packageName }
                            .mapNotNull { usage ->
                                try {
                                    val appInfo =
                                        packageManager.getApplicationInfo(usage.packageName, 0)
                                    retrieveInstalledAppUseCase(appInfo)?.toUi()
                                } catch (_: Exception) {
                                    null
                                }
                            }
                            .take(limit)

                        // Only emit if changed
                        if (apps != lastEmitted) {
                            emit(Result.Success(apps))
                            lastEmitted = apps
                        }
                    } else {
                        emit(Result.Error())
                    }

                } catch (e: Exception) {
                    emit(Result.Error(e))
                }

                delay(refreshIntervalMs)
            }

        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
