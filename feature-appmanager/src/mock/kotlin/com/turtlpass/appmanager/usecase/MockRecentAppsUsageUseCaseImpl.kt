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

class MockRecentAppsUsageUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : RecentAppsUsageUseCase {
    override operator fun invoke(
        minUsageSeconds: Long,
        limit: Int,
        refreshIntervalMs: Long
    ): Flow<Result<List<InstalledAppUi>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(mockRecentApps()))
    }.flowOn(dispatcher)

    private fun mockRecentApps(): List<InstalledAppUi> = listOf(
        InstalledAppUi(
            appName = "TikTok",
            packageName = "com.zhiliaoapp.musically",
            topLevelDomain = "zhiliaoapp"
        ),
        InstalledAppUi(
            appName = "Telegram",
            packageName = "org.telegram.messenger.web",
            topLevelDomain = "telegram"
        ),
        InstalledAppUi(
            appName = "K-9 Mail",
            packageName = "com.fsck.k9",
            topLevelDomain = "fsck"
        ),
        InstalledAppUi(
            appName = "Tor Browser",
            packageName = "org.torproject.torbrowser",
            topLevelDomain = "torproject"
        ),
    )
}
