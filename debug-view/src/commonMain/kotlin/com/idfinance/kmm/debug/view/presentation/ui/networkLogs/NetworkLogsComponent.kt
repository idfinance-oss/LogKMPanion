package com.idfinance.kmm.debug.view.presentation.ui.networkLogs

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debug.view.data.model.GroupedNetworkLogs
import com.idfinance.kmm.debug.view.presentation.ui.ModalComponent

internal interface NetworkLogsComponent {
    /**
     *
     */
    val model: Value<Model>

    val modal: Value<ChildSlot<*, ModalComponent>>

    /**
     *
     */
    fun clearLogs()

    fun openRequestDetails(id: String)

    data class Model(val logs: List<GroupedNetworkLogs> = emptyList())
}