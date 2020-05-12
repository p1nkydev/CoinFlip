package com.pinkydev.server

import com.pinkydev.common.event.PlayerJoinedEvent
import com.pinkydev.common.event.RoomWinnerEvent
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import com.pinkydev.server.local.user.UserCache

class RoomHandler(
    val room: Room,
    private val userCache: UserCache,
    private val eventHandler: EventHandler
) {

    fun join(player: Player) {
        room.players = room.players + player
        if (room.players.size == room.maxPlayersCount) {
            val winner = room.winner
            // updating losers
            room.players
                .filter { it.id != winner.id }
                .map { userCache.getUserById(it.id) }
                .map { it.copy(balance = it.balance - room.moneyAmount) }
                .forEach { userCache.updateUser(it) }

            // updating winner
            val user = userCache.getUserById(winner.id)
            userCache.updateUser(user.copy(balance = user.balance + room.moneyAmount))
            eventHandler.notifyThat(RoomWinnerEvent(room, winner))
        } else {
            eventHandler.notifyThat(PlayerJoinedEvent(room, player))
        }
    }

}