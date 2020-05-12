package com.pinkydev.server.local.user

import com.pinkydev.common.model.User
import com.pinkydev.common.model.UserCredentials

class UserCacheImpl : UserCache {

    private var nextUserId = 0
        get() {
            field += 1
            return field
        }

    private val users = mutableListOf<User>()

    override fun registerUser(credentials: UserCredentials): User {
        val user = User(nextUserId, credentials.name)
        users.add(user)
        return user
    }

    override fun login(credentials: UserCredentials): User? {
        return users.firstOrNull { it.name == credentials.name }
    }

    override fun getUserById(id: Int): User = users.first { it.id == id }

    override fun getUserByName(name: String): User? = users.firstOrNull { it.name == name }

    override fun updateUser(user: User): User {
        users.replaceAll { if (it.id == user.id) user else it }
        return user
    }
}