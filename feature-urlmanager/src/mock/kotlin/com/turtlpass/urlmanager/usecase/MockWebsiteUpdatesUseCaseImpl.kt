package com.turtlpass.urlmanager.usecase

import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.extension.faviconUrl
import com.turtlpass.urlmanager.extension.formatTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MockWebsiteUpdatesUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val extractUrlTopLevelDomainUseCase: ExtractUrlTopLevelDomainUseCase,
) : WebsiteUpdatesUseCase {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override operator fun invoke(maxEvents: Int): Flow<Result<List<WebsiteUi>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(mockWebsiteUpdates()))
    }.flowOn(dispatcher)

    private fun mockWebsiteUpdates(): List<WebsiteUi> {
        val now = System.currentTimeMillis()

        fun event(
            minutesAgo: Long,
            appName: String,
            packageName: String,
            url: String,
        ): WebsiteUi {
            val timestamp = now - minutesAgo * 60_000
            return WebsiteUi(
                appName = appName,
                packageName = packageName,
                url = url,
                topLevelDomain = extractUrlTopLevelDomainUseCase(url),
                timestamp = timestamp,
                time = timeFormatter.formatTime(timestamp),
                faviconUrl = url.faviconUrl()
            )
        }

        return listOf(
            event(
                minutesAgo = 3,
                url = "reddit.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 9,
                url = "ycombinator.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 14,
                url = "github.com",
                appName = "Chromium",
                packageName = "org.chromium.chrome",
            ),
            event(
                minutesAgo = 28,
                url = "medium.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 35,
                url = "stackoverflow.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 80,
                url = "amazon.com",
                appName = "Chromium",
                packageName = "org.chromium.chrome",
            ),
            event(
                minutesAgo = 150,
                url = "weather.com",
                appName = "Chromium",
                packageName = "org.chromium.chrome",
            ),
            event(
                minutesAgo = 160,
                url = "ikea.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 169,
                url = "theverge.com",
                appName = "DuckDuckGo",
                packageName = "com.duckduckgo.mobile.android",
            ),
            event(
                minutesAgo = 171,
                url = "slack.com",
                appName = "Chromium",
                packageName = "org.chromium.chrome",
            ),
        )
    }
}
