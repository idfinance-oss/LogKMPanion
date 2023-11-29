package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class GetLogsFlowUseCase(
    private val repository: LogRepository,
) {

    operator fun invoke() = repository.readLogs()
}
