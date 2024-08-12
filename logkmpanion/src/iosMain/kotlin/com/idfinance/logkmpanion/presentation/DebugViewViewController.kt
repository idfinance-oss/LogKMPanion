package com.idfinance.logkmpanion.presentation

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop
import com.idfinance.logkmpanion.ServiceLocator
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.willMoveToParentViewController

@Suppress("UNUSED", "FunctionName")
fun LogKMPanionViewControllerProvider(onClose: () -> Unit): UIViewController =
    LogKMPanionViewController(onClose)

@OptIn(ExperimentalForeignApi::class)
private class LogKMPanionViewController(onClose: () -> Unit) : UIViewController(null, null) {
    private val lifecycle = LifecycleRegistry()
    private val component =
        ServiceLocator.getRootComponent(DefaultComponentContext(lifecycle), onClose)
    private val controller = ComposeDebugViewViewController(component)

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
