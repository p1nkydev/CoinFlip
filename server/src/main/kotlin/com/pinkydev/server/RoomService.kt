package com.pinkydev.server

import com.pinkydev.common.event.PlayerJoinedEvent
import com.pinkydev.common.event.RoomWinnerEvent
import com.pinkydev.common.event.SocketEvent
import com.pinkydev.common.model.CreateRoomRequest
import com.pinkydev.common.model.Player
import com.pinkydev.common.model.Room
import com.pinkydev.common.model.JoinToRoomRequest
import com.pinkydev.server.local.user.UserCache
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.delay
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

class RoomService(private val userCache: UserCache) {

    private var newRoomId: Long = 0L
        get() {
            field += 1
            return field
        }

    val roomSession = mutableMapOf<Room, MutableList<WebSocketSession>>()

    fun getAllRooms(): List<Room> {
        return roomSession.keys.toList()
    }

    suspend fun createRoom(
        createRoomRequest: CreateRoomRequest,
        session: WebSocketSession
    ) {
        val room = Room(
            newRoomId,
            players = listOf(),
            moneyAmount = createRoomRequest.moneyAmount,
        )
        roomSession[room] = mutableListOf()
        joinPlayerTo(createRoomRequest.player, room, session)
    }

    suspend fun joinPlayerTo(player: Player, room: Room, session: WebSocketSession) {
        room.players += player
        roomSession.get(room)?.add(session)
        if (room.players.size < 2) {
            room.broadcast(PlayerJoinedEvent(player))
        } else {
            val winner = room.players.random()
            // updating losers
            room.players
                .filter { it.id != winner.id }
                .map { userCache.getUserById(it.id) }
                .map { it.copy(balance = it.balance - room.moneyAmount) }
                .forEach { userCache.updateUser(it) }

            // updating winner
            val user = userCache.getUserById(winner.id)
            userCache.updateUser(user.copy(balance = user.balance + room.moneyAmount))

            val event = RoomWinnerEvent(
                roomId = room.id,
                winner = winner,
                looser = room.players.first { it.id != winner.id },
                moneyAmount = room.moneyAmount,
                spins = calculateSpinsCount(winner.joinedSide),
                time = Random.nextLong(from = 3000, until = 6000)
            )

            room.broadcast(event)
//            roomSession.get(room)?.forEach {
//                it.flush()
//                it.close()
//            }
            roomSession.remove(room)
        }
    }

    fun cancelGame(playerId: Int) {
        val room = roomSession
            .keys
            .firstOrNull { it.players.firstOrNull { it.id == playerId } != null }

        room?.let { roomSession.remove(it) }
    }

    private fun calculateSpinsCount(winnerSide: Int): Int {
        val random = Random.nextInt(from = 5, until = 50)
        return if (random % 2 == winnerSide) random else random + 1
    }

    private suspend fun Room.broadcast(event: SocketEvent) {
        roomSession.forEach {
            if (it.key == this) {
                it.value.forEach {
                    it.send(event)
                }
            }
        }
    }
}
