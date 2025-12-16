package com.turtlpass.useraccount.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appIndication
import com.turtlpass.useraccount.model.UserAccount

@Composable
fun UserAccountRow(
    modifier: Modifier = Modifier,
    accountItem: UserAccount,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.x16)
            .clip(shape = RoundedCornerShape(dimensions.cornerRadius))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = appIndication(),
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
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = accountItem.accountId,
            style = typography.title,
        )
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
        UserAccountRow(
            accountItem = UserAccount("android@turtlpass.com", null),
            onClick = {}
        )
    }
}
