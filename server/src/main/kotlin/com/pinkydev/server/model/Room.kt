package com.pinkydev.server.model

import com.pinkydev.common.Player
import kotlin.random.Random.Default.nextInt

data class Room(
    val id: Long = 0L,
    val maxPlayersCount: Int,
    val moneyAmount: Float,
    var players: List<Player>
) {
    val winner: Player
        get() = players[nextInt(players.lastIndex)]

}