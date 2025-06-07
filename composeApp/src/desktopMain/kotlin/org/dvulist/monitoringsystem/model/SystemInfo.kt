package org.dvulist.monitoringsystem.model

import oshi.SystemInfo
import oshi.hardware.HardwareAbstractionLayer
import oshi.software.os.OperatingSystem

object SystemInfo {
    private val systemInfo = SystemInfo()
    private val hardware: HardwareAbstractionLayer = systemInfo.hardware
    private val os: OperatingSystem = systemInfo.operatingSystem

    fun getCpuLoad(): Double {
        return hardware.processor.getSystemCpuLoad(500) * 100
    }

    fun getTotalRam(): Long = hardware.memory.total
    fun getUsedRam(): Long = hardware.memory.total - hardware.memory.available
    fun getFreeRam(): Long = hardware.memory.available

    fun getDiskInfo(): Triple<Long, Long, Long> {
        val fileStore = os.fileSystem.fileStores.first()
        return Triple(
            fileStore.totalSpace,
            fileStore.totalSpace - fileStore.freeSpace,
            fileStore.freeSpace
        )
    }

    fun getProcesses(): List<ProcessInfo> {
        return os.processes
            .sortedByDescending { it.residentSetSize }
            .take(30)
            .map {
                ProcessInfo(
                    name = it.name,
                    cpu = String.format("%.1f%%", it.processCpuLoadCumulative),
                    memory = formatBytes(it.residentSetSize)
                )
            }
    }

    private fun formatBytes(bytes: Long): String {
        val units = listOf("B", "KB", "MB", "GB")
        var size = bytes.toDouble()
        var unitIndex = 0

        while (size > 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }

        return "%.1f %s".format(size, units[unitIndex])
    }

    data class ProcessInfo(val name: String, val cpu: String, val memory: String)
}