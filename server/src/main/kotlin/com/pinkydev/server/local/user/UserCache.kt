package com.pinkydev.server.local.user

import com.pinkydev.common.model.User
import com.pinkydev.common.model.UserCredentials
import com.pinkydev.common.model.UserToken

interface UserCache {
    fun registerUser(credentials: UserCredentials): UserToken
    fun login(credentials: UserCredentials): UserToken?
    fun getUserById(id: Int): User
    fun getUserByName(name: String): User?
    fun updateUser(user: User): User
    fun getUserByToken(token: UserToken): User
}