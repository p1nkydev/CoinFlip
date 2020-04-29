package com.pinkydev.coinflip.coin.side

import android.graphics.Canvas
import android.graphics.Color
import com.pinkydev.coinflip.data.RoundRect

class EdgeSide(
    right: Float,
    bottom: Float
) : CoinSide {

    private val edgeRect = RoundRect(
        right = right,
        bottom = bottom,
        color = Color.BLUE
    )

    override fun draw(canvas: Canvas) {
        edgeRect.draw(canvas)
    }

    override fun rotate(degrees: Float) {
        edgeRect.rotate(degrees)
    }

}