package com.pinkydev.coinflip

import android.graphics.Canvas

interface FrameRequester {
    fun onFrameReady()
}

class Flipper(
    private val width: Float,
    private val height: Float,
    private val frameRequester: FrameRequester
) {
//    private val coin = Coin(FrontSide(0f, width, 0f, height), BackSide(0f, width, 0f, height), SideSide())

    fun drawWith(canvas: Canvas) {
//        coin.draw(canvas)
    }
}