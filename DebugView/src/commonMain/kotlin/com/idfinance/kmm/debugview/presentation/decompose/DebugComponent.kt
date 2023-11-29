package com.idfinance.kmm.debugview.presentation.decompose

import com.arkivanov.decompose.value.Value
import com.idfinance.kmm.debugview.data.model.Log

internal interface DebugComponent {
    /**
     *
     */
    val model: Value<Model>

    /**
     *
     */
    fun clearLogs()

    /**
     *
     */
    data class Model(val logs: List<Log> = emptyList()) {
        val concatenatedLog: String
            get() = logs.joinToString("\n") { "[${it.tag}] it.message" }
    }
}
