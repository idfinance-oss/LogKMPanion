package com.idfinance.kmm.debug.view

import com.arkivanov.decompose.ComponentContext
import com.idfinance.kmm.debug.view.data.model.Log
import com.idfinance.kmm.debug.view.domain.repository.LogRepository
import com.idfinance.kmm.debug.view.domain.usecase.ClearLogsUseCase
import com.idfinance.kmm.debug.view.domain.usecase.GetLogsFlowUseCase
import com.idfinance.kmm.debug.view.domain.usecase.SaveLogUseCase
import com.idfinance.kmm.debug.view.presentation.decompose.DefaultDebugComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

internal object ServiceLocator {

    val saveLogUseCase: SaveLogUseCase
        get() = SaveLogUseCase(repository)

    fun getRootComponent(context: ComponentContext, onClose: () -> Unit) =
        DefaultDebugComponent(context, getLogsFlowUseCase, clearLogsUseCase, onClose)

    private val getLogsFlowUseCase: GetLogsFlowUseCase
        get() = GetLogsFlowUseCase(repository)

    private val clearLogsUseCase: ClearLogsUseCase
        get() = ClearLogsUseCase(repository)

    private val repository: LogRepository by lazy { com.idfinance.kmm.debug.view.data.repository.LogRepository(realm) }

    private val realm by lazy {
        val config = RealmConfiguration.create(schema = setOf(Log::class))
        Realm.open(config)
    }
}
