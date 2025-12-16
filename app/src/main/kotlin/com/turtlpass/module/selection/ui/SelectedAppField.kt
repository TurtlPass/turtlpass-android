package com.turtlpass.module.selection.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.turtlpass.R
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.ui.InstalledAppImage
import com.turtlpass.module.selection.model.SelectionInput
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appOutlinedTextFieldColors
import com.turtlpass.ui.theme.appTextSelectionColors

@Composable
fun SelectedAppField(
    modifier: Modifier = Modifier,
    selectionUiState: State<SelectionUiState>,
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides appTextSelectionColors()
    ) {
        OutlinedTextField(
            modifier = modifier,
            leadingIcon = {
                InstalledAppImage(
                    modifier = Modifier.requiredSize(dimensions.iconFieldSize),
                    installedApp = selectionUiState.value.model.selectedApp,
                    showShimmer = false
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.application),
                    color = colors.text.body,
                    style = typography.title.copy(
                        fontSize = 14.sp
                    ),
                )
            },
            textStyle = typography.title.copy(fontWeight = FontWeight.Normal),
            value = selectionUiState.value.model.selectedApp?.appName ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(dimensions.cornerRadius),
            colors = appOutlinedTextFieldColors()
        )
    }
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
private fun Preview() {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            SelectedAppField(
                modifier = Modifier.fillMaxWidth(),
                selectionUiState = remember {
                    mutableStateOf(
                        SelectionUiState(
                            model = SelectionInput(
                                selectedApp = InstalledAppUi(
                                    appName = "TurtlPass",
                                    packageName = "com.turtlpass",
                                )
                            )
                        )
                    )
                }
            )
        }
    }
}
