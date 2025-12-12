package com.turtlpass.urlmanager.usecase

import com.turtlpass.appmanager.usecase.GetAppNameUseCase
import com.turtlpass.db.entities.WebsiteEventEntity
import com.turtlpass.db.repository.WebsiteRepository
import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.extension.faviconUrl
import com.turtlpass.urlmanager.extension.formatTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class WebsiteUpdatesUseCase @Inject constructor(
    private val repository: WebsiteRepository,
    private val getAppNameUseCase: GetAppNameUseCase,
    private val extractUrlTopLevelDomainUseCase: ExtractUrlTopLevelDomainUseCase,
) {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    operator fun invoke(maxEvents: Int = 100): Flow<Result<List<WebsiteUi>>> = flow {
        emit(Result.Loading)

        repository.observeLatest(maxEvents)
            .map { entities ->
                entities.map {
                    it.toWebsiteUi { pkg ->
                        getAppNameUseCase(pkg) ?: it.packageName
                    }
                }
            }
            .collect { websiteList ->
                if (websiteList.isEmpty()) {
                    emit(Result.Error())
                } else {
                    emit(Result.Success(websiteList))
                }
            }
    }.catch { throwable ->
        emit(Result.Error(throwable))
    }

    fun WebsiteEventEntity.toWebsiteUi(
        appNameProvider: (String) -> String
    ): WebsiteUi =
        WebsiteUi(
            appName = appNameProvider(packageName),
            packageName = packageName,
            url = url,
            topLevelDomain = extractUrlTopLevelDomainUseCase(url),
            timestamp = timestamp,
            time = timeFormatter.formatTime(timestamp),
            faviconUrl = url.faviconUrl()
        )
}
