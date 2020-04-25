package com.pinkydev.coinflip.coin

import android.graphics.Canvas
import com.pinkydev.coinflip.coin.side.CoinSide

class Coin(
    private val frontSide: CoinSide,
    private val backSide: CoinSide,
    private val edgeSide: CoinSide
) {

    private var actualFrontSide:CoinSide? = frontSide


    fun rotate(degree: Float) {
        var actualDegree = degree
        when {
            degree in 0f..80f -> {
                actualFrontSide = frontSide
            }
            degree in 80f..90f -> {
                actualFrontSide = null
            }
            degree > 90f -> {
                actualDegree = 180 - degree
                actualFrontSide = backSide
            }
        }
        edgeSide.rotate(actualDegree)
        actualFrontSide?.rotate(actualDegree)
    }

    fun draw(canvas: Canvas) {
        edgeSide.draw(canvas)
        actualFrontSide?.draw(canvas)
    }

}