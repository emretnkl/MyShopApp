package com.emretnkl.myshopapp.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val dataStoreManager: DataStoreManager): ViewModel() {

    private val _uiEvent = MutableSharedFlow<SplashViewEvent>(replay = 0)
    val uiEvent: SharedFlow<SplashViewEvent> = _uiEvent

    init {
        checkOnboardingVisibleStatus()
    }

    private fun checkOnboardingVisibleStatus() {
        viewModelScope.launch {
            dataStoreManager.getOnboardingVisible.collect{
                if (it) {
                    _uiEvent.emit(SplashViewEvent.NavigateToMain)
                } else {
                    _uiEvent.emit(SplashViewEvent.NavigateToOnboarding)
                }
            }
        }
    }
}

sealed class SplashViewEvent {

    object NavigateToLogin : SplashViewEvent()
    object NavigateToOnboarding : SplashViewEvent()
    object NavigateToMain : SplashViewEvent()

}
