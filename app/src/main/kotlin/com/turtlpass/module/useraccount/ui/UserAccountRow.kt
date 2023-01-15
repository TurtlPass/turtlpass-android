package com.turtlpass.module.useraccount.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.theme.AppRipple
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography

@Composable
fun UserAccountRow(
    modifier: Modifier = Modifier,
    accountItem: UserAccount,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalRippleTheme provides AppRipple
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.x16)
                .clip(shape = RoundedCornerShape(dimensions.cornerRadius))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = onClick
                )
                .padding(horizontal = dimensions.x8)
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensions.x8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAccountImage(
                modifier = Modifier
                    .requiredSize(38.dp),
                userAccount = accountItem,
                showPlaceholder = true,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = accountItem.accountId,
                style = typography.title,
            )
        }
    }
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
private fun Preview() {
    AppTheme {
        UserAccountRow(
            accountItem = UserAccount("android@turtlpass.com", null),
            onClick = {}
        )
    }
}
