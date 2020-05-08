package com.pinkydev.server

import com.pinkydev.common.Player
import com.pinkydev.common.PlayerJoinedEvent
import com.pinkydev.common.RoomEvent
import com.pinkydev.common.RoomWinnerEvent
import com.pinkydev.server.local.user.UserCache
import com.pinkydev.server.model.Room
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class RoomHandler(val room: Room, private val userCache: UserCache) {

    private lateinit var scope: ProducerScope<RoomEvent>

    val events: Flow<RoomEvent> = channelFlow {
        scope = this
        awaitClose()
    }

    fun close() {
        scope.close()
    }

    suspend fun join(player: Player) {
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
            scope.send(RoomWinnerEvent(winner))
            close()
        } else {
            scope.send(PlayerJoinedEvent(player))
        }
    }

}