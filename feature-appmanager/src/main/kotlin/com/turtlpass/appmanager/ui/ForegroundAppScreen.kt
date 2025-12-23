package com.turtlpass.appmanager.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.appmanager.R
import com.turtlpass.appmanager.extension.conditionalHaze
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.ui.skeleton.AndroidIconSkeleton
import com.turtlpass.appmanager.ui.skeleton.RecentAppsRowSkeleton
import com.turtlpass.appmanager.viewmodel.AppManagerUiState
import com.turtlpass.domain.Result
import com.turtlpass.domain.data
import com.turtlpass.ui.anim.conditionalAnimateGridItem
import com.turtlpass.ui.component.ActionCard
import com.turtlpass.ui.fling.rememberCustomFlingBehavior
import com.turtlpass.ui.input.FilterTextField
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appIndication
import com.turtlpass.ui.theme.appTextSelectionColors
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.FlowPreview
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(
    ExperimentalAtomicApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalHazeApi::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
    FlowPreview::class,
)
@Composable
fun ForegroundAppScreen(
    appManagerUiState: State<AppManagerUiState>,
    hazeState: HazeState,
    topAppBarState: TopAppBarState,
    appSearchEnabled: Boolean,
    onAppSearch: (String) -> Unit,
    onAppSelected: (InstalledAppUi) -> Unit,
) {
    val gridState = rememberLazyGridState()          // for the outer LazyVerticalGrid
    val recentRowState = rememberLazyListState()     // for the horizontal LazyRow
    val filteredApps by remember { derivedStateOf { appManagerUiState.value.filteredApps } }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isSearchFocused) {
        if (isSearchFocused) {
            // Scroll the grid to the first item
            gridState.animateScrollToItem(0)
            // expand TopAppBar
            topAppBarState.heightOffset = -topAppBarState.heightOffsetLimit
        }
    }
    LaunchedEffect(appSearchEnabled) {
        if (appSearchEnabled) {
            // Scroll the grid to the first item
            gridState.animateScrollToItem(0)
            // expand TopAppBar
            topAppBarState.heightOffset = -topAppBarState.heightOffsetLimit
        } else {
            // Clear the search query when search is disabled
            searchQuery = ""
            // trigger a search with empty query
            onAppSearch("")
        }
    }
    val isScrolling by remember { derivedStateOf { gridState.isScrollInProgress } }
    var hazeEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(isScrolling) {
        hazeEnabled = !isScrolling
    }

    val recentAppList = appManagerUiState.value.recentAppsResult.data ?: emptyList()
    LaunchedEffect(recentAppList) {
        if (recentAppList.isNotEmpty()) {
            // Scroll to index 0 whenever the list updates
            recentRowState.animateScrollToItem(0)
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 70.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(colors.default.background),
        contentPadding = PaddingValues(bottom = dimensions.x16),
        flingBehavior = rememberCustomFlingBehavior(velocityMultiplier = 0.5f)
    ) {
        if (appSearchEnabled) {
            stickyHeader(key = "searchHeader") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.ultraThin(containerColor = colors.default.background),
                        ) {
                            progressive = HazeProgressive.verticalGradient(
                                startIntensity = 1f,
                                endIntensity = 0f,
                                preferPerformance = true
                            )
                        }
                ) {
                    CompositionLocalProvider(
                        LocalTextSelectionColors provides appTextSelectionColors()
                    ) {
                        FilterTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensions.x16)
                                .padding(top = dimensions.x8),
                            label = stringResource(id = R.string.feature_appmanager_input_label_filter_app),
                            value = searchQuery,
                            onValueChange = { newText ->
                                searchQuery = newText
                                onAppSearch(newText)
                            },
                            onFocusChanged = { focused ->
                                isSearchFocused = focused
                            },
                        )
                    }
                }
            }
        }

        item(key = "recentHeader", span = { GridItemSpan(maxLineSpan) }) {
            RecentAppsHeader(
                isSearchFocused = isSearchFocused,
                hazeState = hazeState,
                hazeEnabled = hazeEnabled
            )
        }

        item(key = "recentApps", span = { GridItemSpan(maxLineSpan) }) {
            RecentAppsRow(
                appManagerUiState = appManagerUiState,
                recentAppList = recentAppList,
                recentRowState = recentRowState,
                hazeState = hazeState,
                hazeEnabled = hazeEnabled,
                isSearchFocused = isSearchFocused,
                onAppSelected = onAppSelected
            )
        }

        item(key = "allApps", span = { GridItemSpan(maxLineSpan) }) {
            AllAppsHeader(
                isSearchFocused = isSearchFocused,
                hazeState = hazeState,
                hazeEnabled = hazeEnabled
            )
        }

        if (appManagerUiState.value.installedAppsResult.isLoading()) {
            items(
                count = 25, // enough to fill screen
                key = { "skeleton_$it" }
            ) {
                AndroidIconSkeleton(modifier = Modifier.animateItem())
            }
        } else {
            items(
                items = filteredApps,
                key = { it.packageName }
            ) { app ->
                AppGridItem(
                    app = app,
                    hazeState = hazeState,
                    hazeEnabled = hazeEnabled,
                    onAppSelected = onAppSelected,
                    modifier = conditionalAnimateGridItem(isScrolling),
                )
            }
        }
    }
}

