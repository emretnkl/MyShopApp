package com.emretnkl.myshopapp.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Success())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        savedStateHandle.get<Boolean>(MainActivity.KEY_NAVIGATE_HOME)?.let {
            if (it) {
                _uiState.value = MainUiState.Success(true)
            } else {
                _uiState.value = MainUiState.Success(false)
            }
        }
    }
}

sealed class MainUiState {
    class Success(val isNavigateProducts: Boolean = false) : MainUiState()
}
