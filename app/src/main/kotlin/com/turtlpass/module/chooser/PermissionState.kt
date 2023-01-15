package com.turtlpass.module.chooser

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@ExperimentalPermissionsApi
data class PermissionState(
    val multiplePermissionsState: MultiplePermissionsState? = null,
    val accountsPermission: AccountsPermission = AccountsPermission.None,
)

enum class AccountsPermission {
    None, Rationale, Granted, NotGranted
}
