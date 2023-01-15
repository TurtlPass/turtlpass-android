package com.turtlpass.module.accessibility.usecase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.turtlpass.di.ApplicationScope
import com.turtlpass.module.accessibility.service.PackageAccessibilityService
import com.turtlpass.module.installedapp.model.InstalledApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class PackageUpdatesUseCase @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val context: Context,
    private val getApplicationInfoUseCase: GetApplicationInfoUseCase,
) {
    suspend operator fun invoke(): Flow<InstalledApp> {
        return callbackFlow {
            val packageReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    if (intent.action == PackageAccessibilityService.ACTION_FOREGROUND_APPLICATION_CHANGED) {
                        val packageName = intent.getStringExtra(PackageAccessibilityService.EXTRA_PACKAGE_NAME).toString()
                        getApplicationInfoUseCase(packageName)?.let { installedApp ->
                            trySend(installedApp)
                        }
                    }
                }
            }
            Timber.d("Starting package updates")
            val intentFilter =
                IntentFilter(PackageAccessibilityService.ACTION_FOREGROUND_APPLICATION_CHANGED)
            context.registerReceiver(packageReceiver, intentFilter)

            awaitClose {
                Timber.d("Stopping package updates")
                context.unregisterReceiver(packageReceiver)
            }
        }.shareIn( // make cold flow hot
            coroutineScope, // make the flow follow the externalScope
            replay = 1, // emit the last emitted element to new collectors
            started = SharingStarted.Lazily
        )
    }
}
