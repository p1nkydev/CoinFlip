package com.pinkydev.server


import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing


fun Application.main() {
    routing {
        get("/") {
            call.respond("Hello World!!")
        }
    }
}
