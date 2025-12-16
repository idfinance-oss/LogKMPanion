package com.idfinance.logkmpanion.domain

import com.idfinance.logkmpanion.presentation.extensions.toUIFormat
import com.idfinance.logkmpanion.ServiceLocator
import com.idfinance.logkmpanion.data.model.Request
import com.idfinance.logkmpanion.data.model.Response
import com.idfinance.logkmpanion.domain.usecase.SaveLogUseCase
import io.ktor.util.date.GMTDate
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val coroutineScope by lazy(::MainScope)

private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

@OptIn(ExperimentalTime::class)
fun addToLogKMPanion(type: LogType, tag: String, message: String) {
    val useCase = ServiceLocator.saveLogUseCase
    coroutineScope.launch(exceptionHandler) {
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
    coroutineScope.launch(exceptionHandler) {
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
    coroutineScope.launch(exceptionHandler) {
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
    coroutineScope.launch(exceptionHandler) {
        useCase(requestId, message)
    }
}

enum class LogType {
    DEFAULT,
    ERROR,
}
