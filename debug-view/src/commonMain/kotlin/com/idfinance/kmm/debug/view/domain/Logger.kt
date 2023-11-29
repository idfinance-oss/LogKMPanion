package com.idfinance.kmm.debug.view.domain

import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.domain.usecase.SaveLogUseCase
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
