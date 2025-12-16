package com.turtlpass.module.main.ui.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.turtlpass.module.main.navigation.NavigationItem
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.ui.theme.Zomp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun BottomNavigationBar(navController: NavController, hazeState: HazeState) {
    val items = remember {
        listOf(
            NavigationItem.App,
            NavigationItem.Domain,
        )
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.ultraThin(containerColor = colors.default.background)
            ),
            containerColor = Color.Transparent,
    ) {
        items.forEachIndexed { index, item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
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
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults
                    .colors(
                        indicatorColor = Zomp.copy(alpha = 0.7f),
                        selectedTextColor = colors.text.primary,
                        selectedIconColor = colors.text.primary,
                        unselectedTextColor = colors.text.primary,
                        unselectedIconColor = colors.text.primary,
                    )
            )
        }
    }
}
