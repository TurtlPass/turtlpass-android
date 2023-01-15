package com.turtlpass.module.pin.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.icons.Circle

@Composable
fun PinIndicator(
    modifier: Modifier = Modifier,
    active: Boolean,
) {
    Icon(
        modifier = modifier
            .padding(9.dp)
            .size(18.dp),
        imageVector = Icons.Filled.Circle,
        contentDescription = null,
        tint = if (active) {
            colors.default.accent
        } else {
            colors.default.unfocused
        },
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
private fun Preview() {
    AppTheme {
        Row {
            PinIndicator(active = true)
            PinIndicator(active = true)
            PinIndicator(active = true)
            PinIndicator(active = false)
            PinIndicator(active = false)
            PinIndicator(active = false)
        }
    }
}
