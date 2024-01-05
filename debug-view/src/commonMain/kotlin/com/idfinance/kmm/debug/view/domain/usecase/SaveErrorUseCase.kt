package com.idfinance.kmm.debug.view.domain.usecase

import com.idfinance.kmm.debug.view.domain.repository.LogRepository

internal class SaveErrorUseCase(
    private val repository: LogRepository
) {

    suspend operator fun invoke(id: String, message: String?) {
        repository.saveErrorMessage(id, message)
    }

}