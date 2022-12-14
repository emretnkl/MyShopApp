package com.emretnkl.myshopapp.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emretnkl.myshopapp.data.local.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore) : ViewModel() {

private val _uiEvent = MutableSharedFlow<RegisterViewEvent>(replay = 0)
    val uiEvent: SharedFlow<RegisterViewEvent> = _uiEvent

    fun register(email: String, password: String, confirmPassword: String, username: String) {
        viewModelScope.launch {
            isValidFields(email, password, confirmPassword, username)?.let {
                _uiEvent.emit(RegisterViewEvent.ShowError(it))
            } ?: kotlin.run{
                withContext(Dispatchers.IO){
                    firebaseAuth.createUserWithEmailAndPassword(
                        email,
                        password
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            setUsername(username, task.result?.user!!.uid)

                        } else {
                            viewModelScope.launch {
                                _uiEvent.emit(RegisterViewEvent.ShowError(task.exception?.message.toString()))
                            }
                        }
                    }
                }

            }
        }
    }


    private fun setUsername(username: String, uuid: String?) {
        viewModelScope.launch {
            dataStoreManager.setUsername(username)
            val userId = uuid

            fireStore.collection("/users").document(userId.toString()).set("username" to username)
                .addOnSuccessListener { documentReference ->
                    viewModelScope.launch { _uiEvent.emit(RegisterViewEvent.NavigateToMain) }
                }.addOnFailureListener { error ->
                    viewModelScope.launch {
                        _uiEvent.emit(RegisterViewEvent.ShowError(error.message.toString()))
                    }
                }
        }
    }



    private fun showError(error: java.lang.Exception) {
        viewModelScope.launch {
            _uiEvent.emit(RegisterViewEvent.ShowError(error.message.toString()))
        }

    }

    private fun navigateToMainPage() {
        viewModelScope.launch {
            _uiEvent.emit(RegisterViewEvent.NavigateToMain)

        }

    }

    private fun isValidFields(
        email: String,
        password: String,
        confirmPassword: String,
        username: String
    ): String? {

        return when {
            isFieldsEmpty(email, password, confirmPassword, username) -> {
                "Please fill all fields"
            }
            isEmailNotValid(email) -> {
                "Please enter a valid email address"
            }
            isPasswordsNotValid(password, confirmPassword) -> {
                "Passwords do not match"
            }
            isPasswordLengthNotValid(password) -> {
                "Password must be at least 6 characters"
            }

            else -> null
        }
    }

    private fun isFieldsEmpty(
        email: String,
        password: String,
        confirmPassword: String,
        username: String
    ) = email.isBlank() && password.isBlank() && confirmPassword.isBlank() && username.isBlank()

    private fun isEmailNotValid(email: String) = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordsNotValid(password: String, confirmPassword: String) = password != confirmPassword

    private fun isPasswordLengthNotValid(password: String) = password.length < 6
}

sealed class RegisterViewEvent {
    object NavigateToMain : RegisterViewEvent()
    class ShowError(val error: String) : RegisterViewEvent()
}