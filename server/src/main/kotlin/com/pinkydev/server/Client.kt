package com.pinkydev.server

import com.pinkydev.common.event.SearchGameEvent
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.SearchRequest
import com.pinkydev.common.model.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.channels.filterNotNull
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.runBlocking

object Player1 {
    @KtorExperimentalAPI
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config {
                install(WebSockets)
                Json {
                    serializer = defaultSerializer()
                }
            }

            println("login")
            val me = client.get<User>(
                host = "192.168.0.102",
                port = 8080,
                path = "/login?username=123&password=123"
            )

            println("ws")
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/pidor") {
                send(
                    SearchGameEvent(
                        SearchRequest(
                            player = Player(me.id, me.name, 0),
                            maxPlayersCount = 2,
                            moneyAmount = 30
                        )
                    )
                )
                for (message in incoming.map { it as? Frame.Text }.filterNotNull()) {
                    println("Player1 server message: " + message.readText())
                }
            }
        }
    }
}

object Player2 {

    @KtorExperimentalAPI
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config {
                install(WebSockets)
                Json {
                    serializer = defaultSerializer()
                }
            }

            println("login")
            val me = client.get<User>(
                host = "192.168.0.102",
                port = 8080,
                path = "/login?username=123&password=123"
            )

            println("ws player2")
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/pidor") {
                send(
                    SearchGameEvent(
                        SearchRequest(
                            player = Player(me.id, me.name, 1),
                            maxPlayersCount = 2,
                            moneyAmount = 40
                        )
                    )
                )
                for (message in incoming.map { it as? Frame.Text }.filterNotNull()) {
                    println("Player2 server message: " + message.readText())
                }
            }
        }
    }
}