package com.pinkydev.coinflip.data

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import kotlin.math.pow

class RoundRect(
    private val left: Float,
    private val right: Float,
    private val top: Float,
    private val bottom: Float,
    private val radius: Float,
    color: Int
) {

    private var radiusX = radius / 2
    private var radiusY = radius / 2

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        this.color = color
    }

    private val drawRect = RectF(left, top, right, bottom)

    fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
//        canvas.drawRoundRect(drawRect, radiusX, radiusY, paint)
    }

    private val path = Path()

    private val height = 40f

    private val corners = floatArrayOf(
        radiusX, radiusY,     // top left
        radiusX, radiusY,     // top right
        radiusX, radiusY,     // bottom right
        radiusX, radiusY      // bottom left
    )


    private val quadX = right / 2
    private val quadY = bottom * 2
    private val minQuadY = bottom / 6

    fun rotate(degree: Float) {

        val rotatePercent = degree / 90

        radiusX = (radius - (radius * rotatePercent))
        radiusY = (radius - (radius * rotatePercent)).pow(2)

        val newTop = top + (bottom * (rotatePercent)) / 2f
        val newBottom = bottom - (bottom * (rotatePercent)) / 2f

        drawRect.set(
            left,
            newTop + height * rotatePercent,
            right,
            newBottom + height * rotatePercent
        )

        val currentY = (quadY - minQuadY) * rotatePercent



        path.reset()
        path.moveTo(0f, bottom / 2)
        path.quadTo(quadX, currentY, right, bottom / 2)
//        path.addRoundRect(drawRect, corners, Path.Direction.CW)
    }
}