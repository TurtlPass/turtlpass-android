package com.turtlpass.module.installedapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.common.compose.column.StickyCharLazyColumn
import com.turtlpass.common.compose.input.DragHandle
import com.turtlpass.common.compose.input.FilterTextField
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.appTextSelectionColors

@ExperimentalMaterialApi
@ExperimentalTextApi
@Composable
fun InstalledAppsScreen(
    appList: List<InstalledApp>,
    onAppSelected: (installedApp: InstalledApp) -> Unit,
    expandBottomSheet: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    var selectedText by remember { mutableStateOf("") }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.x16)
                .padding(horizontal = dimensions.x16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DragHandle()

            CompositionLocalProvider(
                LocalTextSelectionColors provides appTextSelectionColors()
            ) {
                FilterTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensions.x16,
                            bottom = dimensions.x8,
                        ),
                    value = selectedText,
                    onValueChange = { newText ->
                        selectedText = newText
                    },
                    onFocus = expandBottomSheet,
                )
            }
        }

        StickyCharLazyColumn(
            lazyListState = lazyListState,
            items = appList.filter { // filter options based on text field value
                it.appName.contains(selectedText, ignoreCase = true) ||
                        it.packageName.contains(selectedText, ignoreCase = true)
            }.map {
                InstalledAppWrapper(it)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(start = dimensions.x16),
            charBoxWidth = dimensions.x64,
            charTextStyle = typography.h1.copy(fontSize = 39.sp),
            content = { items ->
                itemsIndexed(
                    items = items,
                    key = { index, _ ->
                        items[index].installedApp.packageName
                    }
                ) { index, wrapper ->
                    val verticalModifier = when (index) {
                        (items.size - 1) -> { // last item
                            Modifier.padding(
                                top = dimensions.x2,
                                bottom = dimensions.x16 + dimensions.x8
                            )
                        }
                        else -> {
                            Modifier.padding(
                                top = dimensions.x2,
                                bottom = 0.dp
                            )
                        }
                    }
                    InstalledAppRow(
                        modifier = verticalModifier,
                        lazyListState = lazyListState,
                        app = wrapper.installedApp
                    ) {
                        onAppSelected(wrapper.installedApp)
                    }
                }
            },
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalTextApi
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
private fun Preview() {
    AppTheme {
        val mockApps = listOf(
            InstalledApp("App 1", "com.domain.app1", "domain"),
            InstalledApp("App 2", "com.domain.app2", "domain"),
            InstalledApp("App 3", "com.domain.app3", "domain"),
            InstalledApp("BApp 1", "com.domain.app1", "domain"),
            InstalledApp("BApp 2", "com.domain.app2", "domain"),
            InstalledApp("CApp 1", "com.domain.app1", "domain"),
            InstalledApp("DApp 1", "com.domain.app1", "domain"),
            InstalledApp("DApp 2", "com.domain.app2", "domain"),
            InstalledApp("DApp 3", "com.domain.app3", "domain"),
            InstalledApp("DApp 4", "com.domain.app4", "domain"),
            InstalledApp("DApp 5", "com.domain.app5", "domain"),
            InstalledApp("DApp 6", "com.domain.app6", "domain"),
            InstalledApp("DApp 7", "com.domain.app7", "domain"),
        )
        InstalledAppsScreen(
            appList = mockApps,
            onAppSelected = {},
            expandBottomSheet = {},
        )
    }
}
