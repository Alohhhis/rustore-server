package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.services.TaskService
import kotlinx.serialization.Serializable

@Serializable
data class DownloadRequest(val appId: Int, val apkUrl: String)

fun Application.registerTaskRoutes() {
    routing {

// создание задачки
        post("/tasks/download") {
            val request = call.receive<DownloadRequest>()
            val taskId = TaskService.createDownloadTask(request.appId, request.apkUrl)
            call.respond(mapOf("taskId" to taskId))
        }
// статус задачки
        get("/tasks/{taskId}/status") {
            val taskId = call.parameters["taskId"] ?: return@get call.respond(mapOf("error" to "taskId required"))
            val status = TaskService.getStatus(taskId)
            if (status == null) {
                call.respond(mapOf("error" to "Task not found"))
            } else {
                call.respond(status)
            }
        }

// охрана отмена задачи
        post("/tasks/{taskId}/cancel") {
            val taskId = call.parameters["taskId"] ?: return@post call.respond(mapOf("error" to "taskId required"))
            TaskService.cancelTask(taskId)
            call.respond(mapOf("message" to "Task canceled"))
        }

// результат задачки
        get("/tasks/{taskId}/result") {
            val taskId = call.parameters["taskId"] ?: return@get call.respond(mapOf("error" to "taskId required"))
            val status = TaskService.getStatus(taskId)
            if (status == null) {
                call.respond(mapOf("error" to "Task not found"))
            } else if (status.status != "completed") {
                call.respond(mapOf("error" to "Task not completed yet"))
            } else {
                call.respond(mapOf("apkPath" to status.resultUrl))
            }
        }
    }
}