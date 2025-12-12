package com.turtlpass.appmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turtlpass.appmanager.model.InstalledAppUi
import com.turtlpass.appmanager.usecase.BuildInstalledAppUseCase
import com.turtlpass.appmanager.usecase.InstalledAppsUseCase
import com.turtlpass.appmanager.usecase.RecentAppsUsageUseCase
import com.turtlpass.appmanager.usecase.UsageAccessUseCase
import com.turtlpass.domain.Result
import com.turtlpass.domain.data
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AppManagerViewModel @Inject constructor(
    private val installedAppsUseCase: InstalledAppsUseCase,
    private val recentAppsUseCase: RecentAppsUsageUseCase,
    private val buildInstalledAppUseCase: BuildInstalledAppUseCase,
    usageAccessUseCase: UsageAccessUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppManagerUiState())
    val uiState: StateFlow<AppManagerUiState> = _uiState.asStateFlow()

    // HOT FLOW: Usage Access Permission Stream
    private val usageAccessFlow: SharedFlow<Boolean> =
        usageAccessUseCase()
            .map { it.data ?: false }
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 0),
                replay = 1
            )

    // HOT FLOW: Recent Apps Stream (Starts only when permission granted)
    private val recentAppsFlow: SharedFlow<Result<List<InstalledAppUi>>> =
        usageAccessFlow
            .flatMapLatest { granted ->
                if (granted) {
                    recentAppsUseCase()  // continuous polling flow
                } else {
                    emptyFlow()
                }
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 0),
                replay = 1
            )


    init {
        observeInstalledApps()
        collectUsageAccess()
        collectRecentApps()
    }

    // Installed Apps Observer (Cold > Hot via shareIn not required)
    private fun observeInstalledApps() {
        viewModelScope.launch {
            installedAppsUseCase()
                .onEach { result ->
                    _uiState.update { it.copy(installedAppsResult = result) }
                }
                .launchIn(this)
        }
    }

    // Apply Usage Access flow updates to UI state
    private fun collectUsageAccess() {
        viewModelScope.launch {
            usageAccessFlow.collect { granted ->
                _uiState.update { it.copy(isUsageAccessGranted = granted) }
            }
        }
    }

    // Apply Recent Apps flow updates to UI state
    private fun collectRecentApps() {
        viewModelScope.launch {
            recentAppsFlow.collect { result ->
                _uiState.update { it.copy(recentAppsResult = result) }
            }
        }
    }
}
