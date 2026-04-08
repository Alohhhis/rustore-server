package com.example

import com.example.routes.registerAppRoutes
import com.example.routes.registerTaskRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.http.content.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    registerAppRoutes()
    registerTaskRoutes()

    routing {
        staticFiles("/images", File("src/main/resources/images"))
        staticFiles("/downloads", File("downloads"))
    }

}