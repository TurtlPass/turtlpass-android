package com.turtlpass.module.pin

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.R
import com.turtlpass.common.compose.input.DragHandle
import com.turtlpass.module.pin.ui.KeyPad
import com.turtlpass.module.pin.ui.PinIndicator
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import kotlinx.coroutines.delay

@ExperimentalMaterialApi
@Composable
fun UserPinScreen(
    pinLength: Int = 6,
    onPinCompleted: (pin: List<Int>) -> Unit,
) {
    val pinInput = remember { mutableStateListOf<Int>() }

    LaunchedEffect(pinInput) {
        snapshotFlow { pinInput.size }.collect { size ->
            if (size == pinLength) {
                delay(300)
                onPinCompleted(pinInput)
                pinInput.clear()
            }
        }
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = dimensions.x32)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DragHandle(
            modifier = Modifier
                .padding(
                    top = dimensions.x16,
                    bottom = dimensions.x16
                ),
        )
        Text(
            modifier = Modifier
                .padding(
                    top = dimensions.x32,
                    bottom = dimensions.x16
                )
                .fillMaxWidth(),
            text = stringResource(R.string.enter_pin),
            textAlign = TextAlign.Center,
            style = typography.h2.copy(
                color = colors.default.accent,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Row(
            modifier = Modifier
                .padding(top = dimensions.x32)
        ) {
            (0 until pinLength).forEach {
                PinIndicator(
                    active = pinInput.size > it
                )
            }
        }
        KeyPad(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x64)
                .padding(vertical = dimensions.x32),
            inputPin = pinInput
        )
    }
}

@ExperimentalMaterialApi
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
        UserPinScreen(
            onPinCompleted = {},
        )
    }
}
