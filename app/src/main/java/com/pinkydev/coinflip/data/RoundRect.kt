package com.pinkydev.coinflip.data

import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class RoundRect(
    private val right: Float,
    private val bottom: Float,
    color: Int
) {
    companion object {
        private const val CUBIC_X_MULTIPLIER = 0.957f
        private const val CUBIC_Y_MULTIPLIER = 1.17f
    }


    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        this.color = color
    }

    private val path = Path()

    private val startY = bottom * CUBIC_Y_MULTIPLIER
    private val minQuadY = bottom * 0.6f

    fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }

    fun rotate(degree: Float) {
//        val rotatePercent = degree / 90
//        val currentY = startY - ((startY - minQuadY) * rotatePercent)
//        path.reset()
//        path.moveTo(0f, bottom / 2)
//        path.cubicTo(
//            right - right * CUBIC_X_MULTIPLIER,
//            currentY,
//            right * CUBIC_X_MULTIPLIER,
//            currentY,
//            right,
//            bottom / 2
//        )
    }
}