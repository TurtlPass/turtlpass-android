package com.turtlpass.urlmanager.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.icons.TravelExplore24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.Grey400
import com.turtlpass.urlmanager.R

@Composable
fun EmptyWebsitesState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = TravelExplore24Px,
            contentDescription = null,
            tint = Grey400,
            modifier = Modifier.size(dimensions.x64)
        )

        Spacer(Modifier.height(dimensions.x16))

        Text(
            text = stringResource(R.string.feature_urlmanager_no_websites_detected_yet),
            style = typography.h2.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        )

        Spacer(Modifier.height(dimensions.x8))

        Text(
            text = stringResource(R.string.feature_urlmanager_start_browsing),
            style = typography.subtitle.copy(
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Grey400
            )
        )
    }
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
/*@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)*/
@Composable
private fun Preview(
) {
    AppTheme {
        EmptyWebsitesState()
    }
}
