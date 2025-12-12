package com.turtlpass.appmanager.usecase

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.model.toUi
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class InstalledAppsUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val deviceAppsRepository: DeviceAppsRepository,
    private val retrieveInstalledAppUseCase: RetrieveInstalledAppUseCase,
) {
    operator fun invoke(): Flow<Result<List<InstalledAppUi>>> {
        return flow {
            emit(Result.Loading)
            try {
                val appList: MutableList<InstalledAppUi> = mutableListOf()
                deviceAppsRepository.deviceInstalledApplications().forEach { appInfo ->
                    retrieveInstalledAppUseCase(appInfo)?.let { installedApp ->
                        appList.add(installedApp.toUi())
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
