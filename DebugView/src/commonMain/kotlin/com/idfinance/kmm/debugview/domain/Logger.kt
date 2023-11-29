package com.idfinance.kmm.debugview.domain

import com.idfinance.kmm.debugview.ServiceLocator
import com.idfinance.kmm.debugview.domain.usecase.SaveLogUseCase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

fun handleLog(type: LogType, tag: String, message: String) {
    val useCase = ServiceLocator.saveLogUseCase
    MainScope().launch {
        useCase(SaveLogUseCase.Payload(type, tag, message, Clock.System.now().toEpochMilliseconds()))
    }
}

enum class LogType {
    DEFAULT,
    ERROR,
}
