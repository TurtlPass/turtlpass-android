package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.R
import com.turtlpass.common.compose.anim.Pulse
import com.turtlpass.module.chooser.UsbPermission
import com.turtlpass.module.chooser.UsbState
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography

@Composable
fun PulseHeader(
    modifier: Modifier = Modifier,
    usbState: State<UsbState>,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.CenterStart
    ) {
        Pulse(
            modifier = Modifier.size(80.dp),
            tint = colors.default.accent,
            initialValue = 0.35f,
            bounded = false,
            enabled = usbState.value.usbDevice != null
                    && usbState.value.usbPermission == UsbPermission.Granted
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = dimensions.x16),
            horizontalArrangement = Arrangement.spacedBy(dimensions.x8)
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.drawable.turtlpass),
                contentDescription = stringResource(R.string.app_logo_content_description),
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = stringResource(R.string.app_name),
                style = typography.logo,
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
    val usbState = remember { mutableStateOf(UsbState()) }

    AppTheme {
        PulseHeader(
            usbState = usbState
        )
    }
}
