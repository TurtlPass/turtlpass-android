package com.turtlpass.urlmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turtlpass.accessibility.usecase.AccessibilityEnabledUpdatesUseCase
import com.turtlpass.db.repository.WebsiteRepository
import com.turtlpass.model.WebsiteUi
import com.turtlpass.urlmanager.usecase.WebsiteUpdatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UrlManagerViewModel @Inject constructor(
    private val accessibilityEnabledUpdatesUseCase: AccessibilityEnabledUpdatesUseCase,
    private val websiteUpdatesUseCase: WebsiteUpdatesUseCase,
    private val websiteRepository: WebsiteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UrlManagerUiState())
    val uiState: StateFlow<UrlManagerUiState> = _uiState.asStateFlow()

    init {
        observeAccessibilityEnabled()
        observeWebsites()
    }

    private fun observeAccessibilityEnabled() {
        viewModelScope.launch {
            accessibilityEnabledUpdatesUseCase()
                .onEach { result ->
                    _uiState.update { state ->
                        state.copy(isAccessibilityEnabled = result)
                    }
                }
                .launchIn(this)
        }
    }

    private fun observeWebsites() {
        viewModelScope.launch {
            websiteUpdatesUseCase()
                .onEach { result ->
                    _uiState.update { state -> state.copy(websiteListResult = result) }
                }
                .launchIn(this)
        }
    }

    fun deleteWebsite(website: WebsiteUi) {
        viewModelScope.launch {
            websiteRepository.deleteByUrl(website.url)
        }
    }

    fun clearAllWebsites() {
        viewModelScope.launch {
            websiteRepository.clearAll()
        }
    }
}
