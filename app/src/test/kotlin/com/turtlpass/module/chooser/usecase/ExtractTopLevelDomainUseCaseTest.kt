package com.turtlpass.module.chooser.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExtractTopLevelDomainUseCaseTest {

    private val extractTopLevelDomainUseCase by lazy { ExtractTopLevelDomainUseCase() }

    @Test
    fun `Given a list of package names we extract the top level domain`() {
        val packageNameList = listOf(
            "io.metamask",
            "com.xe.currency",
            "com.shazam.android",
            "com.revolut.revolut",
            "com.whatsapp",
            "com.nest.android",
            "tunein.player",
            "com.paypal.android.p2pmobile",
            "com.discord",
            "com.stripe.android.dashboard",
            "com.brave.browser",
            "com.duckduckgo.mobile.android",
            "org.telegram.messenger.web",
            "com.instagram.android",
            "com.twitter.android",
            "org.fdroid.fdroid",
            "com.google.android.street",
            "com.google.android.apps.translate",
            "com.google.android.apps.meetings",
            "com.google.android.keep",
            "com.teamviewer.teamviewer.market.mobile",
            "com.teamviewer.quicksupport.market",
            "com.facebook.orca",
            "com.linkedin.android",
            "com.ebay.mobile",
        )
        val expectedResults = listOf(
            "metamask",
            "xe",
            "shazam",
            "revolut",
            "whatsapp",
            "nest",
            "tunein",
            "paypal",
            "discord",
            "stripe",
            "brave",
            "duckduckgo",
            "telegram",
            "instagram",
            "twitter",
            "fdroid",
            "google",
            "google",
            "google",
            "google",
            "teamviewer",
            "teamviewer",
            "facebook",
            "linkedin",
            "ebay",
        )
        for (i in packageNameList.indices) {
            val result = extractTopLevelDomainUseCase(packageName = packageNameList[i])
            assertEquals(expectedResults[i], result)
        }
    }
}
