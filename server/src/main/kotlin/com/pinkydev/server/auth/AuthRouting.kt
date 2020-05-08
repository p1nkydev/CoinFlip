package com.pinkydev.server.auth

import com.google.api.client.http.HttpStatusCodes
import com.pinkydev.common.UserCredentials
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext

fun Route.login(userCache: UserCacheImpl) {
    get("/login") {
        val credentials = getCredentials() ?: run {
            call.respond(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
            return@get
        }
        userCache.login(credentials)?.let {
            call.respond(HttpStatusCodes.STATUS_CODE_OK)
        } ?: call.respond(HttpStatusCodes.STATUS_CODE_FORBIDDEN)
    }
}

fun Route.signUp(userCache: UserCacheImpl) {
    get("/signup") {
        getCredentials()?.let {
            userCache.registerUser(it)
            call.respond(HttpStatusCodes.STATUS_CODE_OK)
        } ?: call.respond(HttpStatusCodes.STATUS_CODE_BAD_REQUEST)
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getCredentials(): UserCredentials? {
    val username = call.request.queryParameters["username"] ?: return null
    val password = call.request.queryParameters["password"] ?: return null
    return UserCredentials(username, password)
}