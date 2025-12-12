package com.turtlpass.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turtlpass.ui.button.SecondaryButton
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    text: String,
    buttonText: String,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Card(
        modifier = modifier,
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(dimensions.cornerRadius),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = dimensions.x16)
                .padding(top = dimensions.x16)
                .padding(bottom = dimensions.x8 + dimensions.x2)
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
                    .padding(top = dimensions.x4),
                text = buttonText,
                onClick = onClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffa0,
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
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    vertical = dimensions.x8,
                    horizontal = dimensions.x32
                ),
            text = "Message explaining",
            buttonText = "OK",
            onClick = {},
            backgroundColor = colors.default.cardBackground
        )
    }
}
