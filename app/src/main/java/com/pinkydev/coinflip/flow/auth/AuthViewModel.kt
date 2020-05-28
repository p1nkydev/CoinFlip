package com.pinkydev.coinflip.flow.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pinkydev.coinflip.flow.base.BaseViewModel
import com.pinkydev.coinflip.service
import com.pinkydev.common.model.User
import kotlinx.coroutines.launch

lateinit var user: User

lateinit var login: String
lateinit var pass: String

class AuthViewModel : BaseViewModel() {

    val liveData = MutableLiveData<Unit>()

    fun login(username: String, password: String) {
        if (username.isNotBlank() && password.isNotBlank()) {

            login = username
            pass = password

            viewModelScope.launch {
                user = service.login(username, password)
                liveData.value = Unit
            }
        }
    }

}