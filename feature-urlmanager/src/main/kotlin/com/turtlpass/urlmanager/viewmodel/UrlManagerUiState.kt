package com.turtlpass.urlmanager.viewmodel

import com.turtlpass.domain.Result
import com.turtlpass.model.WebsiteUi

data class UrlManagerUiState(
    val websiteListResult: Result<List<WebsiteUi>> = Result.Loading,
    val isAccessibilityEnabled: Boolean = false,
)
