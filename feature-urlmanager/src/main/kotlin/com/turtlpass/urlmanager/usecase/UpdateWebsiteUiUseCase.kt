package com.turtlpass.urlmanager.usecase

import com.turtlpass.model.ObservedAccessibilityEvent
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.extension.faviconUrl
import javax.inject.Inject

class UpdateWebsiteUiUseCase @Inject constructor(
    private val extractUrlTopLevelDomainUseCase: ExtractUrlTopLevelDomainUseCase,
) {
    operator fun invoke(event: ObservedAccessibilityEvent.UrlEvent): WebsiteUi = WebsiteUi(
        packageName = event.packageName,
        url = event.url,
        topLevelDomain = extractUrlTopLevelDomainUseCase(event.url),
        timestamp = event.timestamp,
        faviconUrl = event.url.faviconUrl()
    )
}
