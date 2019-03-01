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

package net.skyscanner.hive.gradletimelogger.process

import net.skyscanner.hive.gradletimelogger.measure.TaskMeasurement
import net.skyscanner.hive.gradletimelogger.report.util.gatherSystemInfo
import org.gradle.BuildResult

val reportProcessing: (List<TaskMeasurement>, BuildResult, Long) -> ProcessedReport =
    { measurements: List<TaskMeasurement>, buildResult: BuildResult, elapsed: Long ->
        val systemInfo = gatherSystemInfo()
        ProcessedReport(measurements, buildResult.process(), systemInfo, elapsed)
    }
