package com.pinkydev.coinflip.flow.splash

import androidx.lifecycle.MutableLiveData
import com.pinkydev.coinflip.ext.invoke
import com.pinkydev.coinflip.flow.base.BaseViewModel

class SplashViewModel : BaseViewModel() {
    val liveData = MutableLiveData<SplashAction>()

    init {
        liveData { navigateAuth() }
    }
}