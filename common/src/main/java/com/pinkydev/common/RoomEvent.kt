package com.pinkydev.common

import kotlin.random.Random.Default.nextInt

abstract class RoomEvent {
    abstract val type: String

    companion object {
        const val TYPE_PLAYER_JOINED = "plr_join"
        const val TYPE_PLAYER_WINNER = "plr_winner"
    }
}

class PlayerJoinedEvent(val player: Player) : RoomEvent() {
    override val type: String = TYPE_PLAYER_JOINED
}

class RoomWinnerEvent(val winner: Player) : RoomEvent() {
    override val type: String = TYPE_PLAYER_WINNER
    val flipCount = nextInt(3, 15)
    val timeMillis = nextInt(300, 1000)
}