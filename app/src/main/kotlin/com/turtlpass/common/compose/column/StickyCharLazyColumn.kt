package com.turtlpass.common.compose.column

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import java.util.*


interface StickyChar {
    val char: Char
}

/**
 * Display a sticky char on the left side of the list
 *
 * src: https://fvilarino.medium.com/creating-a-sticky-letter-list-in-jetpack-compose-2e7e9c8f9b91
 */
@ExperimentalMaterialApi
@ExperimentalTextApi
@Composable
fun <T : StickyChar> StickyCharLazyColumn(
    lazyListState: LazyListState,
    items: List<T>,
    modifier: Modifier = Modifier,
    charBoxWidth: Dp = 80.dp,
    charTextStyle: TextStyle = TextStyle(fontSize = 38.sp, fontWeight = FontWeight.Bold),
    charColor: Color = colors.text.accent,
    content: LazyListScope.(items: List<T>) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val charBoxWidthPx = with(density) { charBoxWidth.toPx() }
    var itemHeight by remember { mutableStateOf(0) }
    Box(
        modifier = modifier
            .clipToBounds()
            .drawWithCache {
                onDrawBehind {
                    var initial: Char? = null
                    if (itemHeight == 0) {
                        itemHeight = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                    }
                    lazyListState.layoutInfo.visibleItemsInfo.forEachIndexed { index, itemInfo ->
                        val itemInitial = items.getOrNull(itemInfo.index)?.char
                        if (itemInitial != null && itemInitial != initial) {
                            initial = itemInitial
                            val nextInitial = items.getOrNull(itemInfo.index + 1)?.char
                            val textLayout = textMeasurer.measure(
                                text = AnnotatedString(itemInitial.toString()),
                                style = charTextStyle,
                            )
                            val horizontalOffset = (charBoxWidthPx - textLayout.size.width) / 2
                            val verticalOffset = (itemHeight - textLayout.size.height) / 2
                            drawText(
                                textLayoutResult = textLayout,
                                color = charColor,
                                topLeft = Offset(
                                    x = horizontalOffset,
                                    y = if (index != 0 || itemInitial != nextInitial) {
                                        itemInfo.offset.toFloat()
                                    } else {
                                        0f
                                    } + verticalOffset,
                                ),
                            )
                        }
                    }
                }
            }
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = charBoxWidth)
                .scrollbar(
                    state = lazyListState,
                    horizontal = false,
                    thickness = 6.dp,
                    knobCornerRadius = dimensions.x8,
                    knobColor = colors.default.accent,
                )
        ) {
            content(items)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalTextApi
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.default.background,
        ) {
            StickyCharLazyColumn(
                lazyListState = rememberLazyListState(),
                items = ipsum
                    .filter { it.isLetter() || it == ' ' }
                    .split(' ')
                    .filter { word -> word.length > 2 }
                    .distinct()
                    .map { word ->
                        PreviewCharWrapper(
                            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                        )
                    }.sortedBy { item ->
                        item.description.uppercase()
                    },
                modifier = Modifier.fillMaxSize(),
                charBoxWidth = 80.dp,
                content = { items ->
                    items(items) { wrapper ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(
                                    text = wrapper.description,
                                    style = AppTheme.typography.body,
                                )
                            }
                            Divider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            )
        }
    }
}

class PreviewCharWrapper(
    val description: String
) : StickyChar {
    override val char: Char
        get() = description.first()
}

private const val ipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
        "Nullam consectetur accumsan leo at consectetur. In ac libero at risus tempus congue. " +
        "Vivamus sem quam, pellentesque quis orci ac, ultrices posuere lectus. " +
        "Nulla nec turpis nec eros condimentum iaculis a nec elit. " +
        "Nam malesuada vitae urna non accumsan. Vestibulum suscipit nisl sed sodales hendrerit. " +
        "Ut iaculis elementum augue, id consequat erat sodales at. " +
        "Cras non sem dictum, accumsan tortor id, laoreet odio. " +
        "Mauris quis libero aliquet, efficitur tellus dictum, laoreet enim. " +
        "Integer eleifend nunc et magna fringilla, at dictum ex posuere. " +
        "Vivamus commodo ex a mi mollis dictum. " +
        "Aliquam arcu tellus, venenatis id condimentum a, hendrerit non mi. " +
        "Mauris at tellus ac risus condimentum rhoncus."
