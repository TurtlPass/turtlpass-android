package com.turtlpass.module.chooser.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.turtlpass.R
import com.turtlpass.common.compose.input.DropdownTextField
import com.turtlpass.common.compose.input.IconDropdown
import com.turtlpass.common.domain.Result
import com.turtlpass.module.chooser.ChooserUiState
import com.turtlpass.module.chooser.model.ChooserInputs
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.ui.UserAccountImage
import com.turtlpass.theme.AppTheme
import com.turtlpass.theme.AppTheme.colors
import com.turtlpass.theme.AppTheme.dimensions
import com.turtlpass.theme.appTextSelectionColors

@Composable
fun UserAccountField(
    modifier: Modifier = Modifier,
    uiState: State<ChooserUiState>,
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
            label = stringResource(R.string.input_label_account_id),
            value = uiState.value.model.userAccount?.accountId ?: "",
            focused = focused,
            onValueChange = {
                if (it.isNotEmpty()) {
                    onAccountSelected(UserAccount(accountId = it))
                } else onAccountSelected(null)
            },
            leadingIcon = {
                UserAccountImage(
                    modifier = Modifier
                        .requiredSize(28.dp),
                    userAccount = uiState.value.model.userAccount,
                    showPlaceholder = false
                )
            },
            trailingIcon = if (uiState.value.userAccountsResult.isError()) null else {
                {
                    if (uiState.value.userAccountsResult.isLoading()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensions.x16),
                            color = colors.default.icon,
                            strokeWidth = dimensions.x2
                        )
                    } else if (uiState.value.userAccountsResult.isSuccessful()) {
                        IconButton(onClick = onTrailingIconClick) {
                            IconDropdown(expanded = isSheetOpen)
                        }
                    }
                }
            }
        )
    }
}

private class AccountResultFieldProvider : PreviewParameterProvider<Result<List<UserAccount>>> {
    override val values = sequenceOf(
        Result.Loading,
        Result.Success(listOf(UserAccount("android@turtlpass.com", null))),
        Result.Error()
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
    @PreviewParameter(AccountResultFieldProvider::class) item: Result<List<UserAccount>>
) {
    val uiState = remember {
        mutableStateOf(
            ChooserUiState(
                model = ChooserInputs(),
                userAccountsResult = item,
            )
        )
    }
    AppTheme {
        Row(modifier = Modifier.padding(dimensions.x16)) {
            UserAccountField(
                modifier = Modifier.fillMaxWidth(),
                uiState = uiState,
                isSheetOpen = false,
                onAccountSelected = {},
                onTrailingIconClick = {},
            )
        }
    }
}
