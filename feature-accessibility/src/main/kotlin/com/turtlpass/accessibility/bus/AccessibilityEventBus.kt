package com.turtlpass.accessibility.bus

import com.turtlpass.model.ObservedAccessibilityEvent
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@Singleton
class AccessibilityEventBus @Inject constructor() {

    // Holds the latest UrlEvent (or null if none yet)
    private val _lastUrl = MutableStateFlow<ObservedAccessibilityEvent.UrlEvent?>(null)
    val lastUrl: StateFlow<ObservedAccessibilityEvent.UrlEvent?> = _lastUrl

    // Holds the latest event of any type (AppEvent or UrlEvent)
    private val _lastEvent = MutableStateFlow<ObservedAccessibilityEvent?>(null)
    val lastEvent: StateFlow<ObservedAccessibilityEvent?> = _lastEvent

    // keep all events for downstream consumers
    private val _events = MutableSharedFlow<ObservedAccessibilityEvent>(
        replay = 1,
        extraBufferCapacity = 20
    )
    val events: SharedFlow<ObservedAccessibilityEvent> = _events


    suspend fun emit(event: ObservedAccessibilityEvent) {
        _events.emit(event)

        if (event is ObservedAccessibilityEvent.UrlEvent) {
            _lastUrl.value = event
        }

        // Always update the last event
        _lastEvent.value = event
    }

    fun tryEmit(event: ObservedAccessibilityEvent) {
        _events.tryEmit(event)

        if (event is ObservedAccessibilityEvent.UrlEvent) {
            _lastUrl.value = event
        }

        _lastEvent.value = event
    }
}