@Composable
fun RecentAppsHeader(
    isSearchFocused: Boolean,
    hazeState: HazeState,
    hazeEnabled: Boolean,
) {
    AnimatedVisibility(visible = !isSearchFocused) {
        Row(
            modifier = Modifier
                .conditionalHaze(
                    enabled = hazeEnabled,
                    hazeState = hazeState
                )
                .fillMaxWidth()
                .padding(top = dimensions.x16)
                .padding(bottom = dimensions.x8)
                .padding(horizontal = dimensions.x16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.feature_appmanager_recently_used),
                style = typography.h2.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.text.primary,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecentAppsRow(
    appManagerUiState: State<AppManagerUiState>,
    recentAppList: List<InstalledAppUi>,
    hazeState: HazeState,
    hazeEnabled: Boolean,
    recentRowState: LazyListState,
    isSearchFocused: Boolean,
    onAppSelected: (InstalledAppUi) -> Unit,
) {
    val context = LocalContext.current

    AnimatedVisibility(visible = !isSearchFocused) {
        when {
            appManagerUiState.value.isUsageAccessGranted
                    && appManagerUiState.value.recentAppsResult.isLoading() -> {
                RecentAppsRowSkeleton()
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

                            recentAppList.lastIndex -> Modifier
                                .padding(end = dimensions.x8)

                            else -> Modifier
                        }

                        AndroidAdaptiveIcon(
                            modifier = modifier.conditionalHaze(
                                enabled = hazeEnabled,
                                hazeState = hazeState
                            ),
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

@Composable
fun AllAppsHeader(
    isSearchFocused: Boolean,
    hazeState: HazeState,
    hazeEnabled: Boolean,
) {
    AnimatedVisibility(visible = !isSearchFocused) {
        Row(
            modifier = Modifier
                .conditionalHaze(
                    enabled = hazeEnabled,
                    hazeState = hazeState
                )
                .fillMaxWidth()
                .padding(top = dimensions.x16 + dimensions.x8)
                .padding(bottom = dimensions.x8 + dimensions.x4)
                .padding(horizontal = dimensions.x16),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.feature_appmanager_all_apps),
                style = typography.h2.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.text.primary,
                ),
            )
        }
    }
}

@Composable
fun AppGridItem(
    app: InstalledAppUi,
    hazeState: HazeState,
    hazeEnabled: Boolean,
    onAppSelected: (InstalledAppUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .conditionalHaze(
                enabled = hazeEnabled,
                hazeState = hazeState
            )
            .clip(RoundedCornerShape(dimensions.x16))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = appIndication(),
                onClick = { onAppSelected(app) }
            ),
        contentAlignment = Alignment.Center
    ) {
        AndroidIcon(
            modifier = Modifier.fillMaxWidth(),
            app = app
        )
    }
}

private class UsageAccessEnabledProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
}

@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview(
    @PreviewParameter(UsageAccessEnabledProvider::class) item: Boolean
) {
    AppTheme {
        ForegroundAppScreen(
            hazeState = rememberHazeState(),
            topAppBarState = rememberTopAppBarState(),
            appManagerUiState = remember {
                mutableStateOf(
                    AppManagerUiState(
                        isUsageAccessGranted = item,
                        installedAppsResult = Result.Success(
                            listOf(
                                InstalledAppUi(
                                    appName = "Chrome",
                                    packageName = "com.android.chrome"
                                ),
                                InstalledAppUi(
                                    appName = "Gmail",
                                    packageName = "com.google.android.gm"
                                ),
                                InstalledAppUi(
                                    appName = "Google Maps",
                                    packageName = "com.google.android.apps.maps"
                                ),
                                InstalledAppUi(
                                    appName = "YouTube",
                                    packageName = "com.google.android.youtube"
                                ),
                                InstalledAppUi(
                                    appName = "Photos",
                                    packageName = "com.google.android.apps.photos"
                                ),
                                InstalledAppUi(
                                    appName = "Drive",
                                    packageName = "com.google.android.apps.docs"
                                ),
                                InstalledAppUi(
                                    appName = "Calendar",
                                    packageName = "com.google.android.calendar"
                                ),
                                InstalledAppUi(
                                    appName = "WhatsApp",
                                    packageName = "com.whatsapp"
                                ),
                                InstalledAppUi(
                                    appName = "Instagram",
                                    packageName = "com.instagram.android"
                                ),
                                InstalledAppUi(
                                    appName = "Spotify",
                                    packageName = "com.spotify.music"
                                ),
                                InstalledAppUi(
                                    appName = "Slack",
                                    packageName = "com.Slack"
                                ),
                                InstalledAppUi(
                                    appName = "Notion",
                                    packageName = "notion.id"
                                ),
                                InstalledAppUi(
                                    appName = "Twitter",
                                    packageName = "com.twitter.android"
                                ),
                                InstalledAppUi(
                                    appName = "Reddit",
                                    packageName = "com.reddit.frontpage"
                                ),
                                InstalledAppUi(
                                    appName = "TikTok",
                                    packageName = "com.zhiliaoapp.musically"
                                )
                            )
                        )
                    )
                )
            },
            appSearchEnabled = true,
            onAppSearch = {},
            onAppSelected = {},
        )
    }
}
