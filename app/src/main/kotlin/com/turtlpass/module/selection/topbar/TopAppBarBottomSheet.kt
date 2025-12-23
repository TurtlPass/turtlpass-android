package com.turtlpass.module.selection.topbar

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.ui.icons.ArrowBack24Px
import com.turtlpass.ui.icons.Close24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBottomSheet(
    title: String,
    onCancel: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(
            top = 0.dp,   // ignore status bar inset
            bottom = 0.dp,
            left = 0.dp,
            right = 0.dp
        ),
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 0.dp),
                text = title,
                style = typography.logo.copy(
                    fontSize = 22.sp,
                ),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onCancel?.invoke() }
            ) {
                Icon(
                    imageVector = ArrowBack24Px,
                    contentDescription = "Close",
                    tint = colors.text.primary,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.default.background,
            scrolledContainerColor = colors.default.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
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
private fun Preview() {
    AppTheme {
        TopAppBarBottomSheet(
            title = "Enter your PIN",
            onCancel = {},
        )
    }
}
