package com.pinkydev.coinflip

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pinkydev.coinflip.coin.Coin
import com.pinkydev.coinflip.coin.side.BackSide
import com.pinkydev.coinflip.coin.side.EdgeSide
import com.pinkydev.coinflip.coin.side.FrontSide
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flipView.setOnClickListener { flipView.startFlip() }
    }

}

class FlipView(context: Context, attrs: AttributeSet) : View(context, attrs), FrameRequester {

    private val coin by lazy {
        Coin(
            FrontSide(
                0f,
                width.toFloat(),
                0f,
                height.toFloat()
            ),
            BackSide(
                0f,
                width.toFloat(),
                0f,
                height.toFloat()
            ),
            EdgeSide(
                0f,
                width.toFloat(),
                0f,
                height.toFloat()
            )
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coin.draw(canvas)
    }

    fun startFlip() {
        ValueAnimator
            .ofFloat(0f, 180f).apply {
                duration = 20000

                addUpdateListener {
                    val value = it.animatedValue as Float
                    coin.rotate(value)
                    invalidate()
                }

                start()
            }
    }

    override fun onFrameReady() {
        invalidate()
    }

}
