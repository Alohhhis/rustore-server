package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.AppService

fun Application.registerAppRoutes() {
    val appService = AppService()

    routing {
        get("/apps") {
            call.respond(appService.getAllApps())
        }

        get("/apps/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond("Invalid app ID")
                return@get
            }

            val app = appService.getAppById(id)
            if (app == null) {
                call.respond("App not found")
            } else {
                call.respond(app)
            }
        }
    }
}