package com.turtlpass.module.selection.ui

import android.content.res.Configuration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.model.WebsiteUi
import com.turtlpass.module.selection.model.SelectionInput
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.appOutlinedTextFieldColorsM3
import com.turtlpass.ui.theme.appTextSelectionColors
import com.turtlpass.urlmanager.ui.FaviconImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedWebsiteField(
    modifier: Modifier = Modifier,
    uiState: State<SelectionUiState>,
) {
    val textFieldValue = domainTextHighlightedValue(uiState.value.model.selectedUrl)
    val interactionSource = remember { MutableInteractionSource() }
    val fieldColors = appOutlinedTextFieldColorsM3(backgroundColor = colors.default.background)

    CompositionLocalProvider(
        LocalTextSelectionColors provides appTextSelectionColors()
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = { /* read-only, do nothing */ },
            modifier = modifier,
            readOnly = true,
            enabled = false,
            textStyle = typography.title.copy(
                fontWeight = FontWeight.Normal,
                color = colors.text.body
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = textFieldValue.text,
                    innerTextField = innerTextField,
                    enabled = false,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    colors = fieldColors,
                    interactionSource = interactionSource,
                    contentPadding = OutlinedTextFieldDefaults.contentPadding(),
                    leadingIcon = {
                        FaviconImage(
                            modifier = Modifier.size(28.dp),
                            url = uiState.value.model.selectedUrl?.faviconUrl ?: ""
                        )
                    },
                    label = {
                        Text(
                            text = "Website",
                            color = colors.text.body,
                            style = typography.title.copy(fontSize = 14.sp),
                        )
                    },
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = false,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = fieldColors,
                            shape = RoundedCornerShape(dimensions.cornerRadius),
                            focusedBorderThickness = OutlinedTextFieldDefaults.FocusedBorderThickness,
                            unfocusedBorderThickness = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                        )
                    }
                )
            }
        )
    }
}

private fun domainTextHighlightedValue(website: WebsiteUi?): TextFieldValue {
    if (website == null) return TextFieldValue(AnnotatedString(text = ""))

    val text = website.url
    val highlight = website.topLevelDomain

    val annotatedString = if (highlight.isNotEmpty() && highlight.length < text.length) {
        val start = text.indexOf(highlight)
        if (start != -1) {
            AnnotatedString(
                text = text, spanStyles = listOf(
                    AnnotatedString.Range(
                        SpanStyle(fontWeight = FontWeight.SemiBold),
                        start = start,
                        end = start + highlight.length
                    )
                )
            )
        } else {
            AnnotatedString(text = text)
        }
    } else {
        AnnotatedString(text = text)
    }
    return TextFieldValue(annotatedString)
}

private class WebDomainEnabledProvider : PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(
        true, false
    )
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
private fun Preview(
    @PreviewParameter(WebDomainEnabledProvider::class) item: Boolean
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            SelectedWebsiteField(
                modifier = Modifier.fillMaxWidth(),
                uiState = remember {
                    mutableStateOf(
                        SelectionUiState(
                            model = SelectionInput(
                                selectedUrl = WebsiteUi(
                                    packageName = "com.android.chrome",
                                    url = "https://www.google.com",
                                    timestamp = 1717987067L,
                                )
                            ),
                        )
                    )
                },
            )
        }
    }
}
