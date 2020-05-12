package com.pinkydev.common.model

data class RoomCreation(
    val creator: Player,
    val maxPlayersCount: Int,
    val moneyAmount: Float
)