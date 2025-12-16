package com.turtlpass.useraccount.viewmodel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.domain.Result
import com.turtlpass.useraccount.model.UserAccount
import com.turtlpass.useraccount.usecase.PersistAccountUseCase
import com.turtlpass.useraccount.usecase.RetrieveAccountUseCase
import com.turtlpass.useraccount.usecase.UserAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(
    ExperimentalCoroutinesApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalTextApi::class,
    FlowPreview::class,
)
@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val userAccountsUseCase: UserAccountsUseCase,
    private val retrieveAccountUseCase: RetrieveAccountUseCase,
    private val persistAccountUseCase: PersistAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAccountUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeUserAccountsAndResolveInitial()

        // React to first-time permission grant
        viewModelScope.launch {
            uiState
                .map { it.accountsPermission }
                .distinctUntilChanged()
                .filter { it == AccountsPermission.Granted }
                .collect {
                    observeUserAccountsAndResolveInitial()
                }
        }
    }

    private fun observeUserAccountsAndResolveInitial() {
        viewModelScope.launch {
            val persistedAccount = retrieveAccountUseCase().first()

            userAccountsUseCase().collect { result ->
                _uiState.update { it.copy(userAccountsResult = result) }

                if (result is Result.Success) {
                    val initialAccount = persistedAccount ?: result.data.firstOrNull()

                    initialAccount?.let {
                        _uiState.update { state ->
                            state.copy(selectedAccount = it)
                        }
                    }
                }
            }
        }
    }

    fun selectUserAccount(account: UserAccount?, persist: Boolean = false) {
        _uiState.update { it.copy(selectedAccount = account) }

        if (persist && account != null) {
            viewModelScope.launch { persistAccountUseCase(account) }
        }
    }

    fun updatePermissions(allGranted: Boolean) {
        _uiState.update {
            it.copy(accountsPermission = if (allGranted) AccountsPermission.Granted else AccountsPermission.NotGranted)
        }
    }
}
