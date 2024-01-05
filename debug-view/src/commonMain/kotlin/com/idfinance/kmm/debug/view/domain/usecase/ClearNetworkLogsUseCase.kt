package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class ClearNetworkLogsUseCase(
    private val repository: LogRepository,
) {

    suspend operator fun invoke() = repository.clearNetworkLogs()
}