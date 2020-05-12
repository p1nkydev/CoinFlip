package com.pinkydev.server

import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import com.pinkydev.common.model.RoomCreation
import com.pinkydev.common.model.RoomJoin
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
            val json = defaultSerializer()

            println("signup")
            client.get<String>(port = 8080, path = "/signup?username=player1&password=123")

            println("login")
            client.get<String>(port = 8080, path = "/login?username=player1&password=123")

            println("create")
            client.post<String>(port = 8080, path = "/room/create") {
                val room = json.write(
                    RoomCreation(
                        Player(1, "Player1"),
                        2,
                        10f
                    )
                )
                println("room: $room")
                body = room
            }

            println("get rooms")

            val rooms = client.get<List<Room>>(port = 8080, path = "/room/available")

            println("rooms: $rooms")

            println("ws")
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/pidor") {
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

            println("signup")
            client.get<String>(
                host = "192.168.0.102",
                port = 8080,
                path = "/signup?username=123&password=123"
            )

            println("login")
            client.get<String>(
                host = "192.168.0.102",
                port = 8080,
                path = "/login?username=123&password=123"
            )

            println("get rooms")
            val rooms = client.get<List<Room>>(port = 8080, path = "/room/available")
            println("rooms: $rooms")

            println("room join")
            client.post<String>(port = 8080, path = "/room/join") {
                body = defaultSerializer().write(RoomJoin(2, rooms.first().id))
            }

            println("ws player2")
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/pidor") {
                for (message in incoming.map { it as? Frame.Text }.filterNotNull()) {
                    println("Player2 server message: " + message.readText())
                }
            }
        }
    }
}