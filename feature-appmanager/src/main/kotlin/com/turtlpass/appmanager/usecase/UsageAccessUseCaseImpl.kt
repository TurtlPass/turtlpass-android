package com.turtlpass.appmanager.usecase

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import javax.inject.Inject

class UsageAccessUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val appOpsManager: AppOpsManager,
    private val usageStatsManager: UsageStatsManager,
    private val context: Context,
) : UsageAccessUseCase {
    override operator fun invoke(): Flow<Result<Boolean>> {
        return flow {
            emit(Result.Loading)

            while (currentCoroutineContext().isActive) {

                val granted = checkUsageStatsAccess()
                emit(Result.Success(granted))

                if (granted) {
                    // Permission granted - stop polling
                    break
                }

                delay(1000) // poll every second works consistently across OEM devices
                // bc AppOpsManager.OnOpChangedListener is notoriously unreliable
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }

    private fun checkUsageStatsAccess(): Boolean {
        val appOpsGranted = try {
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }

        val statsGranted = try {
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                System.currentTimeMillis() - 60_000,
                System.currentTimeMillis()
            )
            !stats.isNullOrEmpty()
        } catch (e: Exception) {
            false
        }

        return appOpsGranted && statsGranted
    }
}
