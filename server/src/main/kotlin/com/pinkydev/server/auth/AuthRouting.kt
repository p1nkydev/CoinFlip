package com.pinkydev.server.auth

import com.pinkydev.common.model.UserCredentials
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext

fun Route.login(userCache: UserCacheImpl) {
    get("/login") {
        getCredentials()?.let { credentials ->

            userCache.login(credentials)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.Forbidden)

        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}

fun Route.signUp(userCache: UserCacheImpl) {
    get("/signup") {
        getCredentials()?.let {
            userCache.registerUser(it)
            call.respond(HttpStatusCode.OK)
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getCredentials(): UserCredentials? {
    val username = call.request.queryParameters["username"] ?: return null
    val password = call.request.queryParameters["password"] ?: return null
    return UserCredentials(username, password)
}