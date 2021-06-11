package com.pinkydev.common.model

import kotlin.random.Random.Default.nextInt

data class User(
    val id: Int = nextInt(),
    val name: String = "",
    val balance: Int = 100
)