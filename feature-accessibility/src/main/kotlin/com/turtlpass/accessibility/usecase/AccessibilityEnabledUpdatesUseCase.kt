package com.turtlpass.accessibility.usecase

import kotlinx.coroutines.flow.StateFlow

interface AccessibilityEnabledUpdatesUseCase {
    operator fun invoke(): StateFlow<Boolean>
}
