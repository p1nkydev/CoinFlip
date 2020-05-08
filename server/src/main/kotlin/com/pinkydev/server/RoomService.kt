package com.pinkydev.server

import com.pinkydev.common.Player
import com.pinkydev.common.User
import com.pinkydev.server.local.user.UserCache
import com.pinkydev.server.model.Room
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class RoomService(private val userCache: UserCache) {
    private var newRoomId: Long = 0L
        get() {
            field += 1
            return field
        }

    private val roomHandlers = mutableListOf(
        RoomHandler(
            Room(newRoomId, 2, 1f, listOf()),
            userCache
        )
    )

    private lateinit var roomActor: SendChannel<List<Room>>


    fun observeRooms() = callbackFlow<List<Room>> {
        roomActor = actor {
            for (each in channel) {
                this@callbackFlow.send(each)
            }
        }
        awaitClose()
    }


    suspend fun createRoom(creator: Player, maxPlayersCount: Int, moneyAmount: Float) {
        val room = Room(newRoomId, maxPlayersCount, moneyAmount, listOf(creator))
        val roomHandler = RoomHandler(room, userCache)
        roomHandler.join(creator)
        roomHandlers.add(roomHandler)
        updateRooms()
    }

    suspend fun joinRoom(roomId: Long, playerId: Int) {
        val player = userCache.getUserById(playerId).toPlayer()
        roomHandlers
            .firstOrNull { it.room.id == roomId }
            ?.let { it.join(player) }
        updateRooms()
    }

    private suspend fun updateRooms() {
        roomActor.send(roomHandlers.map { it.room })
    }

    private fun User.toPlayer() = Player(id, name)
}
