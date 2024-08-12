package com.idfinance.logkmpanion.domain.usecase

import com.idfinance.logkmpanion.domain.repository.LogRepository

internal class SaveErrorUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(id: String, message: String?) {
        repository.saveErrorMessage(id, message)
    }

}