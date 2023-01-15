package com.turtlpass.module.useraccount

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.turtlpass.common.compose.input.DragHandle
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.ui.UserAccountRow
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.dimensions

@Composable
fun UserAccountsScreen(
    accountList: List<UserAccount>,
    onAccountSelected: (accountItem: UserAccount) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensions.x16)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DragHandle()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.x16)
        ) {
            itemsIndexed(
                items = accountList,
                key = { index, _ ->
                    accountList[index].accountId
                }
            ) { index, account ->
                val verticalModifier = when (index) {
                    (accountList.size - 1) -> { // last item
                        Modifier.padding(
                            top = dimensions.x4,
                            bottom = dimensions.x16
                        )
                    }
                    else -> {
                        Modifier.padding(vertical = dimensions.x4)
                    }
                }
                UserAccountRow(
                    modifier = verticalModifier,
                    accountItem = account
                ) {
                    onAccountSelected(account)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
@Composable
private fun Preview() {
    AppTheme {
        val mockAccounts = listOf(
            UserAccount("android@turtlpass.com", null),
            UserAccount("chrome.extension@turtlpass.com", null),
            UserAccount("firmware@turtlpass.com", null),
        )
        UserAccountsScreen(
            accountList = mockAccounts,
            onAccountSelected = {}
        )
    }
}
