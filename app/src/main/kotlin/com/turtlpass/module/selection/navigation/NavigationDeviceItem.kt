package com.turtlpass.module.selection.navigation

sealed class NavigationDeviceItem(var route: String) {
    object Selection : NavigationDeviceItem("Selection")
    object UserAccounts : NavigationDeviceItem("UserAccounts")
    object PIN : NavigationDeviceItem("PIN")
    object ConnectUsb : NavigationDeviceItem("ConnectUsb")
    object Loader : NavigationDeviceItem("Loader")
}
