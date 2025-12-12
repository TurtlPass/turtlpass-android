package com.turtlpass.module.selection.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.R
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.module.selection.model.ProductTypeUi
import com.turtlpass.module.selection.model.SelectionInput
import com.turtlpass.module.selection.model.SelectionUiState
import com.turtlpass.ui.button.PrimaryButtonSmall
import com.turtlpass.ui.button.SecondaryButtonSmall
import com.turtlpass.ui.icons.Key24Px
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.ui.UserAccountField
import com.turtlpass.useraccount.viewmodel.UserAccountUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectedInputsScreen(
    selectionUiState: State<SelectionUiState>,
    userAccountUiState: State<UserAccountUiState>,
    onUserAccount: (account: UserAccount?) -> Unit,
    onNavigateUserAccounts: () -> Unit = {},
    onCancel: () -> Unit = {},
    onGetPassword: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = dimensions.x8)
            .padding(bottom = dimensions.x16)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            when (selectionUiState.value.model.productType) {
                ProductTypeUi.Application -> {
                    SelectedAppField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        selectionUiState = selectionUiState,
                    )
                }

                ProductTypeUi.Website -> {
                    SelectedWebsiteField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        uiState = selectionUiState,
                    )
                }
            }
            UserAccountField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.x8),
                userAccountUiState = userAccountUiState,
                isSheetOpen = false,
                onAccountSelected = { account ->
                    onUserAccount(account)
                },
                onTrailingIconClick = onNavigateUserAccounts,
            )

            Spacer(Modifier.height(dimensions.x16 + dimensions.x8))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButtonSmall(
                    text = "Cancel",
                    onClick = onCancel,
                )
                PrimaryButtonSmall(
                    text = stringResource(R.string.screen_selection_button_unlock),
                    imageVector = Key24Px,
                    enabled = true,
                    onClick = onGetPassword,
                    backgroundColor = colors.default.button
                )
            }
            //todo: ("<Advanced Options> expandable") // pass size(slider 1-128) / type [0-9][a-Z][±!@£] checkboxes
        }
    }
}


private class ProductTypeUiProvider : PreviewParameterProvider<ProductTypeUi> {
    override val values = sequenceOf(
        ProductTypeUi.Application,
        ProductTypeUi.Website
    )
}
@Preview(
    name = "Light theme",
    showBackground = true,
    backgroundColor = 0xffffffff,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)
/*@Preview(
    name = "Dark theme",
    showBackground = true,
    backgroundColor = 0xff424242,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    device = Devices.PIXEL_XL,
)*/
@Composable
private fun Preview(
    @PreviewParameter(ProductTypeUiProvider::class) item: ProductTypeUi
) {
    AppTheme {
        SelectedInputsScreen(
            selectionUiState = remember {
                mutableStateOf(
                    SelectionUiState(
                        model = SelectionInput(
                            productType = item,
                            selectedApp = InstalledAppUi(
                                appName = "TurtlPass",
                                packageName = "com.turtlpass",
                            )
                        )
                    )
                )
            },
            userAccountUiState = remember { mutableStateOf(UserAccountUiState()) },
//            permissionState = remember {
//                mutableStateOf(PermissionState(accountsPermission = AccountsPermission.Rationale))
//            },
            onUserAccount = {},
            onCancel = {},
            onGetPassword = {},
        )
    }
}
