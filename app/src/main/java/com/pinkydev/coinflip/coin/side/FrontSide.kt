package com.pinkydev.coinflip.coin.side

import android.graphics.Canvas
import android.graphics.Color
import com.pinkydev.coinflip.data.Oval

class FrontSide(
    left: Float,
    right: Float,
    top: Float,
    bottom: Float
) : CoinSide {

    private val outlineWidth = 30f

    private val biggerOval =
        Oval(left, right, top, bottom, Color.BLACK)

    private val smallerOval = Oval(
        left = left + outlineWidth,
        right = right - outlineWidth,
        top = top + outlineWidth,
        bottom = bottom - outlineWidth,
        color = Color.RED
    )

    override fun draw(canvas: Canvas) {
        biggerOval.draw(canvas)
        smallerOval.draw(canvas)
    }

    override fun rotate(degrees: Float) {
        biggerOval.rotate(degrees)
        smallerOval.rotate(degrees)
    }

}