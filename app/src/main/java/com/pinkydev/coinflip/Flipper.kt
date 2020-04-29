package com.pinkydev.coinflip

import android.animation.ValueAnimator
import com.pinkydev.coinflip.coin.Coin
import kotlin.math.sin

interface FrameRequester {
    fun onFrameReady(coin: Coin)
}

class Flipper(
    private val coin: Coin,
    private val frameRequester: FrameRequester
) {

    // e.g 10 times for 10 sec
    fun flip(times: Int, time: Long) {

        val totalDegree = times * 180f + 90

        val fromZ = 1000f
        val toZ = 100

        var lastDegree = 0f

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = time
            addUpdateListener {
                val progress = it.animatedValue as Float
                val degreeX = totalDegree * progress
                val interpolatedValue = sin(Math.PI * progress).toFloat()
                val zProgress = (fromZ - (fromZ * interpolatedValue)) + toZ

                coin.rotateCamera(degreeX + 90, zProgress)

                val cattedDeg = degreeX % 180
                if (lastDegree > cattedDeg) {
                    coin.swapSide()
                }
                lastDegree = cattedDeg
                frameRequester.onFrameReady(coin)
            }
            start()
        }
    }
}