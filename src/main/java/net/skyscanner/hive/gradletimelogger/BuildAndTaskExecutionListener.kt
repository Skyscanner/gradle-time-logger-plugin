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

package net.skyscanner.hive.gradletimelogger

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

open class BuildAndTaskExecutionListener : BuildListener, TaskExecutionListener {

    override fun buildStarted(gradle: Gradle) = Unit

    override fun settingsEvaluated(settings: Settings) = Unit

    override fun projectsLoaded(gradle: Gradle) = Unit

    override fun projectsEvaluated(gradle: Gradle) = Unit

    override fun buildFinished(buildResult: BuildResult) = Unit

    override fun beforeExecute(task: Task) = Unit

    override fun afterExecute(task: Task, taskState: TaskState) = Unit
}
