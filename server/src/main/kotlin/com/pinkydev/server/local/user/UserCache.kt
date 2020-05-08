package com.pinkydev.server.local.user

import com.pinkydev.common.User
import com.pinkydev.common.UserCredentials

interface UserCache {
    fun registerUser(credentials: UserCredentials): User
    fun login(credentials: UserCredentials): User?
    fun getUserById(id: Int): User
    fun getUserByName(name: String): User?
    fun updateUser(user: User): User
}