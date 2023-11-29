package com.idfinance.kmm.debugview.presentation.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

fun ComponentContext.disposableScope(
    context: CoroutineContext = Dispatchers.Main,
): CoroutineScope {
    val result = CoroutineScope(SupervisorJob() + context)
    lifecycle.doOnDestroy(result::cancel)
    return result
}
