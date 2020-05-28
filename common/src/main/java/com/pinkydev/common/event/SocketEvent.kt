package com.pinkydev.common.event

import com.pinkydev.common.model.Player
import com.pinkydev.common.model.SearchRequest

abstract class SocketEvent {
    abstract val type: String

    companion object {
        const val TYPE_PLAYER_JOINED = "p_j"
        const val TYPE_PLAYER_WON = "p_w"
        const val TYPE_SEARCH_GAME = "s_g"
    }
}

data class SearchGameEvent(val searchRequest: SearchRequest) : SocketEvent() {
    override val type: String = TYPE_SEARCH_GAME
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