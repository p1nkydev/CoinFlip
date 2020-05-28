package com.pinkydev.coinflip

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.pinkydev.coinflip.coin.Coin
import com.pinkydev.coinflip.flow.create.SetupGameFragment
import com.pinkydev.common.event.PlayerJoinedEvent
import com.pinkydev.common.event.RoomWinnerEvent
import com.pinkydev.common.event.SocketEvent
import com.pinkydev.common.event.SocketEvent.Companion.TYPE_PLAYER_JOINED
import com.pinkydev.common.event.SocketEvent.Companion.TYPE_PLAYER_WON
import com.pinkydev.common.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Basic {

    @GET("login")
    suspend fun login(
        @Query("username")
        username: String,
        @Query("password")
        password: String
    ): User

    @GET("room/cancel")
    suspend fun cancel(
        @Query("playerId")
        playerId: Int
    )
}

val gson = Gson()

val service: Basic = Retrofit.Builder()
    .client(OkHttpClient.Builder().build())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("http://192.168.0.102:8080/")
    .build()
    .create(Basic::class.java)

val socketFlow = MutableStateFlow<SocketEvent?>(null)

val socket = OkHttpClient().newWebSocket(
    Request.Builder()
        .url("ws://192.168.0.102:8080/pidor").build(),
    object : WebSocketListener() {

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.e("TAGGIR", "web socket: $text")
            val event = when {
                text.contains(TYPE_PLAYER_JOINED) -> gson.fromJson(
                    text,
                    PlayerJoinedEvent::class.java
                )
                text.contains(TYPE_PLAYER_WON) -> gson.fromJson(text, RoomWinnerEvent::class.java)
                else -> null
            }

            socketFlow.value = event
        }
    }
)


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SetupGameFragment())
            .commit()
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

    fun flip(times: Int, time: Long) {
        post { flipper.flip(times, time) }
    }

    fun zoomIn() {
        post { flipper.zoomIn() }
    }

}
