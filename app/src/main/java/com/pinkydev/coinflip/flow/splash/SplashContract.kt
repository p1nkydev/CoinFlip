package com.pinkydev.coinflip.flow.splash

typealias SplashAction = SplashContract.() -> Unit

interface SplashContract {
    fun navigateAuth()
}