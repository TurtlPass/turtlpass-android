package com.turtlpass.module.passphrase

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.common.compose.button.PrimaryButton
import com.turtlpass.common.compose.input.PasswordTextField
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.ArrowBack
import com.turtlpass.theme.icons.Fingerprint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalPermissionsApi
@ExperimentalLifecycleComposeApi
@Composable
fun PassphraseScreen(
    viewModel: PassphraseViewModel = hiltViewModel(),
    onNavigateUpClick: () -> Unit,
    onSaveClick: (passphrase: String) -> Unit,
    onPassphraseEncrypted: (Boolean) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var passphrase by remember { mutableStateOf("") }

    uiState.passphraseEncryptResult?.let { result ->
        val currentOnPassphraseEncrypted by rememberUpdatedState(onPassphraseEncrypted)
        LaunchedEffect(uiState) {
            currentOnPassphraseEncrypted(result)
        }
    }

    Column(
        modifier = Modifier
            .background(colors.default.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = {},
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = onNavigateUpClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        tint = colors.default.icon,
                        contentDescription = "Back",
                    )
                }
            },
        )
        Text(
            text = stringResource(id = R.string.rationale_passphrase_title),
            modifier = Modifier
                .padding(horizontal = dimensions.x32),
            style = typography.h2.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.rationale_passphrase),
            modifier = Modifier
                .padding(horizontal = dimensions.x32)
                .padding(top = dimensions.x32),
            style = typography.body,
            textAlign = TextAlign.Center,
        )
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x32)
                .padding(top = dimensions.x32),
            value = passphrase,
            onValueChange = { value ->
                passphrase = value
            },
            label = stringResource(id = R.string.passphrase),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
        )
        PrimaryButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.End)
                .padding(horizontal = dimensions.x32)
                .padding(top = dimensions.x32),
            imageVector = Icons.Rounded.Fingerprint,
            text = stringResource(R.string.button_save),
            enabled = true,
            onClick = {
                focusManager.clearFocus()
                onSaveClick(passphrase)
            },
            backgroundColor = colors.default.button
        )
    }
}

@FlowPreview
@ExperimentalPermissionsApi
@ExperimentalLifecycleComposeApi
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
    backgroundColor = 0xff303030,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        PassphraseScreen(
            onNavigateUpClick = {},
            onSaveClick = {},
            onPassphraseEncrypted = {},
        )
    }
}
