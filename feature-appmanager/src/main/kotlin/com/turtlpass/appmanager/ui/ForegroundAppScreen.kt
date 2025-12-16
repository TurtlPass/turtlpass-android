package com.turtlpass.appmanager.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.appmanager.R
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.ui.skeleton.AppRowSkeleton
import com.turtlpass.appmanager.viewmodel.AppManagerUiState
import com.turtlpass.domain.Result
import com.turtlpass.ui.input.FilterTextField
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.appTextSelectionColors
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(
    ExperimentalAtomicApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalHazeApi::class,
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
)
@Composable
fun ForegroundAppScreen(
    appManagerUiState: State<AppManagerUiState>,
    hazeState: HazeState,
    topAppBarState: TopAppBarState,
    onAppSelected: (InstalledAppUi) -> Unit,
) {
    val columnState = rememberLazyListState()        // for the outer LazyColumn
    val recentRowState = rememberLazyListState()     // for the horizontal LazyRow

    val appList: List<InstalledAppUi> = appManagerUiState.value.installedAppsResult.let { result ->
        if (result is Result.Success) result.data else emptyList()
    }
    var selectedText by remember { mutableStateOf("") }
    val filteredApps = remember(selectedText, appList) {
        appList.filter {
            it.appName.contains(selectedText, ignoreCase = true) ||
                    it.packageName.contains(selectedText, ignoreCase = true)
        }
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val minItemWidth = 70.dp
    val spacing = 12.dp
    val columns = ((screenWidth + spacing) / (minItemWidth + spacing)).toInt().coerceAtLeast(1)
    val gridRows =
        remember(filteredApps) { filteredApps.chunked(columns) } // flatten grid into rows

    var isSearchFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isSearchFocused) {
        if (isSearchFocused) {
            columnState.animateScrollToItem(0)
            topAppBarState.heightOffset = -topAppBarState.heightOffsetLimit
        }
    }

    Box {
        LazyColumn(
            state = columnState,
            modifier = Modifier
                .fillMaxSize()
                .background(colors.default.background)
        ) {
            // SEARCH HEADER
            stickyHeader {
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
                        .padding(horizontal = 16.dp)
                ) {
                    CompositionLocalProvider(
                        LocalTextSelectionColors provides appTextSelectionColors()
                    ) {
                        FilterTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensions.x8),
                            label = stringResource(id = R.string.feature_appmanager_input_label_filter_app),
                            value = selectedText,
                            onValueChange = { newText ->
                                selectedText = newText
                            },
                            onFocusChanged = { focused ->
                                isSearchFocused = focused
                            },
                        )
                    }
                }
            }

            item {
                RecentAppsHeader(
                    isSearchFocused = isSearchFocused,
                    hazeState = hazeState
                )
            }

            item {
                RecentAppsRow(
                    appManagerUiState = appManagerUiState,
                    recentRowState = recentRowState,
                    hazeState = hazeState,
                    isSearchFocused = isSearchFocused,
                    onAppSelected = onAppSelected
                )
            }

            item {
                AllAppsHeader(
                    isSearchFocused = isSearchFocused,
                    hazeState = hazeState
                )
            }

            // Flattened grid: rows that scroll with the LazyColumn (one unified scroll)
            if (appManagerUiState.value.installedAppsResult.isLoading()) {
                items(6) { // 6 placeholder rows
                    AppRowSkeleton(columns)
                }
            } else {
                items(
                    items = gridRows,
                    key = { row ->
                        // Combine app IDs in the row
                        row.joinToString(separator = "_") { it.packageName }
                    }
                ) { row ->
                    AppRow(row, hazeState, onAppSelected, columns)
                }
            }
        }
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
                                InstalledAppUi("com.domain.app1", "App 1"),
                                InstalledAppUi("com.domain.app2", "App 2"),
                                InstalledAppUi("com.domain.app3", "App 3"),
                                InstalledAppUi("com.domain.app1", "BApp 1"),
                                InstalledAppUi("com.domain.app2", "BApp 2"),
                                InstalledAppUi("com.domain.app1", "CApp 1"),
                                InstalledAppUi("com.domain.app1", "DApp 1"),
                                InstalledAppUi("com.domain.app2", "DApp 2"),
                                InstalledAppUi("com.domain.app3", "DApp 3"),
                                InstalledAppUi("com.domain.app4", "DApp 4"),
                                InstalledAppUi("com.domain.app5", "DApp 5"),
                                InstalledAppUi("com.domain.app6", "DApp 6"),
                                InstalledAppUi("com.domain.app7", "DApp 7"),
                            )
                        )
                    )
                )
            },
            onAppSelected = {},
        )
    }
}
