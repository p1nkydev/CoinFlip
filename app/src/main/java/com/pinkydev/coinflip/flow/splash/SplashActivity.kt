package com.pinkydev.coinflip.flow.splash

import android.os.Bundle
import com.pinkydev.coinflip.R
import com.pinkydev.coinflip.ext.replaceActivity
import com.pinkydev.coinflip.flow.auth.AuthActivity
import com.pinkydev.coinflip.flow.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<SplashViewModel>(R.layout.activity_splash), SplashContract {
    override val viewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.liveData.observe { it.invoke(this) }
    }

    override fun navigateAuth() {
        replaceActivity<AuthActivity>()
    }


}