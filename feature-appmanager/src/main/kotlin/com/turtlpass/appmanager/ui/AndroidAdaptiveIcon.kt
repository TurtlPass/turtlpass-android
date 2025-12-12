package com.turtlpass.appmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AndroidAdaptiveIcon(
    modifier: Modifier = Modifier,
    app: InstalledAppUi,
    onClick: () -> Unit,
) {
    AndroidAdaptiveIconLayout(
        modifier = modifier,
        drawable = {
            InstalledAppAdaptiveDrawable(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(150.dp),
                packageName = app.packageName,
                onClick = onClick
            )
        },
        label = {
            Text(
                modifier = Modifier
                    .padding(top = dimensions.x8)
                    .padding(bottom = dimensions.x2)
                    .padding(horizontal = dimensions.x4)
                    .wrapContentSize(),
                text = app.appName,
                textAlign = TextAlign.Center,
                style = typography.body.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
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
private fun Preview() {
    AppTheme {
        AndroidAdaptiveIcon(
            app = InstalledAppUi(
                packageName = "com.turtlpass",
                appName = "TurtlPass",
            ),
            onClick = {}
        )
    }
}
