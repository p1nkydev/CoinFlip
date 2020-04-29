package com.pinkydev.coinflip

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation


class Flip3dAnimation(
    private val fromDegrees: Float,
    private val toDegrees: Float,
    private val centerX: Float,
    private val centerY: Float
) : Animation() {

    private val camera = Camera()

    override fun applyTransformation(
        interpolatedTime: Float,
        t: Transformation
    ) {
        val degrees = fromDegrees + (toDegrees - fromDegrees) * interpolatedTime
        val matrix = t.matrix
        camera.save()
        camera.rotateX(degrees)
        camera.getMatrix(matrix)
        camera.restore()
        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)
    }

}