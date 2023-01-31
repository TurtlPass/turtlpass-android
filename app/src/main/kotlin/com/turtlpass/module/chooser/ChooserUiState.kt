package com.turtlpass.module.chooser

import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.useraccount.model.UserAccount

data class ChooserUiState(
    val model: ChooserInputs,
    val installedAppsResult: Result<List<InstalledApp>> = Result.Loading,
    val userAccountsResult: Result<List<UserAccount>> = Result.Error(),
    val isAccessibilityServiceEnabled: Boolean = true,
    val isPassphraseEnabled: Boolean = false,
)
