package com.example.services

import com.example.models.TaskStatus
import kotlinx.coroutines.*
import java.io.File
import java.net.URI
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory

object TaskService {

    private val tasks = ConcurrentHashMap<String, TaskStatus>()
    private val taskJobs = ConcurrentHashMap<String, Job>()

    private val log = LoggerFactory.getLogger(TaskService::class.java)

    private val DOWNLOAD_DIR = File(System.getProperty("user.dir"), "downloads").path

    fun createDownloadTask(appId: Int, apkUrl: String): String {
        val taskId = java.util.UUID.randomUUID().toString()
        val taskStatus = TaskStatus(taskId, appId, "pending", 0)
        tasks[taskId] = taskStatus

        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                taskStatus.status = "downloading"

                val downloadDir = File(DOWNLOAD_DIR)
                downloadDir.mkdirs()

                val tmpFile = File(downloadDir, "$taskId.apk")

                val normalizedLocalPath = runCatching {
                    when {
                        apkUrl.startsWith("/downloads/") -> apkUrl
                        apkUrl.startsWith("http://") || apkUrl.startsWith("https://") -> {
                            val uri = URI(apkUrl)
                            val host = (uri.host ?: "").lowercase()
                            val path = uri.rawPath ?: ""
                            val looksLikeOurLocalDownloads =
                                (host == "10.0.2.2" || host == "localhost" || host == "127.0.0.1") &&
                                        path.startsWith("/downloads/")
                            if (looksLikeOurLocalDownloads) path else null
                        }

                        else -> null
                    }
                }.getOrNull()

                val localSourceFile = normalizedLocalPath?.let { File("src/main/resources$it") }
                val isLocalSource = localSourceFile != null

                val totalSize: Long? = if (isLocalSource) {
                    if (localSourceFile!!.exists()) localSourceFile.length() else null
                } else {
                    runCatching { URL(apkUrl).openConnection().contentLengthLong }.getOrNull()
                        ?.takeIf { it > 0 }
                }

                val inputStream = if (isLocalSource) {
                    require(localSourceFile!!.exists()) { "Local APK not found: ${localSourceFile.absolutePath}" }
                    localSourceFile.inputStream()
                } else {
                    URL(apkUrl).openStream()
                }

                inputStream.use { input ->
                    tmpFile.outputStream().use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int
                        var totalRead = 0L

                        while (input.read(buffer).also { bytesRead = it } != -1 && isActive) {
                            output.write(buffer, 0, bytesRead)
                            totalRead += bytesRead
                            if (totalSize != null && totalSize > 0) {
                                taskStatus.progress = ((totalRead * 100) / totalSize).toInt()
                            }
                        }
                    }
                }

                if (!isActive) {
                    taskStatus.status = "canceled"
                    tmpFile.delete()
                } else {
                    taskStatus.status = "completed"
                    taskStatus.progress = 100
                    taskStatus.resultUrl = "/downloads/${tmpFile.name}"
                }

            } catch (e: Exception) {
                taskStatus.status = "failed"
                log.warn("Download task failed: taskId={}, appId={}, apkUrl={}", taskId, appId, apkUrl, e)
            } finally {
                taskJobs.remove(taskId)
            }
        }

        taskJobs[taskId] = job
        return taskId
    }

    fun getStatus(taskId: String): TaskStatus? = tasks[taskId]

    fun cancelTask(taskId: String) {
        taskJobs[taskId]?.cancel()
        tasks[taskId]?.status = "canceled"
    }
}