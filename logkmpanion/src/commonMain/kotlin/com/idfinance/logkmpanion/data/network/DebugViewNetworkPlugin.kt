package com.idfinance.logkmpanion.data.network

import com.benasher44.uuid.uuid4
import com.idfinance.logkmpanion.domain.handleError
import com.idfinance.logkmpanion.domain.handleResponse
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.bodyAsText
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.date.getTimeMillis

internal val requestIdKey = AttributeKey<String>("RequestID")
internal val requestStartKey = AttributeKey<Long>("RequestStart")

private fun getRequestId() = uuid4().toString()

fun logKMPanionNetworkPlugin(sessionId: String = uuid4().toString()) =
    createClientPlugin("DebugViewNetworkPlugin") {
        client.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
            val requestId = getRequestId()
            context.attributes.put(requestIdKey, requestId)
            context.attributes.put(requestStartKey, getTimeMillis())
            logRequest(sessionId, context)

            try {
                proceed()
            } catch (t: Throwable) {
                handleError(requestId, t.message)
                throw t
            }
        }

        onResponse { response ->
            val request = response.call.request
            val uuid = request.attributes[requestIdKey]
            val requestStart = request.attributes[requestStartKey]
            val statusCode = response.status.value
            val responseTime = response.responseTime
            val duration = responseTime.timestamp - requestStart
            val headers = response.headers.entries().map { "${it.key}=${it.value.joinToString()}" }

            val contentType = response.contentType()
            val body: String = when {
                contentType == null -> "<no content-type>"

                contentType.contentType == "application" &&
                        contentType.contentSubtype.contains("json") ->
                    response.bodyAsText()

                else -> buildString {
                    append("<binary skipped>")
                    append(" type=").append(contentType)
                }
            }


            handleResponse(
                uuid = uuid,
                statusCode = statusCode,
                headers = headers,
                body = body,
                responseTime = responseTime,
                duration = duration
            )
        }
    }
