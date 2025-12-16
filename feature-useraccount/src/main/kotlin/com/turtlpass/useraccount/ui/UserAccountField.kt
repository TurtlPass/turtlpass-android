package com.turtlpass.useraccount.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.domain.Result
import com.turtlpass.ui.input.DropdownTextField
import com.turtlpass.ui.input.IconDropdown
import com.turtlpass.ui.theme.AppTheme
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.appTextSelectionColors
import com.turtlpass.useraccount.R
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.viewmodel.UserAccountUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UserAccountField(
    modifier: Modifier = Modifier,
    userAccountUiState: State<UserAccountUiState>,
    isSheetOpen: Boolean,
    onAccountSelected: (accountSelected: UserAccount?) -> Unit,
    onTrailingIconClick: () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalTextSelectionColors provides appTextSelectionColors()
    ) {
        DropdownTextField(
            modifier = modifier
                .onFocusChanged { focusState ->
                    focused = focusState.isFocused
                },
            label = stringResource(R.string.feature_useraccount_input_label_account_id),
            value = userAccountUiState.value.selectedAccount?.accountId ?: "",
            focused = focused,
            onValueChange = {
                if (it.isNotEmpty()) {
                    onAccountSelected(UserAccount(accountId = it))
                } else onAccountSelected(null)
            },
            leadingIcon = {
                UserAccountImage(
                    modifier = Modifier
                        .requiredSize(dimensions.iconFieldSize),
                    userAccount = userAccountUiState.value.selectedAccount,
                    shimmerEnabled = false,
                )
            },
            trailingIcon = if (userAccountUiState.value.userAccountsResult.isError()) {
                {
                    IconButton(onClick = onTrailingIconClick) {
                        IconDropdown(expanded = isSheetOpen)
                    }
                }
            } else {
                {
                    if (userAccountUiState.value.userAccountsResult.isLoading()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensions.x16),
                            color = colors.default.icon,
                            strokeWidth = dimensions.x2
                        )
                    } else if (userAccountUiState.value.userAccountsResult.isSuccessful()) {
                        IconButton(onClick = onTrailingIconClick) {
                            IconDropdown(expanded = isSheetOpen)
                        }
                    }
                }
            }
        )
    }
}

private class AccountResultFielNewProvider : PreviewParameterProvider<Result<List<UserAccount>>> {
    override val values = sequenceOf(
        Result.Loading,
        Result.Success(listOf(UserAccount("android@turtlpass.com", null))),
        Result.Error()
    )
}

@OptIn(ExperimentalPermissionsApi::class)
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
private fun Preview(
    @PreviewParameter(AccountResultFielNewProvider::class) item: Result<List<UserAccount>>
) {
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            UserAccountField(
                modifier = Modifier.fillMaxWidth(),
                userAccountUiState = remember {
                    mutableStateOf(
                        UserAccountUiState(
                            selectedAccount = null,
                            userAccountsResult = item,
                        )
                    )
                },
                isSheetOpen = false,
                onAccountSelected = {},
                onTrailingIconClick = {},
            )
        }
    }
}
