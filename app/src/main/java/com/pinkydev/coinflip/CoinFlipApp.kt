package com.pinkydev.coinflip

import android.app.Application
import com.pinkydev.coinflip.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoinFlipApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@CoinFlipApp)
            // modules
            modules(applicationModule)
        }
    }
}