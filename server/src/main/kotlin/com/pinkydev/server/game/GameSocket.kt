package com.pinkydev.server.game

import com.pinkydev.common.event.*
import com.pinkydev.server.roomService
import com.pinkydev.server.send
import com.pinkydev.server.serializer
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Route
import io.ktor.websocket.webSocket
import kotlinx.coroutines.flow.*

fun Route.gameSocket() {
    webSocket("/pidor") {
        incoming.consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .map { it.readText() }
            .map(::mapSocketEvent)
            .onEach {
                when (it) {
                    is CreateRoomEvent -> {
                        roomService.createRoom(it.createRoomRequest, this)
                    }
                    is GetRoomsEvent -> {
                        send(AvailableRoomsEvent(roomService.getAllRooms()))
                    }
                    is JoinToRoomEvent -> {
                        roomService.joinPlayerTo(it.joinToRoomRequest.player, it.joinToRoomRequest.room, this)
                    }
                }
            }
            .collect()
    }
}

private fun mapSocketEvent(event: String): SocketEvent {
    return when {
        event.contains(SocketEvent.TYPE_CREATE_ROOM) -> serializer.fromJson(
            event,
            CreateRoomEvent::class.java
        )
        event.contains(SocketEvent.TYPE_GET_ROOMS) -> serializer.fromJson(
            event,
            GetRoomsEvent::class.java
        )
        event.contains(SocketEvent.TYPE_JOIN_ROOM) -> serializer.fromJson(
            event,
            JoinToRoomEvent::class.java
        )
        else -> error("SOSI HUI PADLA")
    }
}