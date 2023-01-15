package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.common.compose.button.SecondaryButton
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.AppTheme.typography

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    text: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                vertical = dimensions.x8,
                horizontal = dimensions.x32
            ),
        elevation = 0.dp,
        backgroundColor = colors.default.input,
        shape = RoundedCornerShape(dimensions.cornerRadius),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = dimensions.x16)
                .padding(top = dimensions.x16)
                .padding(bottom = dimensions.x8)
        ) {
            Text(
                modifier = Modifier,
                text = text,
                style = typography.body,
            )
            SecondaryButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(alignment = Alignment.End)
                    .padding(top = dimensions.x2),
                text = buttonText,
                onClick = onClick,
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
        ActionCard(
            text = "Message explaining",
            buttonText = "OK",
            onClick = {}
        )
    }
}
