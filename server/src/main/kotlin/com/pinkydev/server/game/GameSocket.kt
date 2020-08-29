package com.pinkydev.server.game

import com.pinkydev.common.event.SearchGameEvent
import com.pinkydev.common.event.SocketEvent
import com.pinkydev.server.roomService
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