package com.turtlpass.usb.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.turtlpass.ui.button.PrimaryButton
import com.turtlpass.ui.icons.Key24Px
import com.turtlpass.ui.icons.Usb24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.usb.R
import com.turtlpass.usb.model.UsbPermission
import com.turtlpass.usb.model.UsbUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectUsbScreen(
    modifier: Modifier = Modifier,
    usbUiState: State<UsbUiState>,
    onUsbRequestPermissionClick: () -> Unit,
    onGetPasswordClick: () -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("lottie_usb_stick.json")
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensions.x16,
                bottom = dimensions.x32
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .padding(top = dimensions.x32)
                .size(200.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        with(usbUiState.value) {
            val text = when {
                isUsbConnected.not() -> ""
                usbPermission == UsbPermission.NotGranted -> stringResource(R.string.feature_usb_authorise_usb_device)
                else -> stringResource(R.string.feature_usb_connect_usb_device_ready)
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
                visible = usbUiState.value.isUsbConnected && usbPermission == UsbPermission.NotGranted,
                buttonText = stringResource(R.string.feature_usb_button_authorise),
                buttonImageVector = Usb24Px,
                onClick = onUsbRequestPermissionClick
            )
            AnimatedButton(
                visible = usbUiState.value.isUsbConnected && usbPermission == UsbPermission.Granted,
                buttonText = stringResource(R.string.feature_usb_button_unlock_now),
                buttonImageVector = Key24Px,
                onClick = onGetPasswordClick
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
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
    override val values = sequenceOf(UsbPermission.NotGranted, UsbPermission.Granted)
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
        ConnectUsbScreen(
            modifier = Modifier.fillMaxHeight(),
//            modifier = Modifier.wrapContentHeight(),
            usbUiState = remember {
                mutableStateOf(
                    UsbUiState(
                        isUsbConnected = true,
                        usbPermission = item
                    )
                )
            },
            onUsbRequestPermissionClick = {},
            onGetPasswordClick = {},
        )
    }
}
