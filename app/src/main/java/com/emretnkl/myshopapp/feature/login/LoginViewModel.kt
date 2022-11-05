package com.emretnkl.myshopapp.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.local.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataStoreManager: DataStoreManager , private val firebaseAuth: FirebaseAuth): ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<LoginViewEvent>(replay = 0)
    val uiEvent: SharedFlow<LoginViewEvent> = _uiEvent

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (isValidFields(email, password)) {
                firebaseAuth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        viewModelScope.launch {
                            _uiEvent.emit(LoginViewEvent.NavigateToMain)
                        }

                    } else {
                        viewModelScope.launch {
                            _uiEvent.emit(LoginViewEvent.ShowError(task.exception?.message.toString()))
                        }
                    }
                }
            } else {
                _uiEvent.emit(LoginViewEvent.ShowError("Please fill all fields"))
            }
        }
}

    private fun isValidFields(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

}

    sealed class LoginViewEvent {
    object NavigateToMain : LoginViewEvent()
    class ShowError(val error: String) : LoginViewEvent()
}

sealed class LoginUiState {
    object Empty : LoginUiState()
    object Loading : LoginUiState()
    object Success : LoginUiState()
    class Error(val error: String) : LoginUiState()
}