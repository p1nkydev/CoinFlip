package com.pinkydev.server

import com.pinkydev.common.event.*
import com.pinkydev.common.model.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.client.request.*
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.channels.filterNotNull
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
            val token = client.get<UserToken>(
                host = "0.0.0.0",
                port = 8080,
                path = "/login?username=123&password=123"
            )

            val me = client.request<User> {
                header("Authorization", "Bearer ${token.token}")
                method = HttpMethod.Get
                url {
                    host = "0.0.0.0"
                    path("user")
                    port = 8080
                }
            }

            println("ws")

            val request: HttpRequestBuilder.() -> Unit = {
                header("Authorization", "Bearer ${token.token}")
                method = HttpMethod.Get
                host = "0.0.0.0"
                port = 8080
                url {
                    path("pidor")
                }
            }
            client.ws(request) {
                send(
                    CreateRoomEvent(
                        CreateRoomRequest(
                            player = Player(me.id, me.name, 0),
                            moneyAmount = 50
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
            val token = client.get<UserToken>(
                host = "0.0.0.0",
                port = 8080,
                path = "/login?username=1337&password=123"
            )

            val me = client.request<User> {
                header("Authorization", "Bearer ${token.token}")
                method = HttpMethod.Get
                url {
                    host = "0.0.0.0"
                    path("user")
                    port = 8080
                }
            }

            println("ws")

            val request: HttpRequestBuilder.() -> Unit = {
                header("Authorization", "Bearer ${token.token}")
                method = HttpMethod.Get
                host = "0.0.0.0"
                port = 8080
                url {
                    path("pidor")
                }
            }
            client.ws(request) {
                send(GetRoomsEvent())
                incoming.consumeAsFlow()
                    .map { it as? Frame.Text }
                    .filterNotNull()
                    .map { it.readText() }
                    .onEach { text ->
                        if (text.contains(SocketEvent.TYPE_AVAILABLE_ROOMS_RESPONSE)) {
                            val rooms = serializer.fromJson(text, AvailableRoomsEvent::class.java)
                            send(
                                JoinToRoomEvent(
                                    JoinToRoomRequest(
                                        player = Player(me.id, me.name, 1),
                                        rooms.rooms.first()
                                    )
                                )
                            )
                            println("Player2 received rooms: $text")
                        } else {
                            println("Player2 received event: $text")
                        }
                    }.collect()
            }
        }
    }
}