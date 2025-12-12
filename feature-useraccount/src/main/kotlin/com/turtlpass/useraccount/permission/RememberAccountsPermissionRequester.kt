package com.turtlpass.useraccount.permission

import android.Manifest.permission.GET_ACCOUNTS
import android.Manifest.permission.READ_CONTACTS
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun rememberAccountsPermissionRequester(
    onPermissionsGranted: () -> Unit
): () -> Unit = rememberPermissionRequester(
    permissions = listOf(GET_ACCOUNTS, READ_CONTACTS),
    rationaleContent = { onConfirm, onDismiss ->
        AccountsPermissionRationaleDialog(onConfirm, onDismiss)
    },
    onPermissionsGranted = onPermissionsGranted,
)
