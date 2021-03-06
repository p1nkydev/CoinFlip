package com.pinkydev.coinflip.coin.side

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import com.pinkydev.coinflip.R
import com.pinkydev.coinflip.data.Oval
import org.koin.core.KoinComponent
import org.koin.core.get

class FrontSide(
    left: Float,
    right: Float,
    top: Float,
    bottom: Float
) : CoinSide, KoinComponent {

    private val outlineWidth = right * 0.05f

    private val biggerOval =
        Oval(left, right, top, bottom, Color.BLACK)

    private val smallerOval = Oval(
        left = left + outlineWidth,
        right = right - outlineWidth,
        top = top + outlineWidth,
        bottom = bottom - outlineWidth,
        color = get<Context>().getColor(R.color.colorAccent)
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