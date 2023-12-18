package com.idfinance.kmm.debug.view.presentation // ktlint-disable filename

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import com.idfinance.kmm.debug.view.ServiceLocator
import com.idfinance.kmm.debug.view.presentation.decompose.DebugComponent
import com.idfinance.kmm.debug.view.presentation.ui.LogView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.willMoveToParentViewController

@Suppress("FunctionName")
private fun DebugViewViewController(component: DebugComponent) = ComposeUIViewController {
    LogView(component)
}

@Deprecated("Use DebugViewViewController() directly")
object DebugViewManager {
    fun getDebugViewViewController(): UIViewController {
        return DebugViewViewController {}
    }
}

@Suppress("UNUSED")
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@ExportObjCClass
class DebugViewViewController(onClose: () -> Unit) : UIViewController(null, null) {
    private val lifecycle = LifecycleRegistry()
    private val component = ServiceLocator.getRootComponent(DefaultComponentContext(lifecycle), onClose)
    private val controller = DebugViewViewController(component)

    override fun viewDidLoad() {
        super.viewDidLoad()
        controller.willMoveToParentViewController(this)
        controller.view.setFrame(view.frame)
        view.addSubview(controller.view)
        addChildViewController(controller)
        controller.didMoveToParentViewController(this)
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        lifecycle.start()
    }

    override fun viewWillDisappear(animated: Boolean) {
        super.viewWillDisappear(animated)
        lifecycle.stop()
    }
}
