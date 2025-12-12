package com.turtlpass.useraccount.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun rememberPermissionRequester(
    permissions: List<String>,
    rationaleContent: (@Composable (onConfirm: () -> Unit, onDismiss: () -> Unit) -> Unit)? = null,
    onPermissionsGranted: () -> Unit
): () -> Unit {

    val permissionState = rememberMultiplePermissionsState(permissions)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var showRationale by remember { mutableStateOf(false) }
    var requestInProgress by remember { mutableStateOf(false) }
    var userRequested by remember { mutableStateOf(false) } // user explicitly triggered
    var dismissedDialog by remember { mutableStateOf(false) } // dismissed system dialog

    /**
     * User-triggered permission request
     */
    val triggerRequest: () -> Unit = {
        userRequested = true
        requestInProgress = true

        when {
            permissionState.allPermissionsGranted -> {
                onPermissionsGranted()
                requestInProgress = false
                dismissedDialog = false
            }

            rationaleContent != null && (permissionState.shouldShowRationale || dismissedDialog) -> {
                // Show rationale if allowed or if previously dismissed
                showRationale = true
            }

            else -> {
                // Launch system dialog
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }

    // Observe permission state changes
    LaunchedEffect(permissionState.allPermissionsGranted, permissionState.shouldShowRationale) {
        if (!userRequested) return@LaunchedEffect

        when {
            permissionState.allPermissionsGranted -> {
                onPermissionsGranted()
                requestInProgress = false
                showRationale = false
                dismissedDialog = false
                userRequested = false
            }

            !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                // Only consider it a dismiss if system dialog was just shown
                if (requestInProgress) {
                    dismissedDialog = true
                    requestInProgress = false
                }
            }
        }
    }

    // Observe permissions after returning from Settings
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            snapshotFlow { permissionState.allPermissionsGranted }
                .collect { granted ->
                    if (granted && userRequested) {
                        onPermissionsGranted()
                        requestInProgress = false
                        showRationale = false
                        dismissedDialog = false
                        userRequested = false
                    }
                }
        }
    }

    // Rationale dialog
    if (showRationale && rationaleContent != null) {
        rationaleContent(
             {
                showRationale = false
                permissionState.launchMultiplePermissionRequest()
            },
              {
                showRationale = false
                requestInProgress = false
                dismissedDialog = true
            }
        )
    }

    return triggerRequest
}
