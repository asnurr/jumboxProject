package com.aynlss.jumboxproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aynlss.jumboxproject.common.Resource
import com.aynlss.jumboxproject.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
): ViewModel() {

    private var _logInState = MutableLiveData<LogInState>()
    val logInState: LiveData<LogInState> get() = _logInState

    fun logIn(email: String, password: String) = viewModelScope.launch {
        if (checkFields(email, password)) {
            _logInState.value = LogInState.Loading

            _logInState.value = when (val result = firebaseRepository.signIn(email, password)) {
                is Resource.Success -> LogInState.GoToHome
                is Resource.Fail -> LogInState.ShowPopUp(result.failMessage)
                is Resource.Error -> LogInState.ShowPopUp(result.errorMessage)
            }
        }
    }

    private fun checkFields(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                _logInState.value = LogInState.ShowPopUp("Email cannot be left blank!")
                false
            }

            password.isEmpty() -> {
                _logInState.value = LogInState.ShowPopUp("Password cannot be left blank!")
                false
            }

            password.length < 6 -> {
                _logInState.value = LogInState.ShowPopUp("Password should be 6 characters at least!")
                false
            }

            else -> true
        }
    }
}

sealed interface LogInState {
    object Loading : LogInState
    object GoToHome: LogInState
    data class ShowPopUp(val errorMessage: String) : LogInState
}
