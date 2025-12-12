package com.turtlpass.module.selection.model

import com.turtlpass.module.selection.deviceFlow.DeviceFlowMode
import com.turtlpass.usb.ui.LoaderType

data class SelectionUiState(
    val model: SelectionInput,
    val flowMode: DeviceFlowMode = DeviceFlowMode.FullScreen,
    var loaderType: LoaderType = LoaderType.Loading,
)
