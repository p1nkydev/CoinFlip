package com.pinkydev.common

data class RoomCreation(
    val creator: Player,
    val maxPlayersCount: Int,
    val moneyAmount: Float
)