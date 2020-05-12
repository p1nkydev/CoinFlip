package com.pinkydev.coinflip.di

import com.pinkydev.coinflip.flow.auth.AuthViewModel
import com.pinkydev.coinflip.flow.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { AuthViewModel() }

}


val applicationModule = listOf(
    viewModelModule
)