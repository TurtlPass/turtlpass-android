package com.turtlpass.appmanager.usecase

interface PermissionProvider {
    fun checkDrawOverlay(): Boolean
    fun checkUsageStats(): Boolean
}
