package com.pinkydev.coinflip.ext

import androidx.lifecycle.MutableLiveData

operator fun <T> MutableLiveData<T>.invoke(function: T) {
    value = function
}