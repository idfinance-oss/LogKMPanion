package com.idfinance.logkmpanion.presentation.ui.environment

import com.arkivanov.decompose.value.Value
import com.idfinance.logkmpanion.domain.environment.DebugEnvironment

internal interface EnvironmentComponent {
    val model: Value<Model>

    fun selectEnvironment(id: String)
    fun changeCustomHost(host: String)
    fun apply()

    data class Model(
        val environments: List<DebugEnvironment> = emptyList(),
        val current: DebugEnvironment? = null,
        val selectedId: String? = null,
        val customHost: String = "",
        val isApplying: Boolean = false,
        val error: String? = null,
    ) {
        val selected: DebugEnvironment?
            get() = environments.firstOrNull { it.id == selectedId }

        val isCustomHostVisible: Boolean
            get() = selected?.isHostEditable == true

        val isApplyEnabled: Boolean
            get() = when {
                isApplying -> false
                selected == null -> false
                isCustomHostVisible -> customHost.isNotBlank() &&
                    (selectedId != current?.id || customHost.trim() != current?.host)
                else -> selectedId != current?.id
            }
    }
}
