package com.idfinance.logkmpanion.presentation.ui.networkLogs.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.idfinance.logkmpanion.domain.usecase.GetNetworkLogByIdFlowUseCase
import com.idfinance.logkmpanion.presentation.extensions.disposableScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class DefaultNetworkLogDetailsComponent(
    id: String,
    context: ComponentContext,
    private val getNetworkLogByIdFlowUseCase: GetNetworkLogByIdFlowUseCase,
    private val onDismiss: () -> Unit
) : NetworkLogDetailsComponent, CoroutineScope by context.disposableScope() {

    private val _model = MutableValue(NetworkLogDetailsComponent.Model())
    override val model: Value<NetworkLogDetailsComponent.Model> = _model

    init {
        launch {
            getNetworkLogByIdFlowUseCase(id)
                .collect {
                    it.obj?.let {
                        _model.value = _model.value.copy(
                            method = it.request?.method ?: "-",
                            url = it.request?.url.orEmpty(),
                            statusCode = it.response?.statusCode,
                            duration = it.duration,
                            request = it.request?.let {
                                NetworkLogDetailsComponent.Model.Request(
                                    headers = it.headers.toList(),
                                    cookies = it.cookies.toList(),
                                    body = it.body
                                )
                            },
                            response = it.response?.let {
                                NetworkLogDetailsComponent.Model.Response(
                                    headers = it.headers.toList(),
                                    body = it.body,
                                    time = it.time
                                )
                            },
                            error = it.error
                        )
                    }
                }
        }
    }

    override fun onDismiss() {
        onDismiss.invoke()
    }
}