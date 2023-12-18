package com.idfinance.kmm.debug.view.presentation.decompose

import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debug.view.data.model.Log

internal interface DebugComponent {
    /**
     *
     */
    val model: Value<Model>

    /**
     *
     */
    fun clearLogs()
    fun close()

    /**
     *
     */
    data class Model(val logs: List<Log> = emptyList()) {
        val concatenatedLog: String
            get() = logs.joinToString("\n") { "[${it.tag}] it.message" }
    }
}
