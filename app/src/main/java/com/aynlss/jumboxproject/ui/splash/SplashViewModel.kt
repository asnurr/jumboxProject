package com.aynlss.jumboxproject.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aynlss.jumboxproject.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private var _splashState = MutableLiveData<SplashState>()
    val splashState: LiveData<SplashState> get() = _splashState

    init {
        viewModelScope.launch {
            delay(3000)
            checkUser()
        }
    }

    private fun checkUser() = viewModelScope.launch {
        _splashState.value = if (firebaseRepository.isUserLoggedIn()) {
            SplashState.GoToHome
        } else {
            SplashState.GoToSignIn
        }
    }
}

sealed interface SplashState {
    object GoToSignIn : SplashState
    object GoToHome : SplashState
}