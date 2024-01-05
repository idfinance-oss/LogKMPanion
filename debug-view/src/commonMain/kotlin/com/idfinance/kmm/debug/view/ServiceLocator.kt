package com.idfinance.kmm.debug.view

import com.arkivanov.decompose.ComponentContext
import com.idfinance.kmm.debug.view.data.model.Log
import com.idfinance.kmm.debug.view.data.model.NetworkRequest
import com.idfinance.kmm.debug.view.data.model.Request
import com.idfinance.kmm.debug.view.data.model.Response
import com.idfinance.kmm.debug.view.data.model.Session
import com.idfinance.kmm.debug.view.domain.repository.LogRepository
import com.idfinance.kmm.debug.view.domain.usecase.ClearLogsUseCase
import com.idfinance.kmm.debug.view.domain.usecase.ClearNetworkLogsUseCase
import com.idfinance.kmm.debug.view.domain.usecase.GetAllLogsFlowUseCase
import com.idfinance.kmm.debug.view.domain.usecase.GetNetworkLogByIdFlowUseCase
import com.idfinance.kmm.debug.view.domain.usecase.GetNetworkLogsFlowUseCase
import com.idfinance.kmm.debug.view.domain.usecase.SaveErrorUseCase
import com.idfinance.kmm.debug.view.domain.usecase.SaveLogUseCase
import com.idfinance.kmm.debug.view.domain.usecase.SaveRequestUseCase
import com.idfinance.kmm.debug.view.domain.usecase.SaveResponseUseCase
import com.idfinance.kmm.debug.view.presentation.ui.allLogs.DefaultAllLogsComponent
import com.idfinance.kmm.debug.view.presentation.ui.networkLogs.DefaultNetworkLogsComponent
import com.idfinance.kmm.debug.view.presentation.ui.networkLogs.details.DefaultNetworkLogDetailsComponent
import com.idfinance.kmm.debug.view.presentation.ui.root.DefaultRootComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

internal object ServiceLocator {

    val saveLogUseCase: SaveLogUseCase
        get() = SaveLogUseCase(repository)

    val saveRequestUseCase: SaveRequestUseCase
        get() = SaveRequestUseCase(repository)

    val saveResponseUseCase: SaveResponseUseCase
        get() = SaveResponseUseCase(repository)

    val saveErrorUseCase: SaveErrorUseCase
        get() = SaveErrorUseCase(repository)

    fun getRootComponent(context: ComponentContext, onClose: () -> Unit) =
        DefaultRootComponent(context, onClose)

    fun getAllLogsComponent(context: ComponentContext) =
        DefaultAllLogsComponent(context, getAllLogsFlowUseCase, clearLogsUseCase)

    fun getNetworkLogsComponent(context: ComponentContext) =
        DefaultNetworkLogsComponent(context, getNetworkLogsFlowUseCase, clearNetworkLogsUseCase)

    fun getNetworkLogDetailsComponent(
        id: String,
        context: ComponentContext,
        onDismiss: () -> Unit
    ) = DefaultNetworkLogDetailsComponent(id, context, getNetworkLogByIdFlowUseCase, onDismiss)

    private val getAllLogsFlowUseCase: GetAllLogsFlowUseCase
        get() = GetAllLogsFlowUseCase(repository)

    private val getNetworkLogsFlowUseCase: GetNetworkLogsFlowUseCase
        get() = GetNetworkLogsFlowUseCase(repository)

    private val clearLogsUseCase: ClearLogsUseCase
        get() = ClearLogsUseCase(repository)

    private val clearNetworkLogsUseCase: ClearNetworkLogsUseCase
        get() = ClearNetworkLogsUseCase(repository)

    private val getNetworkLogByIdFlowUseCase: GetNetworkLogByIdFlowUseCase
        get() = GetNetworkLogByIdFlowUseCase(repository)

    private val repository: LogRepository by lazy { com.idfinance.kmm.debug.view.data.repository.LogRepository(realm) }

    private val realm by lazy {
        val config = RealmConfiguration.create(
            schema = setOf(
                Log::class,
                NetworkRequest::class,
                Request::class,
                Response::class,
                Session::class
            )
        )
        Realm.open(config)
    }
}
