package com.idfinance.logkmpanion.data.network

import com.benasher44.uuid.uuid4
import com.idfinance.logkmpanion.domain.handleError
import com.idfinance.logkmpanion.domain.handleResponse
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.observer.ResponseHandler
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
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
            } catch (it: Throwable) {
                handleError(requestId, it.message)
                throw it
            }
        }

        val observer: ResponseHandler = observer@{ response ->
            val uuid = response.request.attributes[requestIdKey]
            val requestStart = response.request.attributes[requestStartKey]
            val statusCode = response.status.value
            val body = response.bodyAsText()
            val responseTime = response.responseTime
            val duration = responseTime.timestamp - requestStart
            val headers = response.headers.entries().map { "${it.key}=${it.value.joinToString()}" }
            handleResponse(uuid, statusCode, headers, body, responseTime, duration)
        }

        ResponseObserver.install(ResponseObserver.prepare { onResponse(observer) }, client)
    }
