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

import net.skyscanner.hive.gradletimelogger.report.Report
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

open class BuildTimeLoggerPluginExtension(objects: ObjectFactory) {
    val mixpanelProjectTokenProperty: Property<String> = objects.property()
    val reportsProperty: Property<Set<Report>> = objects.property()
    val anonymousProperty: Property<Boolean?> = objects.property()
}

var BuildTimeLoggerPluginExtension.mixpanelProjectToken: String
    get() = mixpanelProjectTokenProperty.get()
    set(value) = mixpanelProjectTokenProperty.set(value)

var BuildTimeLoggerPluginExtension.reports: Set<Report>
    get() = reportsProperty.get()
    set(value) = reportsProperty.set(value)

var BuildTimeLoggerPluginExtension.anonymous: Boolean?
    get() = anonymousProperty.orNull
    set(value) = anonymousProperty.set(value)
