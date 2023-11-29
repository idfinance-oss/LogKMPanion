package com.idfinance.kmm.debugview

import com.arkivanov.decompose.ComponentContext
import com.idfinance.kmm.debugview.data.model.Log
import com.idfinance.kmm.debugview.data.repository.LogRepository
import com.idfinance.kmm.debugview.domain.usecase.ClearLogsUseCase
import com.idfinance.kmm.debugview.domain.usecase.GetLogsFlowUseCase
import com.idfinance.kmm.debugview.domain.usecase.SaveLogUseCase
import com.idfinance.kmm.debugview.presentation.decompose.DefaultDebugComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

internal object ServiceLocator {

    val saveLogUseCase: SaveLogUseCase
        get() = SaveLogUseCase(repository)

    fun getRootComponent(context: ComponentContext) =
        DefaultDebugComponent(context, getLogsFlowUseCase, clearLogsUseCase)

    private val getLogsFlowUseCase: GetLogsFlowUseCase
        get() = GetLogsFlowUseCase(repository)

    private val clearLogsUseCase: ClearLogsUseCase
        get() = ClearLogsUseCase(repository)

    private val repository: com.idfinance.kmm.debugview.domain.repository.LogRepository by lazy { LogRepository(realm) }

    private val realm by lazy {
        val config = RealmConfiguration.create(schema = setOf(Log::class))
        Realm.open(config)
    }
}
