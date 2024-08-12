package com.idfinance.logkmpanion.presentation.ui.networkLogs

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.idfinance.logkmpanion.presentation.ui.ModalComponent
import com.idfinance.logkmpanion.data.model.GroupedNetworkLogs

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