package com.pinkydev.server.local.user

import com.pinkydev.common.model.User
import com.pinkydev.common.model.UserCredentials
import com.pinkydev.common.model.UserToken
import com.pinkydev.server.simpleJwt

class UserCacheImpl : UserCache {

    private var nextUserId = 0
        get() {
            field += 1
            return field
        }

    private val users = mutableMapOf<User, MutableList<UserToken>>()

    override fun registerUser(credentials: UserCredentials): UserToken =
        User(nextUserId, credentials.name).generateToken()

    override fun login(credentials: UserCredentials): UserToken? =
        users
            .keys
            .firstOrNull { it.name == credentials.name }
            ?.generateToken()

    override fun getUserById(id: Int): User = users.keys.first { it.id == id }

    override fun getUserByName(name: String): User? = users.keys.firstOrNull { it.name == name }

    override fun updateUser(user: User): User {
        val savedUser = users.keys.first { it.id == user.id }
        val tokens = users[savedUser]
        val updatedUser = savedUser.copy(name = user.name, balance = user.balance)
        users.remove(savedUser)
        users[updatedUser] = tokens ?: mutableListOf()
        return user
    }

    override fun getUserByToken(token: UserToken): User {
        users.forEach {
            if (it.value.contains<UserToken>(token)) {
                return it.key
            }
        }
        throw IllegalStateException("No user with such token: $token")
    }

    private fun User.generateToken(): UserToken {
        val token = UserToken(simpleJwt.sign(name))
        val tokens = users.getOrPut(this) { mutableListOf() }
        tokens.add(token)
        return token
    }
}