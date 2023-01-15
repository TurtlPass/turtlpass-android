package com.turtlpass.module.loader.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.turtlpass.R
import com.turtlpass.common.compose.button.PrimaryButton
import com.turtlpass.module.chooser.UsbWriteResult
import com.turtlpass.module.loader.LoaderType
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.ExitToApp
import com.turtlpass.theme.icons.RestartAlt

@ExperimentalMaterialApi
@Composable
fun LoaderScreen(
    result: UsbWriteResult?,
    onClick: ((LoaderType) -> Unit),
) {
    val loaderType = when (result) {
        UsbWriteResult.Success -> LoaderType.Success
        UsbWriteResult.Error -> LoaderType.Error
        else -> LoaderType.Loading
    }
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("lottie_loading_success_error.json")
    )
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val progress by animateLottieCompositionAsState(
        composition,
        clipSpec = LottieClipSpec.Frame(
            min = loaderType.minFrame,
            max = loaderType.maxFrame
        ),
        iterations = loaderType.iterations,
        restartOnPlay = true,
        isPlaying = isPlaying,
    )

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = dimensions.x32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(180.dp),
            composition = composition,
            progress = { progress }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensions.x16,
                    bottom = dimensions.x32,
                ),
            text = when (loaderType) {
                LoaderType.Loading -> stringResource(R.string.connecting)
                LoaderType.Success -> stringResource(R.string.success_device_ready)
                LoaderType.Error -> stringResource(R.string.error_try_again)
            },
            textAlign = TextAlign.Center,
            style = typography.h2,
        )
        AnimatedVisibility(visible = loaderType == LoaderType.Success) {
            PrimaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(horizontal = dimensions.x32)
                    .padding(bottom = dimensions.x16),
                backgroundColor = colors.default.button,
                imageVector = Icons.Rounded.ExitToApp,
                text = stringResource(R.string.close),
                onClick = {
                    if (loaderType == LoaderType.Success) {
                        isPlaying = false
                        onClick(loaderType)
                    }
                },
            )
        }
        AnimatedVisibility(visible = loaderType == LoaderType.Error) {
            PrimaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(horizontal = dimensions.x32)
                    .padding(bottom = dimensions.x16),
                backgroundColor = colors.default.error,
                imageVector = Icons.Rounded.RestartAlt,
                text = stringResource(R.string.try_again),
                onClick = {
                    if (loaderType == LoaderType.Error) {
                        isPlaying = false
                        onClick(loaderType)
                    }
                },
            )
        }
    }
}

private class UsbWriteResultProvider : PreviewParameterProvider<UsbWriteResult?> {
    override val values = sequenceOf(null, UsbWriteResult.Success, UsbWriteResult.Error)
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
    @PreviewParameter(UsbWriteResultProvider::class) item: UsbWriteResult?
) {
    AppTheme {
        LoaderScreen(
            result = item,
            onClick = {}
        )
    }
}
