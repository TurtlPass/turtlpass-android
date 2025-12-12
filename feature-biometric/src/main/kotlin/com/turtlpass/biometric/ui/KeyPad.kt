package com.turtlpass.biometric.ui

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.ui.icons.Backspace24Px
import com.turtlpass.ui.icons.Fingerprint24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KeyPad(
    modifier: Modifier = Modifier,
    inputPin: SnapshotStateList<Int>,
    fingerprintEnabled: Boolean,
    onFingerprint: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.spacedBy(dimensions.x16),
        userScrollEnabled = false,
    ) {
        items(count = 9) { index ->
            val digit = index + 1
            PinKey(
                pinKeyType = PinKeyType.Text(digit.toString()),
                onClick = { inputPin.add(digit) }
            )
        }
        item {
            if (fingerprintEnabled) {
                PinKey(
                    pinKeyType = PinKeyType.Fingerprint(Fingerprint24Px),
                    onClick = onFingerprint
                )
            } else {
                PinKey(
                    pinKeyType = PinKeyType.None,
                    onClick = null
                )
            }
        }
        item {
            PinKey(
                pinKeyType = PinKeyType.Text("0"),
                onClick = { inputPin.add(0) }
            )
        }
        item {
            PinKey(
                pinKeyType = PinKeyType.Vector(Backspace24Px),
                onClick = {
                    if (inputPin.isNotEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                            inputPin.removeLast()
                        } else {
                            inputPin.removeAt(inputPin.lastIndex)
                        }
                    }
                }
            )
        }
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
        KeyPad(
            inputPin = SnapshotStateList(),
            fingerprintEnabled = true,
            onFingerprint = {},
        )
    }
}
