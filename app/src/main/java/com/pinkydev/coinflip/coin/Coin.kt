package com.pinkydev.coinflip.coin

import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import com.pinkydev.coinflip.coin.side.BackSide
import com.pinkydev.coinflip.coin.side.CoinSide
import com.pinkydev.coinflip.coin.side.FrontSide

class Coin(
    left: Float,
    right: Float,
    top: Float,
    bottom: Float
) {

    private val camera = Camera()
    private val matrix = Matrix()

    private val width = right - left
    private val height = bottom - top

    private val centerX = width / 2
    private val centerY = height / 2

    private val frontSide = FrontSide(left, right, top, bottom)
    private val backSide = BackSide(left, right, top, bottom)

    private var actualFrontSide: CoinSide = frontSide

    fun swapSide() {
        actualFrontSide = if (actualFrontSide is FrontSide) backSide else frontSide
    }

    fun rotateCameraX(degreeX: Float, translateZ: Float) {
        camera.save()
        camera.translate(camera.locationX, camera.locationY, translateZ)
        camera.rotateX(degreeX)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }

    fun rotateCameraY(degreeY: Float, translateZ: Float) {
        camera.save()
        camera.translate(camera.locationX, camera.locationY, translateZ)
        camera.rotateY(degreeY)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }

    fun draw(canvas: Canvas) {
        canvas.setMatrix(matrix)
        actualFrontSide.draw(canvas)
        matrix.reset()
    }

}