package com.pinkydev.coinflip.flow.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pinkydev.coinflip.authService
import com.pinkydev.coinflip.flow.base.BaseViewModel
import com.pinkydev.coinflip.token
import com.pinkydev.common.model.User
import kotlinx.coroutines.launch

lateinit var user: User

class AuthViewModel : BaseViewModel() {

    val liveData = MutableLiveData<Unit>()

    fun login(username: String, password: String) {
        if (username.isNotBlank() && password.isNotBlank()) {
            viewModelScope.launch {
                token = authService.login(username, password)
                liveData.value = Unit
            }
        }
    }

}