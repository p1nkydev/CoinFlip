package com.pinkydev.common.event

import com.pinkydev.common.model.CreateRoomRequest
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import com.pinkydev.common.model.JoinToRoomRequest

abstract class SocketEvent {
    abstract val type: String

    companion object {
        const val TYPE_PLAYER_JOINED = "p_j"
        const val TYPE_PLAYER_WON = "p_w"
        const val TYPE_GET_ROOMS = "s_g"
        const val TYPE_CREATE_ROOM = "c_r"
        const val TYPE_JOIN_ROOM = "j_r"
        const val TYPE_AVAILABLE_ROOMS_RESPONSE = "a_r"
    }
}

data class CreateRoomEvent(val createRoomRequest: CreateRoomRequest) : SocketEvent() {
    override val type: String = TYPE_CREATE_ROOM
}

data class JoinToRoomEvent(val joinToRoomRequest: JoinToRoomRequest) : SocketEvent() {
    override val type: String = TYPE_JOIN_ROOM
}

class GetRoomsEvent : SocketEvent() {
    override val type: String = TYPE_GET_ROOMS
}

data class AvailableRoomsEvent(val rooms: List<Room>) : SocketEvent() {
    override val type: String = TYPE_AVAILABLE_ROOMS_RESPONSE
}

data class RoomWinnerEvent(
    val roomId: Long,
    val winner: Player,
    val looser: Player,
    val moneyAmount: Int,
    val spins: Int,
    val time: Long
) : SocketEvent() {
    override val type: String = TYPE_PLAYER_WON
}

class PlayerJoinedEvent(val player: Player) : SocketEvent() {
    override val type: String = TYPE_PLAYER_JOINED
}