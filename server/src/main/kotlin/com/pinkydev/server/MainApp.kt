package com.pinkydev.server


import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson
import com.pinkydev.common.model.UserToken
import com.pinkydev.server.auth.login
import com.pinkydev.server.game.gameSocket
import com.pinkydev.server.game.rooms
import com.pinkydev.server.local.user.UserCacheImpl
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.cio.websocket.send
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.slf4j.event.Level
import java.time.Duration


val userCache = UserCacheImpl()

val roomService = RoomService(userCache)

val serializer = Gson()

val simpleJwt = SimpleJWT("my-super-secret-for-jwt")

class SimpleJWT(secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).build()
    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.main() {
    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }

    install(ContentNegotiation) {
        gson { setPrettyPrinting() }
    }

    install(CallLogging) {
        level = Level.INFO
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(1000)
        timeout = Duration.ofSeconds(10000)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        login()
        authenticate {
            gameSocket()
            rooms()
            user()
        }
    }
}

private fun Route.user() {
    get("/user") {
        val token = call.request.authorization()?.removePrefix("Bearer ") ?: kotlin.run {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        call.respond(HttpStatusCode.OK, userCache.getUserByToken(UserToken(token)))
    }
}

suspend fun <T> WebSocketSession.send(what: T) {
    send(serializer.toJson(what))
}