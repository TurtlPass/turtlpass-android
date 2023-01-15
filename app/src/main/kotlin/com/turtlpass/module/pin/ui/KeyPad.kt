package com.turtlpass.module.pin.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.module.pin.PinKeyType
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.icons.Backspace

@ExperimentalMaterialApi
@Composable
fun KeyPad(
    modifier: Modifier = Modifier,
    inputPin: SnapshotStateList<Int>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalArrangement = Arrangement.Center,
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
            PinKey(
                pinKeyType = PinKeyType.None,
                onClick = null
            )
        }
        item {
            PinKey(
                pinKeyType = PinKeyType.Text("0"),
                onClick = { inputPin.add(0) }
            )
        }
        item {
            PinKey(
                pinKeyType = PinKeyType.Vector(Icons.Rounded.Backspace),
                onClick = {
                    if (inputPin.isNotEmpty()) {
                        inputPin.removeLast()
                    }
                }
            )
        }
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
        KeyPad(inputPin = SnapshotStateList())
    }
}
