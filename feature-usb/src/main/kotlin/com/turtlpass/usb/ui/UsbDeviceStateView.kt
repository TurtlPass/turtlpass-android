package com.turtlpass.usb.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.icons.KeyVertical24Px
import com.turtlpass.ui.icons.Usb24Px
import com.turtlpass.ui.icons.UsbOff24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.usb.model.UsbDeviceUiState


@Composable
fun UsbDeviceStateView(usbDeviceUiState: UsbDeviceUiState, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 3.dp),
            text = when (usbDeviceUiState) {
                UsbDeviceUiState.Attached -> {
                    "TurtlPass device is connected"
                }

                UsbDeviceUiState.Detached -> {
                    "TurtlPass device is disconnected"
                }

                UsbDeviceUiState.MissingPermission -> {
                    "TurtlPass device needs USB permission"
                }
            },
            style = typography.subtitle.copy(
                color = Color.White,
                fontWeight = FontWeight.Normal,
            ),
        )
        Icon(
            imageVector = when (usbDeviceUiState) {
                UsbDeviceUiState.Attached -> Usb24Px
                UsbDeviceUiState.Detached -> UsbOff24Px
                UsbDeviceUiState.MissingPermission -> KeyVertical24Px
            },
            modifier = Modifier
                .size(12.dp),
            contentDescription = null,
            tint = Color.White
        )
    }
}

private class UsbDeviceStateUiProvider : PreviewParameterProvider<UsbDeviceUiState> {
    override val values = sequenceOf(
        UsbDeviceUiState.Attached,
        UsbDeviceUiState.Detached,
        UsbDeviceUiState.MissingPermission,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview(
    @PreviewParameter(UsbDeviceStateUiProvider::class) item: UsbDeviceUiState
) {
    AppTheme {
        UsbDeviceStateView(
            usbDeviceUiState = item, backgroundColor = colorUsbDevice(item)
        )
    }
}
