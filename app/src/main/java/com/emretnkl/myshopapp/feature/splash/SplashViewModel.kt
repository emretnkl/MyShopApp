package com.emretnkl.myshopapp.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.local.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val firebaseAuth: FirebaseAuth
    ): ViewModel() {

    private val _uiEvent = MutableSharedFlow<SplashViewEvent>(replay = 0)
    val uiEvent: SharedFlow<SplashViewEvent> = _uiEvent

    init {
        checkOnboardingVisibleStatus()
    }


    private fun checkOnboardingVisibleStatus() {

        viewModelScope.launch {
            val isOnboardingVisible = dataStoreManager.getOnboardingVisible.first()
            if (checkCurrentUser()) {
                _uiEvent.emit(SplashViewEvent.NavigateToMain(true))
            }else{
                if (isOnboardingVisible) {
                    _uiEvent.emit(SplashViewEvent.NavigateToMain(false))
                } else {
                    _uiEvent.emit(SplashViewEvent.NavigateToOnboarding)
                }
            }
        }

    }


    private fun checkCurrentUser() : Boolean {
        firebaseAuth.currentUser?.let {
            return true
        } ?: run {
            return false
        }
    }
}

sealed class SplashViewEvent {

    object NavigateToLogin : SplashViewEvent()
    object NavigateToOnboarding : SplashViewEvent()
    class NavigateToMain(val isNavigateProducts : Boolean) : SplashViewEvent()

}
