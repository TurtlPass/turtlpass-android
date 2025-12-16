package com.turtlpass.biometric.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.turtlpass.biometric.R
import com.turtlpass.biometric.viewmodel.BiometricUiState
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserPinScreen(
    modifier: Modifier = Modifier,
    biometricUiState: State<BiometricUiState>,
    pinLength: Int = 6,
    onPinCompleted: (pin: List<Int>, enableFingerprint: Boolean) -> Unit,
    onFingerprint: () -> Unit,
) {
    val pinInput = remember { mutableStateListOf<Int>() }
    var enableFingerprint by remember { mutableStateOf(false) }

    LaunchedEffect(pinInput) {
        snapshotFlow { pinInput.size }.collect { size ->
            if (size == pinLength) {
                delay(300)
                onPinCompleted(pinInput, enableFingerprint)
                pinInput.clear()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensions.x16)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = dimensions.x32 + dimensions.x16,
                    bottom = dimensions.x16
                )
        ) {
            (0 until pinLength).forEach { index ->
                PinIndicator(
                    modifier = Modifier.padding(dimensions.x2),
                    index = index,
                    active = pinInput.size > index
                )
            }
        }
        KeyPad(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x32)
                .padding(
                    top = dimensions.x32,
                    bottom = dimensions.x16
                ),
            inputPin = pinInput,
            fingerprintEnabled = biometricUiState.value.isBiometricAvailable
                    && biometricUiState.value.isBiometricEnabled,
            onFingerprint = onFingerprint,
        )
        if (biometricUiState.value.isBiometricAvailable
            && biometricUiState.value.isBiometricEnabled.not()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = dimensions.x16)
                    .padding(end = dimensions.x16 + dimensions.x8)
                    .padding(top = dimensions.x16)
                    .padding(bottom = dimensions.x16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = enableFingerprint,
                    onCheckedChange = { enableFingerprint = it },
                    colors = CheckboxDefaults.colors(
                        checkmarkColor = colors.default.accent,
                        checkedColor = colors.text.body,
                        uncheckedColor = colors.text.body,
                    ),
                )
                Text(
                    text = stringResource(R.string.feature_biometric_use_fingerprint),
                    style = typography.body.copy(
                        color = colors.text.body,
                    ),
                )
            }
        }
    }
}

private class FingerprintEnabledProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
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
    @PreviewParameter(FingerprintEnabledProvider::class) item: Boolean
) {
    AppTheme {
        UserPinScreen(
            modifier = Modifier.fillMaxHeight(),
//            modifier = Modifier.wrapContentHeight(),
            biometricUiState = remember {
                mutableStateOf(
                    BiometricUiState(
                        isBiometricAvailable = true,
                        isBiometricEnabled = item
                    )
                )
            },
            onPinCompleted = { _, _ -> },
            onFingerprint = {},
        )
    }
}
