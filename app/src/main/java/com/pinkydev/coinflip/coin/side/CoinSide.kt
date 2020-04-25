package com.pinkydev.coinflip.coin.side

import android.graphics.Canvas
import androidx.annotation.FloatRange

interface CoinSide {
    fun draw(canvas: Canvas)
    fun rotate(@FloatRange(from = 0.0, to = 180.0) degrees: Float)
}