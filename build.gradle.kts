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

group = "net.skyscanner.gradletimelogger"
version = properties["pluginVersion"].toString()

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm") version "1.3.11"
    id("com.gradle.plugin-publish") version "0.10.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("com.mixpanel:mixpanel-java:1.4.4")
    implementation("com.google.guava:guava:27.0.1-jre")
}

gradlePlugin {
    plugins {
        register("buildTimeLoggerPlugin") {
            id = "net.skyscanner.gradletimelogger"
            implementationClass = "net.skyscanner.hive.gradletimelogger.BuildTimeLoggerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/skyscanner/gradle-time-logger-plugin"
    vcsUrl = "https://github.com/skyscanner/gradle-time-tracker-plugin.git"
    description = "Gradle plugin to log your build times."
    tags = listOf("gradle", "build", "time", "logger")

    plugins {
        getByName("buildTimeLoggerPlugin") {
            displayName = "Build Time Logger"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
