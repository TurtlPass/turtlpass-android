package com.turtlpass.ui.bottomSheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Add ability to fully expanded BottomSheetNavigator by default
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetNavigator(
    sheetState: ModalBottomSheetState,
    skipHalfExpanded: Boolean = true,
): BottomSheetNavigator {
    if (skipHalfExpanded) {
        LaunchedEffect(sheetState) {
            snapshotFlow { sheetState.currentValue to sheetState.targetValue }
                .distinctUntilChanged()
                .collect { (current, target) ->
                    val isAnimating = current != target
                    if (isAnimating) {
                        val isOpening = current == ModalBottomSheetValue.Hidden &&
                                target == ModalBottomSheetValue.HalfExpanded
                        val isClosing = current == ModalBottomSheetValue.Expanded &&
                                target == ModalBottomSheetValue.HalfExpanded

                        when {
                            isOpening -> sheetState.show() // instead of animateTo(Expanded)
                            isClosing -> sheetState.hide() // instead of animateTo(Hidden)
                        }
                    }
                }
        }
    }

    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}