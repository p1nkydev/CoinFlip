package com.pinkydev.coinflip.coin.side

import android.graphics.Canvas
import android.graphics.Color
import com.pinkydev.coinflip.data.RoundRect

class EdgeSide(
    private val left: Float,
    private val right: Float,
    private val top: Float,
    private val bottom: Float
) : CoinSide {

    private val edgeRect = RoundRect(
        left = left,
        right = right,
        top = top,
        bottom = bottom,
        radius = bottom,
        color = Color.BLUE
    )

    override fun draw(canvas: Canvas) {
        edgeRect.draw(canvas)
    }

    override fun rotate(degrees: Float) {
        edgeRect.rotate(degrees)
    }

}