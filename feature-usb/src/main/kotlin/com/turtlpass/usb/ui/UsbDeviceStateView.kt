package com.turtlpass.usb.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appIndication
import com.turtlpass.usb.R
import com.turtlpass.usb.model.UsbDeviceUiState

@Composable
fun UsbDeviceStateView(
    modifier: Modifier = Modifier,
    usbDeviceUiState: UsbDeviceUiState,
    onUsbRequestPermissionClick: () -> Unit,
) {
    val lightText = usbDeviceUiState != UsbDeviceUiState.AttachedWithoutPermission

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(
                if (usbDeviceUiState == UsbDeviceUiState.AttachedWithoutPermission) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = appIndication(),
                    onClick = onUsbRequestPermissionClick
                ) else Modifier
            ),
    ) {
        Row(
            modifier = modifier
                .padding(bottom = dimensions.x4)
                .fillMaxWidth()
                .wrapContentHeight()
                .then(
                    if (usbDeviceUiState == UsbDeviceUiState.AttachedWithoutPermission) Modifier.background(
                        usbDeviceUiState.primaryColorUsbDevice()
                    ) else Modifier
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (usbDeviceUiState) {
                    UsbDeviceUiState.Attached -> Usb24Px
                    UsbDeviceUiState.Detached -> UsbOff24Px
                    UsbDeviceUiState.AttachedWithoutPermission -> KeyVertical24Px
                },
                modifier = Modifier.size(12.dp),
                contentDescription = null,
                tint = if (lightText) Color.White else Color.Black,
            )
            Text(
                modifier = Modifier.padding(horizontal = 3.dp),
                text = when (usbDeviceUiState) {
                    UsbDeviceUiState.Attached -> {
                        stringResource(R.string.feature_usb_state_attached)
                    }

                    UsbDeviceUiState.Detached -> {
                        stringResource(R.string.feature_usb_state_detached)
                    }

                    UsbDeviceUiState.AttachedWithoutPermission -> {
                        stringResource(R.string.feature_usb_state_missing_permission)
                    }
                },
                style = typography.subtitle.copy(
                    color = if (lightText) Color.White else Color.Black,
                    fontWeight = if (lightText) FontWeight.Normal else FontWeight.SemiBold,
                ),
            )
        }
    }
}

private class UsbDeviceStateUiProvider : PreviewParameterProvider<UsbDeviceUiState> {
    override val values = sequenceOf(
        UsbDeviceUiState.Attached,
        UsbDeviceUiState.Detached,
        UsbDeviceUiState.AttachedWithoutPermission,
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
            usbDeviceUiState = item,
            onUsbRequestPermissionClick = {}
        )
    }
}
