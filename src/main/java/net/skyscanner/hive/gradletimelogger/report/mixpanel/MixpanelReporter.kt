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

import com.mixpanel.mixpanelapi.ClientDelivery
import com.mixpanel.mixpanelapi.MessageBuilder
import com.mixpanel.mixpanelapi.MixpanelAPI
import net.skyscanner.hive.gradletimelogger.BuildTimeLoggerPluginExtension
import net.skyscanner.hive.gradletimelogger.anonymous
import net.skyscanner.hive.gradletimelogger.mixpanelProjectToken
import net.skyscanner.hive.gradletimelogger.process.ProcessedReport
import net.skyscanner.hive.gradletimelogger.report.ReporterFactory
import net.skyscanner.hive.gradletimelogger.report.util.md5
import org.gradle.api.logging.Logger
import org.json.JSONObject

val mixpanelReporterFactory: ReporterFactory = { logger: Logger, extension: BuildTimeLoggerPluginExtension ->
    val mixpanelAPI = MixpanelAPI()
    val token = extension.mixpanelProjectToken
    val builder = MessageBuilder(token)

    val reporter = { processedReport: ProcessedReport ->
        try {
            val distinctId = processedReport.systemInfo.hostname
                ?.let { if (extension.anonymous == false) it.md5() else it } ?: "unknown"
            val delivery = ClientDelivery()
            processedReport.toEvents()
                .map { it.mapMixpanelMessage(builder, distinctId) }
                .forEach { delivery.addMessage(it) }
            mixpanelAPI.deliver(delivery)
            logger.lifecycle("\uD83D\uDCE1 Build stats sent to Mixpanel \uD83D\uDCE1")
        } catch (e: RuntimeException) {
            logger.warn("Mixpanel logging failed: $e")
        }
    }
    reporter
}

private fun MixpanelEvent.mapMixpanelMessage(builder: MessageBuilder, distinctId: String) =
    builder.event(distinctId, name, JSONObject(properties))
