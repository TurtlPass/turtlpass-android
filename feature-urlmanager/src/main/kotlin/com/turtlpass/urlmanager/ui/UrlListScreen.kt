package com.turtlpass.urlmanager.ui

import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi
import com.turtlpass.ui.component.ActionCard
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.urlmanager.R
import com.turtlpass.urlmanager.ui.menu.IconContextMenu
import com.turtlpass.urlmanager.ui.skeleton.UrlItemSkeleton
import com.turtlpass.urlmanager.viewmodel.UrlManagerUiState
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class UrlListContent { Permission, Loading, List, Empty }

@OptIn(
    ExperimentalHazeMaterialsApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun UrlListScreen(
    urlManagerUiState: State<UrlManagerUiState>,
    hazeState: HazeState,
    onSelectWebsite: (websiteUi: WebsiteUi) -> Unit,
    onDeleteWebsite: (websiteUi: WebsiteUi) -> Unit,
    onClearAll: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val websiteList: List<WebsiteUi> = urlManagerUiState.value.websiteListResult.let { result ->
        if (result is Result.Success) result.data else emptyList()
    }
    var expandedItemKey by remember { mutableStateOf<String?>(null) }

    val contentState = when {
        urlManagerUiState.value.isAccessibilityEnabled.not() ->
            UrlListContent.Permission

        urlManagerUiState.value.websiteListResult.isLoading() ->
            UrlListContent.Loading

        urlManagerUiState.value.isAccessibilityEnabled && websiteList.isEmpty() ->
            UrlListContent.Empty

        else ->
            UrlListContent.List
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.default.background)
    ) {
        // HEADER
        HeaderRow(
            hazeState = hazeState,
            showClearAll = urlManagerUiState.value.isAccessibilityEnabled && websiteList.isNotEmpty(),
            onClearAll = onClearAll
        )

        // CONTENT
        AnimatedContent(
            targetState = contentState,
            contentKey = { it },
            transitionSpec = {
                (fadeIn(tween(180)) + slideInVertically { it / 12 }) togetherWith
                        (fadeOut(tween(120)) + slideOutVertically { it / 12 })
            },
            label = "UrlListContent"
        ) { state ->
            when (state) {
                UrlListContent.Permission -> {
                    ActionCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = dimensions.x16),
                        text = stringResource(R.string.feature_urlmanager_rationale_accessibility),
                        buttonText = stringResource(R.string.feature_urlmanager_rationale_accessibility_button),
                        onClick = {
                            context.startActivity(
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            )
                        },
                        backgroundColor = colors.default.cardBackground
                    )
                }

                UrlListContent.Loading -> {
                    LazyColumn {
                        items(12, key = { "skeleton_$it" }) {
                            UrlItemSkeleton(
                                modifier = Modifier
                                    .padding(horizontal = dimensions.x16, vertical = dimensions.x4)
                            )
                        }
                    }
                }

                UrlListContent.Empty -> {
                    EmptyWebsitesState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dimensions.x16)
                            .padding(bottom = dimensions.x32)
                    )
                }

                UrlListContent.List -> {
                    WebsiteListContent(
                        websiteList = websiteList,
                        hazeState = hazeState,
                        expandedItemKey = expandedItemKey,
                        onExpandChanged = { expandedItemKey = it },
                        onSelectWebsite = onSelectWebsite,
                        onDeleteWebsite = { websiteUi ->
                            scope.launch {
                                delay(250)
                                onDeleteWebsite(websiteUi)
                                if (expandedItemKey == "${websiteUi.timestamp}_${websiteUi.url}") {
                                    expandedItemKey = null
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
private fun HeaderRow(
    hazeState: HazeState,
    showClearAll: Boolean,
    onClearAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThick(containerColor = colors.default.background),
            ) {
                progressive = HazeProgressive.verticalGradient(
                    startIntensity = 1f,
                    endIntensity = 0f,
                    preferPerformance = true
                )
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = dimensions.x16)
                .padding(bottom = dimensions.x8 + dimensions.x4)
                .padding(horizontal = dimensions.x16),
            text = stringResource(R.string.feature_urlmanager_recent_websites),
            style = typography.h2.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colors.text.primary,
            ),
        )
        if (showClearAll) {
            IconContextMenu(onDelete = onClearAll)
        }
    }
}

private class AccessibilityServiceEnabledProvider : PreviewParameterProvider<Boolean> {
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
    @PreviewParameter(AccessibilityServiceEnabledProvider::class) item: Boolean
) {
    AppTheme {
        UrlListScreen(
            urlManagerUiState = remember {
                mutableStateOf(
                    UrlManagerUiState(
                        isAccessibilityEnabled = item,
                        websiteListResult = Result.Success(
                            listOf(
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987067L,
                                ),
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987778L,
                                ),
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987779L,
                                ),
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987770L,
                                ),
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987771L,
                                ),
                                WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987772L,
                                ),
                            )
                        )
                    )
                )
            },
            hazeState = rememberHazeState(),
            onSelectWebsite = {},
            onDeleteWebsite = {},
            onClearAll = {}
        )
    }
}
