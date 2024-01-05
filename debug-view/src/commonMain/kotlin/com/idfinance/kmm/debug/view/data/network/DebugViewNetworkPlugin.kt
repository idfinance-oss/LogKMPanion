package com.idfinance.kmm.debug.view.data.network

import com.benasher44.uuid.uuid4
import com.idfinance.kmm.debug.view.domain.handleError
import com.idfinance.kmm.debug.view.domain.handleResponse
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.observer.ResponseHandler
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.util.AttributeKey
import io.ktor.util.date.getTimeMillis

internal val requestIdKey = AttributeKey<String>("RequestID")
internal val requestStartKey = AttributeKey<Long>("RequestStart")

private fun getRequestId() = uuid4().toString()

fun debugViewNetworkPlugin(sessionId: String = uuid4().toString()) = createClientPlugin("DebugViewNetworkPlugin") {
    on(SendHook) { request ->
        val requestId = getRequestId()
        request.attributes.put(requestIdKey, requestId)
        request.attributes.put(requestStartKey, getTimeMillis())
        val loggedRequest = runCatching { logRequest(sessionId, request) }.getOrNull()

        try {
            proceedWith(loggedRequest ?: request.body)
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
