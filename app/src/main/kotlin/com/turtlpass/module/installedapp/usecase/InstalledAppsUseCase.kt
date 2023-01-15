package com.turtlpass.module.installedapp.usecase

import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.accessibility.usecase.RetrieveInstalledAppUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InstalledAppsUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val deviceAppsRepository: DeviceAppsRepository,
    private val retrieveInstalledAppUseCase: RetrieveInstalledAppUseCase,
) {
    operator fun invoke(): Flow<Result<List<InstalledApp>>> {
        return flow {
            emit(Result.Loading)
            try {
                val appList: MutableList<InstalledApp> = mutableListOf()
                deviceAppsRepository.deviceInstalledApplications().forEach { appInfo ->
                    retrieveInstalledAppUseCase(appInfo)?.let { installedApp ->
                        appList.add(installedApp)
                    }
                }
                if (appList.isNotEmpty()) {
                    val appListSortedByName = appList.sortedBy {
                        it.appName.uppercase()
                    }
                    emit(Result.Success(appListSortedByName))
                } else {
                    emit(Result.Error())
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }
}
