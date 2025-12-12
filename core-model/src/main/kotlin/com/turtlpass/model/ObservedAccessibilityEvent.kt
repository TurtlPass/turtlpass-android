package com.turtlpass.model

sealed class ObservedAccessibilityEvent {

    abstract val packageName: String
    abstract val timestamp: Long

    data class AppEvent(
        override val packageName: String,
        override val timestamp: Long,
    ) : ObservedAccessibilityEvent()

    data class UrlEvent(
        val url: String,
        override val packageName: String,
        override val timestamp: Long,
    ) : ObservedAccessibilityEvent()
}