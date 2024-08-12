package com.idfinance.logkmpanion

import com.arkivanov.decompose.ComponentContext
import com.idfinance.logkmpanion.data.model.Log
import com.idfinance.logkmpanion.data.model.NetworkRequest
import com.idfinance.logkmpanion.data.model.Request
import com.idfinance.logkmpanion.data.model.Response
import com.idfinance.logkmpanion.data.model.Session
import com.idfinance.logkmpanion.domain.repository.LogRepository
import com.idfinance.logkmpanion.domain.usecase.ClearLogsUseCase
import com.idfinance.logkmpanion.domain.usecase.ClearNetworkLogsUseCase
import com.idfinance.logkmpanion.domain.usecase.GetAllLogsFlowUseCase
import com.idfinance.logkmpanion.domain.usecase.GetNetworkLogByIdFlowUseCase
import com.idfinance.logkmpanion.domain.usecase.GetNetworkLogsFlowUseCase
import com.idfinance.logkmpanion.domain.usecase.SaveErrorUseCase
import com.idfinance.logkmpanion.domain.usecase.SaveLogUseCase
import com.idfinance.logkmpanion.domain.usecase.SaveRequestUseCase
import com.idfinance.logkmpanion.domain.usecase.SaveResponseUseCase
import com.idfinance.logkmpanion.presentation.ui.allLogs.DefaultAllLogsComponent
import com.idfinance.logkmpanion.presentation.ui.networkLogs.DefaultNetworkLogsComponent
import com.idfinance.logkmpanion.presentation.ui.networkLogs.details.DefaultNetworkLogDetailsComponent
import com.idfinance.logkmpanion.presentation.ui.root.DefaultRootComponent
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

    private val repository: LogRepository by lazy {
        com.idfinance.logkmpanion.data.repository.LogRepository(
            realm
        )
    }

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
