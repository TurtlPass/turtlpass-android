package com.turtlpass.module.main.ui.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.turtlpass.module.main.navigation.NavigationItem
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.Blackish
import com.turtlpass.ui.theme.Zomp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun BottomNavigationBar(navController: NavController, hazeState: HazeState) {
    val items = listOf(
        NavigationItem.App,
        NavigationItem.Domain,
    )
    //var selectedItem by remember { mutableIntStateOf(0) }
    var selectedItem: Int = remember { 0 } //fixme
//    var selectedItem: MutableState<Int> = remember { mutableStateOf(1) }

    var currentRoute by remember { mutableStateOf(NavigationItem.App.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar(
        modifier = Modifier
            // bottom haze
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(containerColor = Color.White)
            ) {
//                progressive = HazeProgressive.verticalGradient(
//                    startIntensity = 0f,
//                    endIntensity = 1f,
//                    preferPerformance = true
//                )
            },
            // subtle gradient to fade items into haze
//            .drawBehind {
//                val gradientHeightPx = 40.dp.toPx() // height of the fade
//                drawRect(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent,
//                            Color.White.copy(alpha = 0.2f) // subtle fade into haze
//                        ),
//                        startY = 0f,
//                        endY = gradientHeightPx
//                    ),
//                    topLeft = Offset(0f, 0f),
//                    size = Size(size.width, gradientHeightPx)
//                )
//            },
                containerColor = Color.Transparent,
//        containerColor = colors.default.background,
    ) {
        items.forEachIndexed { index, item ->

            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        item.icon!!,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        item.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = typography.navigationBar
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults
                    .colors(
//                        indicatorColor = MagicMint,
                        indicatorColor = Zomp.copy(alpha = 0.7f),
//                        indicatorColor = Zomp2,
//                        indicatorColor = MintCream,
                        selectedTextColor = Blackish,
                        selectedIconColor = Blackish,
                        unselectedTextColor = Blackish,
                        unselectedIconColor = Blackish,
                    )
            )
        }
    }
}
