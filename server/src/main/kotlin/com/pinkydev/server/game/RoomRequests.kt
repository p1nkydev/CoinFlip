package com.pinkydev.server.game

import com.pinkydev.server.roomService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.rooms() {
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