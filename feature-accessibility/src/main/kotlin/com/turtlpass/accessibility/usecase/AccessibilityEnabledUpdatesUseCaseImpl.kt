package com.turtlpass.accessibility.usecase

import android.view.accessibility.AccessibilityManager
import com.turtlpass.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

class AccessibilityEnabledUpdatesUseCaseImpl @Inject constructor(
    @param:ApplicationScope private val coroutineScope: CoroutineScope,
    private val accessibilityManager: AccessibilityManager,
) : AccessibilityEnabledUpdatesUseCase {
    override operator fun invoke(): StateFlow<Boolean> {
        return callbackFlow {
            val listener =
                AccessibilityManager.AccessibilityStateChangeListener { enabled ->
                    trySend(enabled)
                }

            Timber.d("Starting accessibility updates")

            // Register listener
            accessibilityManager.addAccessibilityStateChangeListener(listener)

            // Immediate initial value
            trySend(accessibilityManager.isEnabled)

            awaitClose {
                Timber.d("Stopping accessibility updates")
                accessibilityManager.removeAccessibilityStateChangeListener(listener)
            }
        }.distinctUntilChanged()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = accessibilityManager.isEnabled
            )
    }
}
