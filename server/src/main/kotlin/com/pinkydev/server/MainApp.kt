package com.pinkydev.server


import com.google.api.client.http.HttpStatusCodes
import com.google.appengine.repackaged.com.google.gson.Gson
import com.pinkydev.common.RoomCreation
import com.pinkydev.server.auth.login
import com.pinkydev.server.auth.signUp
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.basic
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.cio.websocket.Frame
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


private val userCache = UserCacheImpl()

private val roomService = RoomService(userCache)

private val gson = Gson()

fun Application.main() {
    install(CallLogging)
    install(ContentNegotiation) { gson() }
    install(WebSockets)

    authentication {
        basic(name = "core_auth") {
            validate { call ->
                userCache
                    .getUserByName(call.name)
                    ?.let { UserIdPrincipal(call.name) }
            }
        }
    }

    routing {
        login(userCache)
        signUp(userCache)
        webSocket("/pidor") {
            println("connected")
            launch {
                println("launched")
                roomService.observeRooms()
                    .map { gson.toJson(it) }
                    .onEach { outgoing.send(Frame.Text(it)) }
                    .collect()
                println("launch done")
            }
            println("web socket done")
        }
        authenticate("core_auth") {
            post("/room/create") {
                val room = call.receiveOrNull<RoomCreation>()
                if (room == null) {
                    call.respond(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                } else {
                    roomService.createRoom(room.creator, room.maxPlayersCount, room.moneyAmount)
                    call.respond(HttpStatusCodes.STATUS_CODE_OK)
                }
            }

            get("/room/join") {
                val roomId = call.request.queryParameters["roomId"]?.toLong()
                val playerId = call.request.queryParameters["playerId"]?.toInt()

                if (roomId != null && playerId != null) {
                    roomService.joinRoom(roomId, playerId)
                } else {
                    call.respond(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
                }
            }
        }

    }
}