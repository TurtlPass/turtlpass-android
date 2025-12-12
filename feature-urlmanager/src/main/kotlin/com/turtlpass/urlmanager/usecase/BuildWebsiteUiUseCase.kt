package com.turtlpass.urlmanager.usecase

import com.turtlpass.accessibility.extension.parseUrl
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.extension.faviconUrl
import javax.inject.Inject

class BuildWebsiteUiUseCase @Inject constructor(
    private val extractUrlTopLevelDomainUseCase: ExtractUrlTopLevelDomainUseCase,
) {
    operator fun invoke(url: String): WebsiteUi = WebsiteUi(
        packageName = "", // unknown
        url = parseUrl(url) ?: url,
        topLevelDomain = extractUrlTopLevelDomainUseCase(url),
        timestamp = System.currentTimeMillis(),
        faviconUrl = url.faviconUrl()
    )
}
