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

package net.skyscanner.hive.gradletimelogger.report.mixpanel

import net.skyscanner.hive.gradletimelogger.measure.TaskMeasurement
import net.skyscanner.hive.gradletimelogger.process.ProcessedBuildResult
import net.skyscanner.hive.gradletimelogger.process.ProcessedReport
import net.skyscanner.hive.gradletimelogger.report.util.SystemInfo

data class MixpanelEvent(val name: String, val properties: Map<String, Any?>) {
    companion object {
        const val Build = "Build"
        const val BuildTask = "BuildTask"
    }
}

fun ProcessedReport.toEvents(): List<MixpanelEvent> {
    val buildEvent = MixpanelEvent(
        name = MixpanelEvent.Build,
        properties = mapOf(
            "buildFailed" to buildResult.buildFailed,
            "buildFailure" to buildResult.buildFailure?.message
        )
    )
    val buildModuleEvents = measurements.map(TaskMeasurement::mixpanelEvent)
    return (buildModuleEvents + buildEvent)
        .map { it.addGradleInfo(buildResult) }
        .map { it.addSystemInfo(systemInfo) }
        .map { it.addTotalBuildTime(totalElapsedBuildTimeMs) }
}

private fun MixpanelEvent.addGradleInfo(buildResult: ProcessedBuildResult) = copy(
    properties = properties + mapOf(
        "buildAction" to buildResult.buildAction,
        "buildTaskNames" to buildResult.buildTaskNames,
        "gradleVersion" to buildResult.gradleVersion,
        "gradleProjectName" to buildResult.gradleProjectName
    )
)

private fun MixpanelEvent.addTotalBuildTime(totalElapsedBuildTimeMs: Long): MixpanelEvent =
    copy(
        properties = properties + mapOf(
            "totalElapsedBuildTimeMs" to totalElapsedBuildTimeMs
        )
    )


private fun MixpanelEvent.addSystemInfo(systemInfo: SystemInfo): MixpanelEvent = copy(
    properties = properties + mapOf(
        "cpuIdentifier" to systemInfo.cpuIdentifier,
        "hostname" to systemInfo.hostname,
        "localMidnightUTCTimestamp" to systemInfo.localMidnightUTCTimestamp,
        "maxMemory" to systemInfo.maxMemory,
        "osIdentifier" to systemInfo.osIdentifier
    )
)


private fun TaskMeasurement.mixpanelEvent(): MixpanelEvent = MixpanelEvent(
    name = MixpanelEvent.BuildTask,
    properties = mapOf(
        "buildTimeMs" to ms,
        "task" to path,
        "success" to success,
        "skipped" to skipped,
        "didWork" to didWork,
        "skipMessage" to skipMessage,
        "upToDate" to upToDate,
        "noSource" to noSource
    )
)
