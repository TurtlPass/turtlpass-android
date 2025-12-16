package com.turtlpass.module.selection.deviceFlow

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
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
import com.turtlpass.usb.ui.colorUsbDevice

@Composable
fun BottomSheetContainer(
    usbUiState: State<UsbUiState>,
    title: String? = null,
    onCancel: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    val statusBarColor = colorUsbDevice(usbUiState.value.usbDeviceUiState)
    val animatedStatusBarColor by animateColorAsState(
        targetValue = statusBarColor,
        animationSpec = tween(durationMillis = 500)
    )

    Column(
        Modifier.wrapContentHeight()
    ) {
        /*Box(
            modifier = Modifier
                .background(animatedStatusBarColor)
                .fillMaxWidth()
        ) {
            // DragHandle
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .padding(
                        top = dimensions.x16,
                        bottom = dimensions.x8
                    )
                    .height(5.dp)
                    .width(38.dp)
                    .clip(shape = RoundedCornerShape(dimensions.x16))
                    .background(Color.White)
            )
        }*/

        UsbDeviceStateView(
            usbDeviceUiState = usbUiState.value.usbDeviceUiState,
            backgroundColor = animatedStatusBarColor
        )

        if (title == null) {
            TopAppBarBottomSheetLogo()
        } else {
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
                onCancel = null,
                content = {}
            )
        }
    }
}
