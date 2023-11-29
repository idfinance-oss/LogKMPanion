package com.idfinance.kmm.debugview.domain.usecase

import com.idfinance.kmm.debugview.domain.repository.LogRepository

internal class ClearLogsUseCase(
    private val repository: LogRepository,
) {

    suspend operator fun invoke() = repository.clearLogs()
}
