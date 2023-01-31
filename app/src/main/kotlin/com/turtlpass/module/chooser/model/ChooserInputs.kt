package com.turtlpass.module.chooser.model

import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.module.useraccount.model.UserAccount

data class ChooserInputs(
    var installedApp: InstalledApp? = null,
    var recentApp: InstalledApp? = null,
    var userAccount: UserAccount? = null,
    var storedAccount: UserAccount? = null,
    val pin: String? = null,
    val passphrase: String? = null,
)
