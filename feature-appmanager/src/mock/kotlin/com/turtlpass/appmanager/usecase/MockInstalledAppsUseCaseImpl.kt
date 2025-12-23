package com.turtlpass.appmanager.usecase

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.di.IoDispatcher
import com.turtlpass.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MockInstalledAppsUseCaseImpl @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : InstalledAppsUseCase {
    override operator fun invoke(): Flow<Result<List<InstalledAppUi>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(mockInstalledApps()))
    }.flowOn(dispatcher)

    private fun mockInstalledApps(): List<InstalledAppUi> = listOf(
        InstalledAppUi(
            appName = "Aurora Store",
            packageName = "com.aurora.store",
            topLevelDomain = "aurora"
        ),
        InstalledAppUi(
            appName = "DuckDuckGo",
            packageName = "com.duckduckgo.mobile.android",
            topLevelDomain = "duckduckgo"
        ),
        InstalledAppUi(
            appName = "F-Droid Basic",
            packageName = "org.fdroid.basic",
            topLevelDomain = "fdroid"
        ),
        InstalledAppUi(
            appName = "Fing",
            packageName = "com.overlook.android.fing",
            topLevelDomain = "overlook"
        ),
        InstalledAppUi(
            appName = "FUTO Keyboard",
            packageName = "org.futo.inputmethod.latin",
            topLevelDomain = "futo"
        ),
        InstalledAppUi(
            appName = "FX",
            packageName = "nextapp.fx",
            topLevelDomain = "nextapp"
        ),
        InstalledAppUi(
            appName = "Grayjay",
            packageName = "com.futo.platformplayer",
            topLevelDomain = "futo"
        ),
        InstalledAppUi(
            appName = "Immich",
            packageName = "app.alextran.immich",
            topLevelDomain = "alextran"
        ),
        InstalledAppUi(
            appName = "K-9 Mail",
            packageName = "com.fsck.k9",
            topLevelDomain = "fsck"
        ),
        InstalledAppUi(
            appName = "LinkedIn",
            packageName = "com.linkedin.android",
            topLevelDomain = "linkedin"
        ),
        InstalledAppUi(
            appName = "Messenger",
            packageName = "com.facebook.orca",
            topLevelDomain = "facebook"
        ),
        InstalledAppUi(
            appName = "MuPDF viewer",
            packageName = "com.artifex.mupdf.viewer.app",
            topLevelDomain = "artifex"
        ),
        InstalledAppUi(
            appName = "Obtainium",
            packageName = "dev.imranr.obtainium.fdroid",
            topLevelDomain = "imranr"
        ),
        InstalledAppUi(
            appName = "Organic Maps",
            packageName = "app.organicmaps",
            topLevelDomain = "organicmaps"
        ),
        InstalledAppUi(
            appName = "Scrambled Exif",
            packageName = "com.jarsilio.android.scrambledeggsif",
            topLevelDomain = "jarsilio"
        ),
        InstalledAppUi(
            appName = "Stremio",
            packageName = "com.stremio.one",
            topLevelDomain = "stremio"
        ),
        InstalledAppUi(
            appName = "Telegram",
            packageName = "org.telegram.messenger.web",
            topLevelDomain = "telegram"
        ),
        InstalledAppUi(
            appName = "TikTok",
            packageName = "com.zhiliaoapp.musically",
            topLevelDomain = "zhiliaoapp"
        ),
        InstalledAppUi(
            appName = "Tor Browser",
            packageName = "org.torproject.torbrowser",
            topLevelDomain = "torproject"
        ),
        InstalledAppUi(
            appName = "WhatsApp",
            packageName = "com.whatsapp",
            topLevelDomain = "whatsapp"
        ),
        InstalledAppUi(
            appName = "YouTube",
            packageName = "com.google.android.youtube",
            topLevelDomain = "google"
        )
    ).sortedBy { it.appName.lowercase() }
}

