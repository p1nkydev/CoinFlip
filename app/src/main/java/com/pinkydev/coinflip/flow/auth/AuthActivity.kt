package com.pinkydev.coinflip.flow.auth

import android.os.Bundle
import com.pinkydev.coinflip.MainActivity
import com.pinkydev.coinflip.R
import com.pinkydev.coinflip.ext.replaceActivity
import com.pinkydev.coinflip.flow.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity<AuthViewModel>(R.layout.activity_auth) {

    override val viewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fvAuth.flipInfinite()
        viewModel.liveData.observe {
            replaceActivity<MainActivity>()
        }
        bntLogin.setOnClickListener {
            viewModel.login(etUsername.text.toString(), etPassword.text.toString())
        }
    }

}