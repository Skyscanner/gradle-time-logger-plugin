/*
 * Copyright 2019 Skyscanner Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package net.skyscanner.hive.gradletimelogger.report.util

import com.sun.management.OperatingSystemMXBean
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import java.io.File
import java.lang.management.ManagementFactory
import java.time.LocalDateTime
import java.time.ZoneId

fun gatherSystemInfo(): SystemInfo = SystemInfo(
    hostname = hostname,
    maxMemory = maxMemory,
    osIdentifier = osIdentifier,
    cpuIdentifier = cpuIdentifier,
    localMidnightUTCTimestamp = localMidnightUTCEpoch
)

data class SystemInfo(
    val hostname: String?,
    val maxMemory: Long?,
    val osIdentifier: String?,
    val cpuIdentifier: String?,
    val localMidnightUTCTimestamp: Long?
)

private val osIdentifier: String?
    get() = listOf("os.name", "os.version", "os.arch").joinToString(" ") { System.getProperty(it) }

private val maxMemory: Long?
    get() = physicalMemoryAvailable

private val cpuIdentifier: String?
    get() = multiplatform(
        osx = { runProcess("sysctl", "-n", "machdep.cpu.brand_string") },
        linux = {
            var osName = ""
            File("/proc/cpuinfo").forEachLine {
                if (osName.isNotEmpty()) return@forEachLine
                if (it.startsWith("model name")) osName = it.split(": ")[1]
            }
            osName
        },
        default = ""
    )

private val physicalMemoryAvailable: Long?
    get() = (ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean).totalPhysicalMemorySize

private val localMidnightUTCEpoch: Long?
    get() = LocalDateTime.now()
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
        .atZone(ZoneId.of("UTC"))
        .toEpochSecond()

private val hostname: String
    get() = multiplatform(
        osx = { runProcess("hostname") },
        linux = { runProcess("hostname") },
        default = ""
    )

private fun runProcess(vararg args: String): String? = try {

    Runtime.getRuntime().exec(args).run {
        waitFor()
        if (exitValue() == 0) ProcessGroovyMethods.getText(this).trim() else null
    }
} catch (ignored: Exception) {
    null
}

private fun <T> multiplatform(
    osx: () -> T? = { null },
    linux: () -> T? = { null },
    windows: () -> T? = { null },
    default: T
): T =
    when (System.getProperty("os.name").toLowerCase()) {
        "mac os x" -> osx()
        "linux" -> linux()
        "windows" -> windows()
        else -> default
    } ?: default
