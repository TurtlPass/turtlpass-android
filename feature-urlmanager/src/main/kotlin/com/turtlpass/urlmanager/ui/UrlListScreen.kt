package com.turtlpass.urlmanager.ui

import android.content.Intent
import android.content.res.Configuration
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
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
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val websiteList: List<WebsiteUi> = urlManagerUiState.value.websiteListResult.let { result ->
        if (result is Result.Success) result.data else emptyList()
    }
    var expandedItemKey by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(containerColor = Color.White),
            )
            .background(colors.default.background)
    ) {
        // HEADER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(top = dimensions.x16)
                        .padding(bottom = dimensions.x8 + dimensions.x4)
                        .padding(horizontal = dimensions.x16),
                    text = "Recent Websites",
                    style = typography.h2.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    ),
                )
                AnimatedVisibility(
                    visible = urlManagerUiState.value.isAccessibilityEnabled
                            && websiteList.isNotEmpty()
                ) {
                    IconContextMenu(onDelete = onClearAll)
                }
            }
        }

        // EMPTY STATE WHEN ACCESSIBILITY ENABLED BUT LIST EMPTY
        if (
            urlManagerUiState.value.isAccessibilityEnabled &&
            !urlManagerUiState.value.websiteListResult.isLoading() &&
            websiteList.isEmpty()
        ) {
            item {
                EmptyWebsitesState(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(bottom = dimensions.x64)
                        .padding(horizontal = dimensions.x16)
                )
            }
        }
        // SKELETON LOADING
        else if (urlManagerUiState.value.websiteListResult.isLoading()) {
            items(12) {
                AnimatedVisibility(visible = urlManagerUiState.value.isAccessibilityEnabled) {
                    UrlItemSkeleton(
                        modifier = Modifier
                            .padding(vertical = dimensions.x4)
                            .padding(horizontal = dimensions.x16)
                            .animateItem()
                    )
                }
            }
            // ACTUAL WEBSITE ITEMS
        } else {
            itemsIndexed(
                items = websiteList,
                key = { _, item -> "${item.timestamp}_${item.url}" }
            ) { index, websiteUi ->
                var isVisible by remember { mutableStateOf(true) }
                val itemKey = "${websiteUi.timestamp}_${websiteUi.url}"

                AnimatedVisibility(
                    visible = isVisible && urlManagerUiState.value.isAccessibilityEnabled,
                    exit = shrinkVertically(animationSpec = tween(250)) + fadeOut(
                        animationSpec = tween(
                            250
                        )
                    )
                ) {
                    val verticalModifier = when (index) {
                        websiteList.lastIndex -> Modifier
                            .padding(top = dimensions.x4)
                            .padding(bottom = dimensions.x8)

                        else -> Modifier.padding(vertical = dimensions.x4)
                    }

                    SwipeRevealUrlItem(
                        modifier = verticalModifier
                            .animateItem(), // smooth diff animation
                        websiteUi = websiteUi,
                        isExpanded = expandedItemKey == itemKey,
                        onExpandChanged = { expanded ->
                            expandedItemKey = if (expanded) itemKey else null
                        },
                        onSelectWebsite = {
                            onSelectWebsite(websiteUi)
                        },
                        onDelete = {
                            // Animate exit before removing
                            isVisible = false
                            scope.launch {
                                delay(250)
                                onDeleteWebsite(websiteUi)
                                if (expandedItemKey == itemKey) expandedItemKey = null
                            }
                        }
                    )
                }
            }
        }

        item {
            AnimatedVisibility(visible = urlManagerUiState.value.isAccessibilityEnabled.not()) {
                ActionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(
                            horizontal = dimensions.x16
                        ),
                    text = stringResource(R.string.feature_urlmanager_rationale_accessibility),
                    buttonText = stringResource(R.string.feature_urlmanager_rationale_accessibility_button),
                    onClick = {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    backgroundColor = colors.default.cardBackground,
                )
            }
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
/*@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)*/
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
