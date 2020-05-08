package com.pinkydev.coinflip.flow.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseActivity<T : BaseViewModel>(@LayoutRes layout: Int) : AppCompatActivity(layout) {

    abstract val viewModel: T

    protected fun <Y> LiveData<Y>.observe(observer: (Y) -> Unit) {
        observe(this@BaseActivity, Observer { y ->
            y?.let { observer(it) }
        })
    }
}