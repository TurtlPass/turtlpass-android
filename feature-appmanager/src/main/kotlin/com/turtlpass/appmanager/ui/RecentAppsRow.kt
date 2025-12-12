package com.turtlpass.appmanager.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.turtlpass.appmanager.R
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.ui.skeleton.RecentAppsRowSkeleton
import com.turtlpass.appmanager.viewmodel.AppManagerUiState
import com.turtlpass.domain.data
import com.turtlpass.ui.component.ActionCard
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecentAppsRow(
    appManagerUiState: State<AppManagerUiState>,
    hazeState: HazeState,
    recentRowState: LazyListState,
    isSearchFocused: Boolean,
    onAppSelected: (InstalledAppUi) -> Unit
) {
    val context = LocalContext.current
    val recentAppList = appManagerUiState.value.recentAppsResult.data ?: emptyList()

    // Scroll to index 0 whenever the list updates
    LaunchedEffect(recentAppList) {
        if (recentAppList.isNotEmpty()) {
            recentRowState.animateScrollToItem(0)
        }
    }

    AnimatedVisibility(visible = !isSearchFocused) {
        when {
            appManagerUiState.value.isUsageAccessGranted
                    && appManagerUiState.value.recentAppsResult.isLoading() -> {
                RecentAppsRowSkeleton(
                    hazeState = hazeState
                )
            }

            appManagerUiState.value.isUsageAccessGranted
                    && appManagerUiState.value.recentAppsResult.isSuccessful() -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = recentRowState,
                    verticalAlignment = Alignment.CenterVertically,
                    flingBehavior = rememberSnapFlingBehavior(recentRowState)
                ) {
                    itemsIndexed(
                        items = recentAppList,
                        key = { _, app -> app.packageName }
                    ) { index, app ->
                        val modifier = when (index) {
                            0 -> Modifier
                                .padding(start = dimensions.x8)
                                .hazeSource(hazeState)

                            recentAppList.lastIndex -> Modifier
                                .padding(end = dimensions.x8)
                                .hazeSource(hazeState)

                            else -> Modifier.hazeSource(hazeState)
                        }

                        AndroidAdaptiveIcon(
                            modifier = modifier,
                            app = app,
                            onClick = { onAppSelected(app) }
                        )
                    }
                }
            }

            appManagerUiState.value.isUsageAccessGranted.not() -> {
                ActionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = dimensions.x4 + dimensions.x2)
                        .padding(horizontal = dimensions.x16),
                    text = stringResource(R.string.feature_appmanager_rationale_apps_usage),
                    buttonText = stringResource(R.string.feature_appmanager_rationale_apps_usage_button),
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    },
                    backgroundColor = colors.default.cardBackground
                )
            }
        }
    }
}
