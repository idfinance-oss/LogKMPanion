package com.idfinance.kmm.debug.view.domain

import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.data.model.Request
import com.idfinance.kmm.debug.view.data.model.Response
import com.idfinance.kmm.debug.view.domain.usecase.SaveLogUseCase
import com.idfinance.kmm.debug.view.presentation.extensions.toUIFormat
import io.ktor.util.date.GMTDate
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

private val coroutineScope by lazy(::MainScope)

fun handleLog(type: LogType, tag: String, message: String) {
    val useCase = ServiceLocator.saveLogUseCase
    coroutineScope.launch {
        useCase(
            SaveLogUseCase.Payload(
                type,
                tag,
                message,
                Clock.System.now().toEpochMilliseconds()
            )
        )
    }
}

internal fun handleRequest(
    sessionId: String,
    uuid: String,
    url: String,
    method: String,
    headers: List<String>,
    cookies: List<String>,
    body: String
) {
    val useCase = ServiceLocator.saveRequestUseCase
    coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }) {
        val request = Request().apply {
            this.url = url
            this.method = method
            this.headers = headers.toRealmList()
            this.cookies = cookies.toRealmList()
            this.body = body
        }
        useCase(uuid, sessionId, request)
    }
}

internal fun handleResponse(
    uuid: String,
    statusCode: Int,
    headers: List<String>,
    body: String,
    responseTime: GMTDate,
    duration: Long
) {
    val useCase = ServiceLocator.saveResponseUseCase
    coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }) {
        val response = Response().apply {
            this.statusCode = statusCode
            this.headers = headers.toRealmList()
            this.body = body
            this.time = responseTime.toUIFormat()
        }
        useCase(uuid, duration, response)
    }
}

internal fun handleError(requestId: String, message: String?) {
    val useCase = ServiceLocator.saveErrorUseCase
    coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }) {
        useCase(requestId, message)
    }
}

enum class LogType {
    DEFAULT,
    ERROR,
}
