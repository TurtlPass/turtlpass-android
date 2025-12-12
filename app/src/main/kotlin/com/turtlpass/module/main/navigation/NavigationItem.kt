package com.turtlpass.module.main.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.turtlpass.ui.icons.Apps24Px
import com.turtlpass.ui.icons.Globe24Px

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    object App : NavigationItem("Apps", Apps24Px, "Apps")
    object Domain : NavigationItem("Domains", Globe24Px, "Websites")
    object UserAccounts : NavigationItem("UserAccounts", null, "")
}
