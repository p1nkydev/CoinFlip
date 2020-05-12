package com.pinkydev.coinflip

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pinkydev.coinflip.coin.Coin
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flipView.setOnClickListener {
            flipView.flipInfinite()
        }
    }

}

class FlipView(context: Context, attrs: AttributeSet) : View(context, attrs), FrameRequester {

    private var coin: Coin? = null

    private val flipper by lazy {
        Flipper(
            Coin(
                left = 0f,
                right = width.toFloat(),
                top = 0f,
                bottom = height.toFloat()
            ),
            this
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coin?.draw(canvas)
    }

    override fun onFrameReady(coin: Coin) {
        this.coin = coin
        invalidate()
    }

    fun flipInfinite() {
        post { flipper.flipInfinite(300, 600_000) }
    }

}
