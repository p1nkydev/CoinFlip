package com.pinkydev.server


import com.google.gson.Gson
import com.pinkydev.common.event.SearchGameEvent
import com.pinkydev.common.event.SocketEvent
import com.pinkydev.server.auth.login
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.*
import org.slf4j.event.Level
import java.time.Duration


private val userCache = UserCacheImpl()

private val roomService = RoomService(userCache)

private val serializer = Gson()

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.main() {
    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(1000)
        timeout = Duration.ofSeconds(10000)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        login(userCache)
        webSocket("/pidor") {

            for (each in incoming) {
                incoming



                flowOf(each)
                    .filterIsInstance<Frame.Text>()
                    .map { it.readText() }
                    .filter { it.contains(SocketEvent.TYPE_SEARCH_GAME) }
                    .map { serializer.fromJson(it, SearchGameEvent::class.java) }
                    .onEach { event ->
                        roomService.getRoomBy(event.searchRequest)?.let {
                            roomService.joinPlayerTo(event.searchRequest.player, it, this)
                        } ?: kotlin.run {
                            roomService.createRoom(event.searchRequest, this)
                        }
                    }
                    .collect()
            }
        }

        get("/room/cancel") {
            val playerId = call.request.queryParameters["playerId"]?.toInt() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            roomService.cancelGame(playerId)
            call.respond(HttpStatusCode.OK)
        }

        get("/room/available") {
            call.respond(HttpStatusCode.OK, roomService.roomSession.keys)
        }

    }
}

suspend fun <T> WebSocketSession.send(what: T) {
    send(serializer.toJson(what))
}