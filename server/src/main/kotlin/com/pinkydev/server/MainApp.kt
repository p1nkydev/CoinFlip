package com.pinkydev.server


import com.google.gson.Gson
import com.pinkydev.common.model.RoomCreation
import com.pinkydev.common.model.RoomJoin
import com.pinkydev.server.auth.login
import com.pinkydev.server.auth.signUp
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.Frame
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import org.slf4j.event.Level
import java.time.Duration


private val userCache = UserCacheImpl()

private val eventHandler = EventHandler()

private val roomService = RoomService(userCache, eventHandler)

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
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        login(userCache)
        signUp(userCache)
        webSocket("/pidor") {
            eventHandler.events
                .map { serializer.toJson(it) }
                .collect {
                    sendText(it)
                }
        }
        post("/room/create") {
            val room = call.receiveOrNull<RoomCreation>()
            if (room == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                roomService.createRoom(room.creator, room.maxPlayersCount, room.moneyAmount)
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/room/available") {
            call.respond(HttpStatusCode.OK, roomService.rooms)
        }

        post("/room/join") {
            val join = call.receiveOrNull<RoomJoin>()

            if (join == null) {
                call.respond(HttpStatusCode.BadRequest)
            } else {
                roomService.joinRoom(join.roomId, join.playerId)
                call.respond(HttpStatusCode.OK)
            }
        }

    }
}

suspend fun DefaultWebSocketServerSession.sendText(text: String) {
    outgoing.send(Frame.Text(text))
}