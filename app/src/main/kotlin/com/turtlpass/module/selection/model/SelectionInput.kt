package com.turtlpass.module.selection.model

import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.model.WebsiteUi

data class SelectionInput(
    var productType: ProductTypeUi = ProductTypeUi.Application,
    val selectedApp: InstalledAppUi? = null,
    var selectedUrl: WebsiteUi? = null,
    val selectedPin: String? = null,
)
