package com.turtlpass.module.main.ui.topbar

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.useraccount.ui.UserAccountImage
import com.turtlpass.useraccount.viewmodel.UserAccountUiState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun TopAppBarMain(
    userAccountUiState: State<UserAccountUiState>,
    hazeState: HazeState,
    scrollBehavior: TopAppBarScrollBehavior,
    onAccountPickerRequested: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            // top haze
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(containerColor = Color.White)
            ) {
//                progressive = HazeProgressive.verticalGradient(
//                    startIntensity = 1f, // fully opaque at top
//                    endIntensity = 0f,   // fully transparent at bottom
//                    preferPerformance = true
//                )
            },
            // subtle gradient to fade items into haze
//            .drawBehind {
//                val gradientHeightPx = 40.dp.toPx()
//                drawRect(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.White.copy(alpha = 0.2f), // subtle fade
//                            Color.Transparent
//                        ),
//                        startY = 0f,
//                        endY = gradientHeightPx
//                    ),
//                    size = Size(size.width, gradientHeightPx)
//                )
//            },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets(
            top = 0.dp,   // ignore status bar inset
            bottom = 0.dp,
            left = 0.dp,
            right = 0.dp
        ),
        title = {
            Text(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = stringResource(R.string.app_name),
                style = typography.logo,
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.turtlpass),
                contentDescription = "TurtlPass Icon",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(dimensions.x32 + dimensions.x2)
            )
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(end = 4.5.dp),
                onClick = onAccountPickerRequested
            ) {
                UserAccountImage(
                    modifier = Modifier
                        .requiredSize(dimensions.x32 + dimensions.x2),
                    userAccount = userAccountUiState.value.selectedAccount,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
//            containerColor = colors.default.background,
//            scrolledContainerColor = colors.default.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
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
        TopAppBarMain(
            hazeState = rememberHazeState(),
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            userAccountUiState = remember { mutableStateOf(UserAccountUiState()) },
            onAccountPickerRequested = {},
        )
    }
}
