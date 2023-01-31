package com.turtlpass.module.accessibility.usecase

import android.content.*
import android.database.ContentObserver
import android.provider.Settings
import android.text.TextUtils
import com.turtlpass.di.ApplicationScope
import com.turtlpass.module.accessibility.service.PackageAccessibilityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class AccessibilityUpdatesUseCase @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val context: Context,
    private val contentResolver: ContentResolver,
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return callbackFlow {
            val observer = object : ContentObserver(null) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    val enabled = isAccessibilityServiceEnabled(
                        context = context,
                        accessibilityService = PackageAccessibilityService::class.java
                    )
                    trySend(enabled)
                }
            }
            Timber.d("Starting accessibility updates")
            val uri = Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            contentResolver.registerContentObserver(uri, false, observer)

            // initial check
            val enabled = isAccessibilityServiceEnabled(
                context = context,
                accessibilityService = PackageAccessibilityService::class.java
            )
            trySend(enabled)

            awaitClose {
                Timber.d("Stopping accessibility updates")
                contentResolver.unregisterContentObserver(observer)
            }
        }.shareIn( // make cold flow hot
            coroutineScope, // make the flow follow the externalScope
            replay = 1, // emit the last emitted element to new collectors
            started = SharingStarted.Eagerly
        )
    }

    /**
     * Based on [com.android.settingslib.accessibility.AccessibilityUtils.getEnabledServicesFromSettings]
     *
     * @see [AccessibilityUtils](https://github.com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096/packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils.java.L55)
     */
    private fun isAccessibilityServiceEnabled(
        context: Context,
        accessibilityService: Class<*>
    ): Boolean {
        try {
            val expectedComponentName = ComponentName(context, accessibilityService)
            val enabledServicesSetting: String =
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
                )
                    ?: return false
            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServicesSetting)
            while (colonSplitter.hasNext()) {
                val componentNameString: String = colonSplitter.next()
                val enabledService = ComponentName.unflattenFromString(componentNameString)
                if (enabledService != null && enabledService == expectedComponentName) return true
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return false
    }
}
