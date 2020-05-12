package com.pinkydev.coinflip.ext

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.inflate(layoutRes: Int): View =
    LayoutInflater.from(this).inflate(layoutRes, null, false)

inline fun <reified T : AppCompatActivity> AppCompatActivity.replaceActivity(intent: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(intent))
    finish()
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivity() {
    startActivity(Intent(this, T::class.java))
}
