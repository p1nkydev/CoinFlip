package com.pinkydev.common.model

data class JoinToRoomRequest(
    val player: Player,
    val room: Room,
)

data class CreateRoomRequest(
    val player: Player,
    val moneyAmount: Int,
)