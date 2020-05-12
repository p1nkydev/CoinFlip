package com.pinkydev.server

import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import com.pinkydev.common.model.User
import com.pinkydev.server.local.user.UserCache

class RoomService(private val userCache: UserCache, private val eventHandler: EventHandler) {

    private var newRoomId: Long = 0L
        get() {
            field += 1
            return field
        }

    private val roomHandlers = mutableListOf<RoomHandler>()

    val rooms get() = roomHandlers.map { it.room }

    fun createRoom(creator: Player, maxPlayersCount: Int, moneyAmount: Float) {
        val room = Room(
            newRoomId,
            maxPlayersCount,
            moneyAmount,
            listOf()
        )
        roomHandlers.add(RoomHandler(room, userCache, eventHandler))
        joinRoom(room.id, creator.id)
    }

    fun joinRoom(roomId: Long, playerId: Int) {
        val player = userCache.getUserById(playerId).toPlayer()
        roomHandlers
            .firstOrNull { it.room.id == roomId }
            ?.let { it.join(player) }
    }

    private fun User.toPlayer() = Player(id, name)
}
