package com.turtlpass.module.selection.topbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.ui.icons.Close24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBottomSheet(
    title: String? = null,
    onCancel: (() -> Unit)? = null,
) {
//    val density = LocalDensity.current
//    val statusBarHeightPx = with(density) {
//        WindowInsets.statusBars.getTop(this).toFloat() + 18.dp.toPx()
//    }

    TopAppBar(
        windowInsets = WindowInsets(
            top = 0.dp,   // ignore status bar inset
            bottom = 0.dp,
            left = 0.dp,
            right = 0.dp
        ),
//        windowInsets = TopAppBarDefaults.windowInsets.ignore(WindowInsetsSides.Top),
        title = {
            if (title == null) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = stringResource(R.string.app_name),
                    style = typography.logo.copy(
                        fontSize = 26.sp,
                    ),
                )
            } else {
                Text(
                    modifier = Modifier
                        .padding(start = 0.dp),
                    text = title,
                    style = typography.logo.copy(
                        fontSize = 26.sp,
                    ),
                )
            }
        },
        navigationIcon = {
            if (title == null) {
                Image(
                    painter = painterResource(id = R.drawable.turtlpass),
                    contentDescription = "TurtlPass Icon",
                    modifier = Modifier
                        .padding(start = 22.dp)
                        .size(dimensions.x32)
                )
            } else {
                IconButton(
//                    modifier = Modifier
//                        .padding(end = 6.dp),
                    onClick = { onCancel?.invoke() }
                ) {
                    Icon(
                        imageVector = Close24Px,
                        contentDescription = "Close",
                        tint = Color.Black,
                    )
                }
            }
        },
//        actions = {
//            onCancel?.let { onClick ->
//
//            }
////            IconButton(
////                modifier = Modifier
////                    .padding(end = 4.5.dp),
////                onClick = onCancelClick) {
////                UserAccountImage(
////                    modifier = Modifier
////                        .requiredSize(dimensions.x32 + dimensions.x2),
////                    userAccount = uiState.value.model.userAccount,
////                    showPlaceholder = true
////                )
////            }
//        },
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
            onCancel = {},
        )
    }
}
