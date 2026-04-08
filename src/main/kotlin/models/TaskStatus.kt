package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskStatus(
    val taskId: String,
    val appId: Int,
    var status: String,
    var progress: Int,
    var resultUrl: String? = null
)