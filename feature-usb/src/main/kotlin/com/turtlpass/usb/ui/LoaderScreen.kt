package com.turtlpass.usb.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.turtlpass.ui.button.PrimaryButton
import com.turtlpass.ui.icons.ExitToApp24Px
import com.turtlpass.ui.icons.RestartAlt24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.usb.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoaderScreen(
    modifier: Modifier = Modifier,
    loaderType: LoaderType,
    onCloseClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.x32),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                LoaderType.Loading -> stringResource(R.string.feature_usb_connecting)
                LoaderType.Success -> stringResource(R.string.feature_usb_success_device_ready)
                is LoaderType.Error -> stringResource(
                    id = R.string.feature_usb_error_message,
                    loaderType.errorMessage
                )
            },
            textAlign = TextAlign.Center,
            style = typography.h2,
        )
        AnimatedVisibility(visible = loaderType is LoaderType.Error) {
            PrimaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(horizontal = dimensions.x32)
                    .padding(bottom = dimensions.x16),
                backgroundColor = colors.default.error,
                imageVector = RestartAlt24Px,
                text = stringResource(R.string.feature_usb_try_again),
                onClick = onRetryClick,
            )
        }
        AnimatedVisibility(visible = loaderType != LoaderType.Loading) {
            PrimaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(horizontal = dimensions.x32)
                    .padding(bottom = dimensions.x16),
                backgroundColor = colors.default.button,
                imageVector = ExitToApp24Px,
                text = stringResource(R.string.feature_usb_close),
                onClick = onCloseClick,
            )
        }
    }
}

private class LoaderTypeProvider : PreviewParameterProvider<LoaderType> {
    override val values =
        sequenceOf(LoaderType.Loading, LoaderType.Success, LoaderType.Error("SEED_NOT_INITIALIZED"))
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
    @PreviewParameter(LoaderTypeProvider::class) item: LoaderType
) {
    AppTheme {
        LoaderScreen(
            modifier = Modifier.fillMaxHeight(),
//            modifier = Modifier.wrapContentHeight(),
            loaderType = item,
            onCloseClick = {},
            onRetryClick = {}
        )
    }
}
