package com.emretnkl.myshopapp.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val dataStoreManager : DataStoreManager) : ViewModel() {

    fun setOnboardingStatus() {
        viewModelScope.launch {
            dataStoreManager.setOnboardingVisible(true)
        }
    }
}