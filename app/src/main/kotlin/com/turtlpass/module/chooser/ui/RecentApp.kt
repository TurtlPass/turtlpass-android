package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp.Companion.Hairline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.R
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.theme.AppRipple
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography
import com.turtlpass.theme.icons.Assistant

@Composable
fun RecentApp(
    modifier: Modifier = Modifier,
    recentApp: InstalledApp,
    onRecentApp: (app: InstalledApp) -> Unit,
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(Hairline)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = dimensions.x2)
                .padding(end = 1.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(dimensions.x16),
                imageVector = Icons.Rounded.Assistant,
                contentDescription = null,
                tint = colors.default.error,
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = dimensions.x2),
                text = stringResource(R.string.recent_app),
                textAlign = TextAlign.Center,
                style = typography.buttonPrimary.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = colors.default.error
                )
            )
        }
        CompositionLocalProvider(
            LocalRippleTheme provides AppRipple
        ) {
            Text(
                modifier = Modifier
                    .background(
                        color = colors.default.input,
                        shape = RoundedCornerShape(dimensions.x32)
                    )
                    .clip(shape = RoundedCornerShape(dimensions.x32))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        onClick = { onRecentApp(recentApp) }
                    )
                    .padding(vertical = dimensions.x2)
                    .padding(horizontal = dimensions.x8),
                text = recentApp.appName,
                textAlign = TextAlign.Center,
                style = typography.buttonSecondary.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.text.secondaryButton
                )
            )
        }
    }
}

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
        Row(modifier = Modifier.padding(dimensions.x8)) {
            RecentApp(
                recentApp = InstalledApp(appName = "Telegram", packageName = "org.telegram"),
                onRecentApp = {},
            )
        }
    }
}
