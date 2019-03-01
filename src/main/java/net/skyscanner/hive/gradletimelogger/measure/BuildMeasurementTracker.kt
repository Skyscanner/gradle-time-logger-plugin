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

package net.skyscanner.hive.gradletimelogger.measure

import com.google.common.base.Stopwatch
import net.skyscanner.hive.gradletimelogger.BuildAndTaskExecutionListener
import net.skyscanner.hive.gradletimelogger.process.ProcessedReport
import net.skyscanner.hive.gradletimelogger.process.reportProcessing
import net.skyscanner.hive.gradletimelogger.report.Reporter
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskState
import java.util.concurrent.TimeUnit

class BuildMeasurementTracker(
    private val reporters: Provider<Set<Reporter>>,
    private val measurements: MutableList<TaskMeasurement> = mutableListOf(),
    private val processReport: (List<TaskMeasurement>, BuildResult, Long) -> ProcessedReport = reportProcessing
) : BuildAndTaskExecutionListener() {

    private val watches = mutableMapOf<Task, Stopwatch>()
    private val mainWatch = Stopwatch.createUnstarted()

    override fun projectsEvaluated(gradle: Gradle) {
        mainWatch.start()
    }

    override fun beforeExecute(task: Task) {
        watches[task] = Stopwatch.createStarted()
    }

    override fun afterExecute(task: Task, taskState: TaskState) {
        val elapsed = watches[task]!!.elapsed(TimeUnit.MILLISECONDS)
        measurements += TaskMeasurement(
            elapsed,
            task.path,
            taskState.failure == null,
            taskState.didWork,
            taskState.skipped,
            taskState.skipMessage,
            taskState.upToDate,
            taskState.noSource
        )
    }

    override fun buildFinished(buildResult: BuildResult) {
        val elapsed = mainWatch.elapsed(TimeUnit.MILLISECONDS)
        val processedReport = processReport(measurements, buildResult, elapsed)
        reporters.orNull?.forEach { reporter -> reporter(processedReport) }
    }
}
