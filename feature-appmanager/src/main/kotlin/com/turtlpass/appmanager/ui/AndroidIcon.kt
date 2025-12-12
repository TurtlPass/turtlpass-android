package com.turtlpass.appmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AndroidIcon(
    modifier: Modifier = Modifier,
    app: InstalledAppUi,
) {
    AndroidIconLayout(
        modifier = modifier,
        image = {
            AndroidAppImage(
                modifier = Modifier
                    .padding(horizontal = dimensions.x8)
                    .aspectRatio(1f)
                    .fillMaxSize(),
                packageName = app.packageName,
                placeholderPadding = false
            )
        },
        label = {
            Text(
                modifier = Modifier
                    .padding(top = dimensions.x4)
                    .fillMaxWidth(),
                text = app.appName,
                style = typography.body.copy(fontSize = 12.sp),
                maxLines = 1,
                overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center
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
        AndroidIcon(
            app = InstalledAppUi("TurtlPass","com.turtlpass"),
        )
    }
}
