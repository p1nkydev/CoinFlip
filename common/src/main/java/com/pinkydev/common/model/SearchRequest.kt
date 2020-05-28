package com.pinkydev.common.model

data class SearchRequest(
    val player: Player,
    val maxPlayersCount: Int,
    val moneyAmount: Int
)