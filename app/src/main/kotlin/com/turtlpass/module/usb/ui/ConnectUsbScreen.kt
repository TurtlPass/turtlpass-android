package com.turtlpass.module.usb.ui

import android.content.res.Configuration
import android.hardware.usb.UsbDevice
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.turtlpass.R
import com.turtlpass.common.compose.button.PrimaryButton
import com.turtlpass.common.compose.input.DragHandle
import com.turtlpass.module.chooser.UsbPermission
import com.turtlpass.module.chooser.UsbState
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.Security
import com.turtlpass.theme.icons.Usb

@ExperimentalMaterialApi
@Composable
fun ConnectUsbScreen(
    usbState: State<UsbState>,
    onRequestUsbPermission: (usbDevice: UsbDevice) -> Unit,
    onReadyClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("lottie_usb_stick.json")
    )
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                top = dimensions.x16,
                bottom = dimensions.x32
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        DragHandle()

        LottieAnimation(
            modifier = Modifier
                .padding(top = dimensions.x32)
                .size(200.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        with(usbState.value) {
            val text = when {
                usbDevice == null -> stringResource(R.string.connect_usb_device)
                usbPermission == UsbPermission.NotGranted -> stringResource(R.string.authorise_usb_device)
                else -> stringResource(R.string.connect_usb_device_ready)
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = dimensions.x8,
                        bottom = dimensions.x32,
                    ),
                text = text,
                textAlign = TextAlign.Center,
                style = typography.h2,
            )
            AnimatedButton(
                visible = usbDevice != null && usbPermission == UsbPermission.NotGranted,
                buttonText = stringResource(R.string.button_authorise),
                buttonImageVector = Icons.Rounded.Usb,
                onClick = {
                    if (usbDevice != null) onRequestUsbPermission(usbDevice)
                }
            )
            AnimatedButton(
                visible = usbDevice != null && usbPermission == UsbPermission.Granted,
                buttonText = stringResource(R.string.button_unlock_now),
                buttonImageVector = Icons.Rounded.Security,
                onClick = onReadyClick
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ColumnScope.AnimatedButton(
    visible: Boolean,
    buttonText: String,
    buttonImageVector: ImageVector,
    buttonColor: Color = colors.default.button,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(initialAlpha = 0.4f),
        exit = fadeOut(animationSpec = tween(durationMillis = 250))
    ) {
        PrimaryButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(horizontal = dimensions.x32)
                .padding(bottom = dimensions.x16),
            imageVector = buttonImageVector,
            text = buttonText,
            enabled = true,
            onClick = { if (visible) onClick() },
            backgroundColor = buttonColor,
        )
    }
}

private class UsbPermissionProvider : PreviewParameterProvider<UsbPermission> {
    override val values = sequenceOf( UsbPermission.NotGranted, UsbPermission.Granted)
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
private fun Preview(
    @PreviewParameter(UsbPermissionProvider::class) item: UsbPermission
) {
    AppTheme {
        val usbState = remember { mutableStateOf(UsbState(usbPermission = item)) }

        ConnectUsbScreen(
            usbState = usbState,
            onRequestUsbPermission = {},
            onReadyClick = {},
        )
    }
}
