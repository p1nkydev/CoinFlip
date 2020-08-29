package com.pinkydev.server.auth

import com.pinkydev.common.model.UserCredentials
import com.pinkydev.server.userCache
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext

fun Route.login() {
    get("/login") {
        getCredentials()?.let { credentials ->
            val token = userCache.login(credentials) ?: userCache.registerUser(credentials)
            call.respond(HttpStatusCode.OK, token)
        } ?: call.respond(HttpStatusCode.BadRequest)
    }
}

private fun PipelineContext<Unit, ApplicationCall>.getCredentials(): UserCredentials? {
    val username = call.request.queryParameters["username"] ?: return null
    val password = call.request.queryParameters["password"] ?: return null
    return UserCredentials(username, password)
}