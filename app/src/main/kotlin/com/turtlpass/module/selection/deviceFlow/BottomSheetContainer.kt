package com.turtlpass.module.selection.deviceFlow

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.turtlpass.module.selection.topbar.TopAppBarBottomSheet
import com.turtlpass.module.selection.topbar.TopAppBarBottomSheetLogo
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.usb.model.UsbDeviceUiState
import com.turtlpass.usb.model.UsbUiState
import com.turtlpass.usb.ui.UsbDeviceStateView
import com.turtlpass.usb.ui.rememberStripeBrush

@Composable
fun BottomSheetContainer(
    usbUiState: State<UsbUiState>,
    onUsbRequestPermissionClick: () -> Unit,
    title: String? = null,
    onCancel: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    val statusBarBrush = rememberStripeBrush(
        state = usbUiState.value.usbDeviceUiState
    )
    Column(
        Modifier.wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.background(statusBarBrush)
        ) {
            UsbDeviceStateView(
                modifier = Modifier.padding(top = dimensions.x4),
                usbDeviceUiState = usbUiState.value.usbDeviceUiState,
                onUsbRequestPermissionClick = onUsbRequestPermissionClick
            )
        }
        if (title == null) {
            TopAppBarBottomSheetLogo()
        } else if (onCancel != null) {
            TopAppBarBottomSheet(
                title = title,
                onCancel = onCancel,
            )
        }
        content()
    }
}

private class UsbStateProvider : PreviewParameterProvider<UsbDeviceUiState> {
    override val values = sequenceOf(UsbDeviceUiState.Attached, UsbDeviceUiState.Detached)
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
    @PreviewParameter(UsbStateProvider::class) item: UsbDeviceUiState
) {
    AppTheme {
        Surface(
            shape = RoundedCornerShape(
                topStart = dimensions.cornerRadius,
                topEnd = dimensions.cornerRadius
            ),
        ) {
            BottomSheetContainer(
                usbUiState = remember { mutableStateOf(UsbUiState(usbDeviceUiState = item)) },
                onUsbRequestPermissionClick = {},
                onCancel = null,
                content = {},
            )
        }
    }
}
