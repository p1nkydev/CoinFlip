package com.pinkydev.coinflip.data

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Oval(
    private val left: Float,
    private val right: Float,
    private val top: Float,
    private val bottom: Float,
    color: Int
) {

    private val paint = Paint().apply {
        isAntiAlias = true
        this.color = color
    }

    private val drawRect = RectF(left, top, right, bottom)

    fun set(left: Float, top: Float, right: Float, bottom: Float) {
        drawRect.set(left, top, right, bottom)
    }

    fun rotate(degree: Float) {
        val rotatePercent = degree / 90
        val newTop = top + (bottom * rotatePercent) / 2f
        val newBottom = bottom - (bottom * rotatePercent) / 2f
        drawRect.set(left, newTop, right, newBottom)
    }

    fun draw(canvas: Canvas) {
        canvas.drawOval(drawRect, paint)
    }

}