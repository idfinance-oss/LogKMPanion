package com.idfinance.logkmpanion

import android.content.Context
import com.idfinance.logkmpanion.domain.LogType
import com.idfinance.logkmpanion.presentation.component.openLogKMPanion
import kotlin.test.Test

class PublicApiContractAndroidTest {

    @Test
    fun topLevelFunctions_liveInTheSameFileFacadesAsInTheFullLibrary() {
        facade("com.idfinance.logkmpanion.domain.LoggerKt")
            .getMethod("addToLogKMPanion", LogType::class.java, String::class.java, String::class.java)
        facade("com.idfinance.logkmpanion.data.network.DebugViewNetworkPluginKt")
            .getMethod("logKMPanionNetworkPlugin", String::class.java)
        facade("com.idfinance.logkmpanion.data.network.DebugViewNetworkPluginKt")
            .getMethod("logKMPanionNetworkPlugin\$default", String::class.java, Int::class.javaPrimitiveType, Any::class.java)
        facade("com.idfinance.logkmpanion.presentation.component.LogKMPanionActivityKt")
            .getMethod("openLogKMPanion", Context::class.java)
    }

    @Suppress("unused")
    private fun callEveryAndroidApi(context: Context) {
        openLogKMPanion(context = context)
    }

    private fun facade(name: String): Class<*> = Class.forName(name, false, javaClass.classLoader)
}
