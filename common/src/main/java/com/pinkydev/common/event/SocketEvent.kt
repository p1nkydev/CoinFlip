package com.pinkydev.common.event

import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room

abstract class SocketEvent {
    abstract val type: String

    companion object {
        const val TYPE_ROOM_CREATED = "r_c"
        const val TYPE_PLAYER_JOINED = "p_j"
        const val TYPE_PLAYER_WON = "p_w"
    }
}

class RoomWinnerEvent(val room: Room, val winner: Player) : SocketEvent() {
    override val type: String = TYPE_PLAYER_WON
}

class PlayerJoinedEvent(val room: Room, val player: Player) : SocketEvent() {
    override val type: String = TYPE_PLAYER_JOINED
}