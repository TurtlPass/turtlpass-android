package com.turtlpass.urlmanager.usecase

import com.turtlpass.accessibility.usecase.AccessibilityEnabledUpdatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MockAccessibilityEnabledUpdatesUseCaseImpl @Inject constructor(
) : AccessibilityEnabledUpdatesUseCase {
    override operator fun invoke(): StateFlow<Boolean> =
        MutableStateFlow(true)
}
