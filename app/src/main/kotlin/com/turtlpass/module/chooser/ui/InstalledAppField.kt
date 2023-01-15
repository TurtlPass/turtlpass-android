package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.R
import com.turtlpass.common.compose.input.DropdownField
import com.turtlpass.common.compose.input.IconDropdown
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.ChooserUiState
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.installedapp.ui.InstalledAppImage
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.appTextSelectionColors

@Composable
fun InstalledAppField(
    modifier: Modifier = Modifier,
    uiState: State<ChooserUiState>,
    onClick: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalTextSelectionColors provides appTextSelectionColors()
    ) {
        DropdownField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    focused = focusState.isFocused
                },
            label = stringResource(R.string.input_label_choose_app),
            value = uiState.value.model.installedApp?.appName ?: "",
            focused = focused,
            readOnly = true,
            onClick = onClick,
            leadingIcon = {
                InstalledAppImage(
                    modifier = Modifier
                        .requiredSize(28.dp),
                    installedApp = uiState.value.model.installedApp,
                    showShimmer = false
                )
            },
            trailingIcon = if (uiState.value.installedAppsResult.isError()) null else {
                {
                    if (uiState.value.installedAppsResult.isLoading()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensions.x16),
                            color = colors.default.icon,
                            strokeWidth = dimensions.x2
                        )
                    } else if (uiState.value.installedAppsResult.isSuccessful()) {
                        IconDropdown(expanded = focused)
                    }
                }
            }
        )
    }
}

private class AppResultFieldProvider : PreviewParameterProvider<Result<List<InstalledApp>>> {
    override val values = sequenceOf(
        Result.Loading,
        Result.Success(listOf(InstalledApp("TurtlPass", "com.turtlpass"))),
        Result.Error()
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
    @PreviewParameter(AppResultFieldProvider::class) item: Result<List<InstalledApp>>
) {
    val uiState = remember {
        mutableStateOf(
            ChooserUiState(
                model = ChooserInputs(),
                installedAppsResult = item,
            )
        )
    }
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            InstalledAppField(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
                onClick = {},
            )
        }
    }
}
