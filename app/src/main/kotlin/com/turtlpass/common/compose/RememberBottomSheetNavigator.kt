package com.turtlpass.common.compose

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

/**
 * Add ability to fully expanded BottomSheetNavigator by default
 *
 * src: https://github.com/google/accompanist/issues/657#issuecomment-938249186
 */
@ExperimentalMaterialApi
@ExperimentalMaterialNavigationApi
@Composable
fun rememberBottomSheetNavigator(
    sheetState: ModalBottomSheetState,
    skipHalfExpanded: Boolean = true,
): BottomSheetNavigator {
    if (skipHalfExpanded) {
        LaunchedEffect(sheetState) {
            snapshotFlow { sheetState.isAnimationRunning }
                .collect {
                    with(sheetState) {
                        val isOpening = currentValue == ModalBottomSheetValue.Hidden
                                && targetValue == ModalBottomSheetValue.HalfExpanded
                        val isClosing = currentValue == ModalBottomSheetValue.Expanded
                                && targetValue == ModalBottomSheetValue.HalfExpanded
                        when {
                            isOpening -> animateTo(ModalBottomSheetValue.Expanded)
                            isClosing -> animateTo(ModalBottomSheetValue.Hidden)
                        }
                    }
                }
        }
    }
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}
