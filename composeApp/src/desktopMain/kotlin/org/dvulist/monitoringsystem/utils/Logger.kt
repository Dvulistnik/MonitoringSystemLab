package org.dvulist.monitoringsystem.utils

import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object Logger {
    private val file = File("monitor_log.txt")
    private val lock = ReentrantLock()

    fun log(data: String) {

        lock.withLock {
            try {
                file.appendText("${LocalDateTime.now()} - $data\n")
            } catch (e: Exception) {
                System.err.println("Failed to write log: ${e.message}")
            }
        }
    }
}