package org.dvulist.monitoringsystem
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.dvulist.monitoringsystem.model.SystemInfo
import org.dvulist.monitoringsystem.utils.Logger

@Composable
@Preview
fun App() {
    var cpuLoad by remember { mutableStateOf(0.0) }
    var totalRam by remember { mutableStateOf(0L) }
    var usedRam by remember { mutableStateOf(0L) }
    var freeRam by remember { mutableStateOf(0L) }
    var diskInfo by remember { mutableStateOf(Triple(0L, 0L, 0L)) }
    var processes by remember { mutableStateOf(emptyList<SystemInfo.ProcessInfo>()) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                cpuLoad = SystemInfo.getCpuLoad()
                totalRam = SystemInfo.getTotalRam()
                usedRam = SystemInfo.getUsedRam()
                freeRam = SystemInfo.getFreeRam()
                diskInfo = SystemInfo.getDiskInfo()
                processes = SystemInfo.getProcesses()

                Logger.log("CPU Load: %.1f%%, RAM Used: %s, Disk Used: %s".format(
                    cpuLoad,
                    formatBytes(usedRam),
                    formatBytes(diskInfo.second)
                ))
            } catch (e: Exception) {
                Logger.log("Error fetching system info: ${e.message}")
            }
            delay(1000)
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Мониторинг Системы", style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Загрузка ЦП: %.1f%%".format(cpuLoad), style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Оперативная память:", style = MaterialTheme.typography.h6)
            Text("Всего: ${formatBytes(totalRam)}")
            Text("Использовано: ${formatBytes(usedRam)}")
            Text("Свободно: ${formatBytes(freeRam)}")
            Spacer(modifier = Modifier.height(8.dp))

            Text("Диск:", style = MaterialTheme.typography.h6)
            Text("Всего: ${formatBytes(diskInfo.first)}")
            Text("Использовано: ${formatBytes(diskInfo.second)}")
            Text("Свободно: ${formatBytes(diskInfo.third)}")
            Spacer(modifier = Modifier.height(16.dp))

            Text("Топ 30 процессов по использованию памяти:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                        Text("Имя", Modifier.weight(0.6f), fontWeight = FontWeight.Bold)
                        Text("CPU", Modifier.weight(0.2f), fontWeight = FontWeight.Bold)
                        Text("Память", Modifier.weight(0.2f), fontWeight = FontWeight.Bold)
                    }
                }
                items(processes) { process ->
                    Row(Modifier.fillMaxWidth()) {
                        Text(process.name, Modifier.weight(0.6f))
                        Text(process.cpu, Modifier.weight(0.2f))
                        Text(process.memory, Modifier.weight(0.2f))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    val units = listOf("B", "KB", "MB", "GB", "TB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size > 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    return "%.1f %s".format(size, units[unitIndex])
}